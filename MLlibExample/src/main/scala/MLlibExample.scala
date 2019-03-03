

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession


object MLlib {
  def main(args: Array[String]) : Unit ={
  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")

  val sc = new SparkContext(conf)

    val spark = SparkSession
      .builder()
      .appName("MySparkApp")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .enableHiveSupport()
      .getOrCreate()

  val training = spark.read.format("libsvm").load("/home/jouko/dev/software/spark-2.4.0-bin-hadoop2.7/data/mllib/sample_libsvm_data.txt")


  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)

  // Fit the model
  val lrModel = lr.fit(training)

  // Print the coefficients and intercept for logistic regression
  println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

  // We can also use the multinomial family for binary classification
  val mlr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)
    .setFamily("multinomial")

  val mlrModel = mlr.fit(training)

  // Print the coefficients and intercepts for logistic regression with multinomial family
  println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
  println(s"Multinomial intercepts: ${mlrModel.interceptVector}")
  }
}