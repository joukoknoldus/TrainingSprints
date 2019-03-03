//import sbt.Keys.unmanagedJars

name := "StanfordNlpTriples"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.4.0"

//unmanagedJars := ("/home/jouko/dev/software/stanford-corenlp-full-2018-10-05/*.jar").classpath


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  
  "edu.stanford.nlp" % "stanford-corenlp" % "3.9.2",
  //"edu.stanford.nlp" % "stanford-postag-models"
  "edu.stanford.nlp" % "stanford-corenlp" % "3.9.2" classifier "models-english",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.9.2" classifier  "models"
  
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
