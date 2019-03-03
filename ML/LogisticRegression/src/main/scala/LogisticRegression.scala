import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.Imputer
import org.apache.spark.sql.{Column, Row, SparkSession}
import org.apache.spark.ml.linalg.{Matrix, Vector, Vectors}
import org.apache.spark.ml.stat.{ChiSquareTest, Correlation, Summarizer}
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

import scala.util.Try

object LogisticRegression {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("BasicStatistics")

    val sc = new SparkContext(conf)

    val spark = SparkSession
      .builder()
      .appName("BasicStatistics")
      .getOrCreate()

    import spark.implicits._
    import Summarizer._
    spark.sparkContext.setLogLevel("WARN")

    val dataDir="/home/jouko/dev/projects/TrainingSprints/ML/BasicStatistics/src/main/data"
    val trainingPath = dataDir+"/train.csv"
    val testPath = dataDir+"/test.csv"
    val trainingDf=readAndTransformData(trainingPath, "training", spark)
    val testDf=readAndTransformData(testPath, "test", spark)

    val lr=new LogisticRegression()

    val lrModel=lr.fit(trainingDf)
    println("Coefficients= "+lrModel.coefficients)
    println("Intercept= "+lrModel.intercept)

    val dfPredicted=lrModel.transform(testDf).select("features", "probability", "prediction", "PassengerId")

    dfPredicted.show()

    val pathOut=dataDir+"/prediction.csv"
    val id: Column=dfPredicted.col("PassengerId")
    val prediction: Column=dfPredicted.col("prediction").as("Survived")

    val dfPredicted2=dfPredicted.withColumn("predictedTemp", prediction.cast(IntegerType)).drop("Survived").withColumnRenamed("predictedTemp", "Survived")

    val prediction2: Column=dfPredicted2.col("Survived")

    dfPredicted2.select(id, prediction2).repartition(1).write.format("com.databricks.spark.csv").option("header", "true").save(pathOut)
  }

  def readFile(path: String, spark: SparkSession): DataFrame = {
    //spark.read.format("csv").option("header", "true").option("inferSchema", "true").load(path).na.fill(0)
    spark.read.format("csv").option("header", "true").option("inferSchema", "true").load(path)
  }

  def getGender(df: DataFrame): Column = {
    when(df.col("Sex") === "male", 1).otherwise(0).as("IntSex")
  }

  def pickColumns(df: DataFrame, dataType: String): DataFrame = {
    //val df2=df.na.fill((("Pclass"), 2))
    df.printSchema()
    val df2=df.withColumn("doublePclass", col("Pclass").cast(DoubleType)).drop("Pclass").withColumnRenamed("doublePclass", "Pclass")
    //val imputer=new Imputer().setInputCols(df.columns).setOutputCols(df.columns.map(c => s"${c}_imputed")).setStrategy("mean")
    val imputer=new Imputer().setInputCols(Array("Pclass", "Age", "Fare")).setOutputCols(Array("Pclass_f", "Age_f", "Fare_f")).setStrategy("mean")
    val df3=imputer.fit(df2).transform(df2)
    val intSex=getGender(df3)
    val pclass: Column = df3.col("Pclass_f").as("Pclass")
    val age: Column = df3.col("Age_f").as("Age")
    val fare: Column = df3.col("Fare_f").as("Fare")
    val id: Column = df.col("PassengerId")

    if (dataType=="training") {
      val survived: Column = df.col("Survived").as("label")
      df3.select(id, survived, intSex, pclass, age, fare)
    } else {
      df3.select(id, intSex, pclass, age, fare)
    }
  }

  def readAndTransformData(path: String, dataType: String, spark: SparkSession): DataFrame = {
    val df = readFile(path, spark)
    val df2 = pickColumns(df, dataType)

    val assembler = new VectorAssembler().setInputCols(Array("IntSex", "Age", "Pclass", "Fare")).setOutputCol("features")
    assembler.transform(df2)
  }

}

