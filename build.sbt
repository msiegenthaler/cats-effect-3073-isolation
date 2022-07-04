ThisBuild / organization := "ch.linkyard"
ThisBuild / scalaVersion := "2.13.5"

lazy val root = (project in file(".")).settings(
  name := "cats-effect-3073-isolation",
  scalacOptions += "-Xasync",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.3.13",
    "org.typelevel" %% "cats-effect-cps" % "0.3.0",
    "co.fs2" %% "fs2-core" % "3.2.9",
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )
)
