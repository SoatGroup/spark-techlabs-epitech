import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object WordCountRDD  extends App{

  Logger.getLogger("org").setLevel(Level.WARN)

  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("Word count")
    .getOrCreate()


  val sparkContext = spark.sparkContext

  val wordsRDD = sparkContext.textFile("data/words/mag_pdf-programmez211.txt")

  wordsRDD
    .flatMap(_.split("\\s"))
    .map(
       _.replaceAll("[^a-zA-Z]+","")
        .trim
        .toLowerCase)
    .filter(_.length>2)
    .filter(_.length<20)
    .filter(!_.isEmpty)
    .map((_,1))
    .reduceByKey(_ + _)
    .sortBy(_._2, false)
    .saveAsTextFile("target/words/counts")
}
