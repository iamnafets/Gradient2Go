package jobs

import play.api.Play.current
import play.api.libs.ws._
import play.api.Logger
import java.nio.file._
import play.api.libs.concurrent.Execution.Implicits._
import java.lang.System
import awscala._
import scala.io.Source
import s3._
import java.net.InetAddress

object Scrape {
  val Cities = Seq("seattle", "austin")

  // I'm getting protocol_version mismatches without this, don't want to spend a lot
  // of time debugging.
  System.setProperty("jdk.tls.client.protocols", "TLSv1")

  implicit val s3: S3 = S3.at(Region.Oregon)

  val landingDirectory = Files.createTempDirectory("scrape2go_landing")
  Logger.info("Writing to " + landingDirectory.toString)

  def scrapeApi = {
    Logger.info("Calling Car2Go API")
    val cityScrapes = Cities.map { city =>
      (city, WS
        .url("http://www.car2go.com/api/v2.1/vehicles?loc=" + city + "&oauth_consumer_key=car2gowebsite&format=json")
        .get)
    }

    cityScrapes.map {
      case (city, future) =>
        future.recover {
          case e: Exception =>
            Logger.error("Failed to scrape API", e)
        }
    }

    cityScrapes map {
      case (city, future) => future map { response =>
        val end = System.currentTimeMillis()
        val destinationFile = Files.createTempFile(landingDirectory, "writing", "")
        Files.write(destinationFile, response.body.getBytes())
        Logger.info("Wrote to " + destinationFile + " for city " + city)

        Files.move(
          destinationFile,
          destinationFile.resolveSibling("written_" + city + "_" + InetAddress.getLocalHost().getHostName() + "_" + end))
      }
    }
  }

  def pushToS3 = {
    Cities map { city =>
      s3.bucket("scrape2go") map { bucket =>
        val activeFiles = landingDirectory.toFile.listFiles.filter(_.getName.startsWith("written_" + city))
        val contentList = activeFiles.map { file =>
          val content = Source.fromFile(file).mkString
          val end = file.getName.replace("written_" + city + "_" + InetAddress.getLocalHost().getHostName() + "_", "").toLong
          "\"" + end + "\": " + content
        }

        if (!contentList.isEmpty) {
          val stagingContent = "{ \"" + city + "\": {" + contentList.mkString(",") + "}}"
          Logger.info("Trying to upload " + stagingContent.size + " segments to S3")
          val stagingFile = Files.createTempFile(landingDirectory, "to_s3", "")
          Files.write(stagingFile, stagingContent.getBytes())

          bucket.put(InetAddress.getLocalHost().getHostName() + "/" + city + "/" + System.currentTimeMillis, stagingFile.toFile)
          Files.delete(stagingFile)
          Logger.info("Succeeded in upload " + contentList.size + " segments, " + stagingContent.size + " bytes to S3")

          activeFiles.map { file =>
            file.delete
          }
        } else {
          Logger.info("No segments to upload to S3.")
        }
      }
    }
  }

  def cleanUp = {
    landingDirectory.toFile.listFiles.map { _.delete }
    Files.delete(landingDirectory)
  }
}

