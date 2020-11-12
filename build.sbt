name := "blake3-jni"
organization := "org.alephium"
homepage := Some(url("https://github.com/alephium/blake3-jni"))
licenses := Seq("LGPL 3.0" -> new URL("https://www.gnu.org/licenses/lgpl-3.0.en.html"))
developers := List(
  Developer(
    id    = "alephium core dev",
    name  = "alephium core dev",
    email = "dev@alephium.org",
    url   = url("https://alephium.org/")
  )
)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  "io.circe" %% "circe-parser" % "0.13.0" % "test",
  "io.circe" %% "circe-generic" % "0.13.0" % "test"
)

fork := true

Compile / unmanagedResourceDirectories += baseDirectory.value / "lib"

javaOptions += s"-Djava.library.path=${baseDirectory.value}/lib"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explaintypes",
  "-feature",
  "-unchecked",
  "-Xsource:3",
  "-Xfatal-warnings",
  "-Xlint:adapted-args",
  "-Xlint:constant",
  "-Xlint:delayedinit-select",
  "-Xlint:doc-detached",
  "-Xlint:inaccessible",
  "-Xlint:infer-any",
  "-Xlint:missing-interpolator",
  "-Xlint:nullary-unit",
  "-Xlint:option-implicit",
  "-Xlint:package-object-classes",
  "-Xlint:poly-implicit-overload",
  "-Xlint:private-shadow",
  "-Xlint:stars-align",
  "-Xlint:type-parameter-shadow",
  "-Xlint:nonlocal-return",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard"
)
