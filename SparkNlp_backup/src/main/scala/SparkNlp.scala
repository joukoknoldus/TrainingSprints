//https://nlp.johnsnowlabs.com/quickstart.html

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import com.johnsnowlabs.nlp.pretrained.pipelines.en.BasicPipeline

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

    val data=spark.read.textFile("/home/jouko/dev/software/spark-2.4.0-bin-hadoop2.7/README.md").toDF("mainColumn")

    //val data=Seq("hello, this is an example sentence").toDF("mainColumn")
    BasicPipeline().annotate(data, "mainColumn").show()
    BasicPipeline().annotate(data, "mainColumn").select("pos").take(10).map( x => getSVO(x.toString) ).foreach(println)
    //BasicPipeline().annotate(data, "mainColumn").select("pos").take(10).map( x => x(0).toString ).foreach(println)

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
}
