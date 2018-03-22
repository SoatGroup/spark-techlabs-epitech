
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.random.RandomRDDs
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Kmeans extends App {
  // Logger.getLogger("org").setLevel(Level.WARN)

  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("Word count")
    .getOrCreate()


  val sparkContext = spark.sparkContext


  val data: RDD[Vector] = RandomRDDs.normalVectorRDD(sparkContext, numRows = 1000000L, numCols = 4)


  val clusters: KMeansModel = KMeans.train(data, k = 2, maxIterations = 20)


  val WSSSE: Double = clusters.computeCost(data)
  println("Within Set Sum of Squared Errors = " + WSSSE)

  println("PREDICTION " + clusters.predict(data.take(100000)(19999)))

}
