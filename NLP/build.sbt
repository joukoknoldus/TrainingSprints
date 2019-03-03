name := "NLP"

version := "0.1"

scalaVersion := "2.11.12"


resolvers ++= Seq(
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo",
  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "org.scalanlp" %% "epic" % "0.2",
  "org.scalanlp" %% "epic-parser-en-span" % "2014.9.15",
  "org.scalanlp" %% "epic-ner-en-conll" % "2014.10.26",
  "junit" % "junit" % "4.5" % "test"
)


credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")


scalacOptions ++= Seq("-deprecation", "-language:_", "-optimize")

javaOptions += "-Xmx2g"

