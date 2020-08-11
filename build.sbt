scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  "io.circe" %% "circe-parser" % "0.13.0" % "test",
  "io.circe" %% "circe-generic" % "0.13.0" % "test"
)
