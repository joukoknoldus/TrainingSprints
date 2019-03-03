import java.io.File

import edu.stanford.nlp.coref.data.CorefChain
import edu.stanford.nlp.ling._
import edu.stanford.nlp.ie.util._
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.semgraph._
import edu.stanford.nlp.trees._
import java.util._

import edu.stanford.nlp.naturalli.NaturalLogicAnnotations
import edu.stanford.nlp.ie.util.RelationTriple
import edu.stanford.nlp.io.IOUtils
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations
import edu.stanford.nlp.util.CoreMap
import org.apache.log4j.BasicConfigurator

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object StanfordNlpTriples {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local[2]").setAppName("StanfordNlpTriples")

    val sc=new SparkContext(conf)

    val spark=SparkSession
      .builder()
      .appName("StanfordNlpTriples")
      .getOrCreate()


    val props = new Properties()

    import edu.stanford.nlp.ling.CoreLabel
    import edu.stanford.nlp.pipeline.CoreDocument
    import edu.stanford.nlp.pipeline.StanfordCoreNLP
    import spark.implicits._


    val path="/home/jouko/dev/software/spark-2.4.0-bin-hadoop2.7/README.md"
    //val reader=DocumentReader.getReader(path)
    //val document=reader.readText()
    //val doc=DocumentReader.parseDocumentText(path)

    BasicConfigurator.configure()

    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie")


    val pipeline=new StanfordCoreNLP(props)
    val doc=new Annotation(IOUtils.slurpFile(new File(path)))
    pipeline.annotate(doc)

    val sentences=doc.get(classOf[CoreAnnotations.SentencesAnnotation])

    var i=0
    for (i <- 0 to sentences.size-1) {
      var triple=sentences.get(i).get(classOf[NaturalLogicAnnotations.RelationTriplesAnnotation])
      println(triple)
    }
    //sentences.forEach{ sentence => sentence.get(classOf[NaturalLogicAnnotations.RelationTriplesAnnotation])}
    //for (sentence <- sentences) {
    //  var triple=sentence.get(classOf[NaturalLogicAnnotations.RelationTriplesAnnotation])
    //  println(triple)
    //}
    /*
    val sentences=doc.get(classOf[CoreAnnotations.SentencesAnnotation])
    val triple=sentences.get(1).get(classOf[NaturalLogicAnnotations.RelationTriplesAnnotation])

    println("sentences")
    println(sentences)
    println("sentence")
    println(sentences.get(1))
    println("triple")
    println(triple)
*/



  }
}
