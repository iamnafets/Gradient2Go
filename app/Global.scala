import play.api._
import play.libs._
import play.api.Logger
import scala.concurrent.duration._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import jobs._
import akka.actor._

object Global extends GlobalSettings {

  var scraperCancel: Option[Cancellable] = None
  var s3Cancel: Option[Cancellable] = None

  override def onStart(app: Application) = {
    Logger.info("Scheduling scraping operations...")
    scraperCancel = Some(Akka.system.scheduler.schedule(0.microsecond, 5.second) { Scrape.scrapeApi })
    s3Cancel = Some(Akka.system.scheduler.schedule(0.microsecond, 60.second) { Scrape.pushToS3 })
  }

  override def onStop(app: Application) = {
    Logger.info("Cleaning up scraping operations...")
    scraperCancel map (_.cancel)
    s3Cancel.map(_.cancel)
    Scrape.cleanUp
    Logger.info("Done cleaning up scraping operations.")
  }

}
