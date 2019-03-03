//https://nlp.johnsnowlabs.com/quickstart.html

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import com.johnsnowlabs.nlp.pretrained.pipelines.en.BasicPipeline

import com.johnsnowlabs.nlp.base._
import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.annotators.ner.NerConverter

import scala.collection.mutable

object SparkNlp {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local[2]").setAppName("SparkNlp")

    val sc=new SparkContext(conf)

    val spark=SparkSession
      .builder()
      .appName("SparkNlp")
      .getOrCreate()

    import spark.implicits._
    spark.sparkContext.setLogLevel("WARN")

    //val path="/home/jouko/dev/software/spark-2.4.0-bin-hadoop2.7/README.md"
    val path="/home/jouko/dev/projects/TrainingSprints/SparkNlp2/data/pg10.txt"
    //https://www.oreilly.com/ideas/comparing-production-grade-nlp-libraries-running-spark-nlp-and-spacy-pipelines
    val data=spark.read.textFile(path).as[String].map(_.trim()).filter(_.nonEmpty).filter(x => x.count(_=='.')>1).map(x => findSentence(x) ).withColumnRenamed("value", "text")
    //val data=sc.textFile(path).map(_.split(".")).toDF().withColumnRenamed("value", "text")
    //val data=sc.textFile(path).map(_.split("\\.")).toDF().withColumnRenamed("value", "text")

    //data.printSchema()
    //data.show(100, false)

    val doc = new DocumentAssembler().
      setInputCol("text").
      setOutputCol("document")

    val sent = new SentenceDetector().
      //setCustomBounds(Array(System.lineSeparator()+System.lineSeparator())).
      setInputCols(Array("document")).
      setOutputCol("sentence")

    val tok = new Tokenizer().
      setInputCols(Array("document")).
      setOutputCol("token")

    val pos = PerceptronModel.
      pretrained().
      //load("/home/saif/cache_pretrained/pos_fast_en_1.6.1_2_1533853928168/").
      setInputCols("token", "document").
      setOutputCol("pos")

    val pipeline = new RecursivePipeline().setStages(
      Array(doc,
        sent,
        tok,
        pos)).fit(data)

    //pipeline.transform(data).select("document").show(false)


    //val data=spark.read.textFile("/home/jouko/dev/software/spark-2.4.0-bin-hadoop2.7/README.md").toDF("mainColumn")
    //val data=spark.read.format("csv").option("header", "false").option("delimiter", ".").load(path).toDF("mainColumn")
    //val data=Seq("hello, this is an example sentence").toDF("mainColumn")
    //BasicPipeline().annotate(data, "mainColumn").show()
    //BasicPipeline().annotate(data, "mainColumn").select("pos").take(10).map( x => getSVO(x.toString) ).foreach(println)
    BasicPipeline().annotate(data, "text").select("pos").map( x => getSVO(x.toString) ).take(10).filter(x => x.head!="" && x.tail.head!="" && x.tail.tail.head!="").foreach(println)


  }

  def getSVO(pos: String): List[String] = {
    val pos2=pos.replace("[WrappedArray(", "")
    val pos3=pos2.split("],")
    val pos4=pos3.map( x => if (x.split(",").length>4) {List(x.split(",")(3), getWordFromMap(x.split(",")(4)))} else {List("", "")} )
    var i=0
    var subject=""
    var verb=""
    var wobject=""
    var foundVerb=false
    for (i <- 0 to pos4.length-1) {
      if (pos4(i)(0).slice(0, 2)!="VB" && !foundVerb) {
        subject = subject + " " + pos4(i)(1)
      }
      else if (!foundVerb) { verb=pos4(i)(1); foundVerb=true}
      else { wobject=wobject+" "+pos4(i)(1)}
    }
    //pos4.map(x => x.toString).toList
    List(subject, verb, wobject)
  }

  def getWordFromMap(map: String): String = {
    map.split("->")(1).replace(")", "").replace("]]", "")
  }

  def findSentence(line: String): String = {
    val idx1=line.indexOf('.')
    val idx2=line.lastIndexOf('.')

    line.slice(idx1, idx2)
  }
}
