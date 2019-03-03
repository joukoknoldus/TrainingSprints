//import sbt.Keys.unmanagedJars

name := "StanfordNlpTriples"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.4.0"
//val nlpVersion="3.6.0"
val nlpVersion="3.9.2"

resolvers ++= Seq("apache-snapshots" at "http://repository.apache.org/snapshots")



libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  
  "edu.stanford.nlp" % "stanford-corenlp" % nlpVersion,
  //"edu.stanford.nlp" % "stanford-postag-models"
  "edu.stanford.nlp" % "stanford-corenlp" % nlpVersion classifier "models-english",
  "edu.stanford.nlp" % "stanford-corenlp" % nlpVersion classifier "models",
  
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5"
  
  
)


/*
<dependencies>
  <dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>3.9.2</version>
  </dependency>
  <dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>3.9.2</version>
    <classifier>models</classifier>
  </dependency>
</dependencies>
*/
