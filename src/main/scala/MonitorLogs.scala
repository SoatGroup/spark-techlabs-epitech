import org.apache.log4j.{Level, Logger}
import org.apache.spark._
import org.apache.spark.streaming._

object MonitorLogs extends App{
  Logger.getLogger("org").setLevel(Level.ERROR)

  // Create a local StreamingContext with two working thread and batch interval of 1 second.
  // The master requires 2 cores to prevent a starvation scenario.

  val conf = new SparkConf().setMaster("local[*]").setAppName("MonitorLogs")
  val ssc = new StreamingContext(conf, Seconds(1))
  ssc.checkpoint("target/checkpoint")


  // Create a DStream that will connect to hostname:port, like localhost:9999
  val lines = ssc.socketTextStream("localhost", 9999)



  // Count each info in each batch
  val infosCount = lines
    .filter(_.toUpperCase.contains("INFO"))
    .map(info => ("INFO", 1))
    .reduceByKey(_ + _)
    //.updateStateByKey[Int](updateFunction _)
    //.reduceByKeyAndWindow((a:Int,b:Int) => (a + b), Seconds(30), Seconds(10))

  // Count each warn in each batch
  val warnCount = lines
    .filter(_.toUpperCase.contains("WARN"))
    .map(warn => ("WARN", 1))
    .reduceByKey(_ + _)
    //.updateStateByKey[Int](updateFunction _)
    //.reduceByKeyAndWindow((a:Int,b:Int) => (a + b), Seconds(30), Seconds(10))



  // Count each error in each batch
  val errorsCount = lines
    .filter(_.toUpperCase.contains("ERROR"))
    .map(error => ("ERROR", 1))
    .reduceByKey(_ + _)
    //.updateStateByKey[Int](updateFunction _)
    //.reduceByKeyAndWindow((a:Int,b:Int) => (a + b), Seconds(30), Seconds(10))


  infosCount.union(warnCount).union(errorsCount).print()

  ssc.start()             // Start the computation
  ssc.awaitTermination()  // Wait for the computation to terminate



  def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    val newCount = runningCount.getOrElse(0) +  newValues.sum // add the new values with the previous running count to get the new count
    Some(newCount)
  }

}

