name := """gradient2go"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.github.seratch" %% "awscala" % "0.3.+"
)
