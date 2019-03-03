import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{Column, Row, SparkSession}
import org.apache.spark.ml.linalg.{Matrix, Vector, Vectors}
import org.apache.spark.ml.stat.{ChiSquareTest, Correlation, Summarizer}
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object BasicStatistics {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local[2]").setAppName("BasicStatistics")

    val sc=new SparkContext(conf)

    val spark=SparkSession
      .builder()
      .appName("BasicStatistics")
      .getOrCreate()

    import spark.implicits._
    import Summarizer._

    val path="/home/jouko/dev/projects/TrainingSprints/ML/BasicStatistics/src/main/data/train.csv"
    val df=spark.read.format("csv").option("header", "true").option("inferSchema", "true").load(path).na.fill(0)
    val intSex: Column = when(df.col("Sex")==="male", 1).otherwise(0).as("IntSex")
    val survived: Column=df.col("Survived")
    val pclass: Column=df.col("Pclass")
    val age: Column=df.col("Age")
    val fare: Column=df.col("Fare")
    val df2=df.select(survived, intSex, pclass, age, fare)
    val assembler=new VectorAssembler().setInputCols(Array("IntSex", "Age", "Pclass", "Fare")).setOutputCol("features")
    val df3=assembler.transform(df2)

    df3.show(false)

/*
    val data=Seq(
      Vectors.sparse(4, Seq((0, 1.0), (3, -2.0))),
      Vectors.dense(4.0, 5.0, 0.0, 3.0),
      Vectors.dense(6.0, 7.0, 0.0, 8.0),
      Vectors.sparse(4, Seq((0, 9.0), (3, 1.0)))
    )
*/
    /*
    val data = Seq(
      (Vectors.dense(2.0, 3.0, 5.0), 1.0),
      (Vectors.dense(4.0, 6.0, 7.0), 2.0)
    )

    val df_example = data.toDF("features", "weight")
*/
    //val df_example=data.map(Tuple1.apply).toDF("features")


    val Row(coeff1: Matrix)=Correlation.corr(df3, "features").head
    println(s"Pearson correlation matrix:\n $coeff1")

    val Row(coeff2: Matrix)=Correlation.corr(df3, "features", "spearman").head
    println(s"Spearman correlation matrix:\n $coeff2")

    //This should only be for categorical data.
    val chi=ChiSquareTest.test(df3, "features", "Survived").head

    println(s"pValues= ${chi.getAs[Vector](0)}")
    println(s"degreesOfFreedom ${chi.getSeq[Int](1).mkString("[", ",", "]")}")
    println(s"statistics ${chi.getAs[Vector](2)}")

    df3.printSchema()


    //val varianceVal=df_example.select(variance($"features")).as[Vector[Double]].first()
    val (meanVal, varianceVal) = df3.select(mean($"features"), variance($"features"))
      .as[(Vector, Vector)].first()

    println("Mean= "+meanVal)
    println("Variance= "+varianceVal)

  }

}
