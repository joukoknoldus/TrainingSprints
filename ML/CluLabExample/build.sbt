name := "CluLabExample"

version := "0.1"

scalaVersion := "2.11.12"

//import ReleaseTransformations._

/*
lazy val commonSettings = Seq(
  organization := "org.clulab",
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.11", "2.12.4"),
  scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),
  parallelExecution in Test := false,
  scalacOptions in (Compile, doc) += "-no-link-warnings", // suppresses problems with scaladoc @throws links
*/





libraryDependencies ++= {
  val procVer = "6.1.5"

  Seq(
    "org.clulab" %% "processors-main" % procVer,
    "org.clulab" %% "processors-corenlp" % procVer,
    "org.clulab" %% "processors-odin" % procVer,
    "org.clulab" %% "processors-modelsmain" % procVer,
    "org.clulab" %% "processors-modelscorenlp" % procVer,
  )
}