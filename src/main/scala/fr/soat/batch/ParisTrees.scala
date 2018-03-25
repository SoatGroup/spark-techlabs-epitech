package fr.soat.batch

import java.net.InetAddress

import com.datastax.spark.connector.cql.CassandraConnector
import com.typesafe.scalalogging.LazyLogging
import fr.soat.model.TreesPerZone
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

object ParisTrees extends LazyLogging {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)



    val dataPath = if(args.isEmpty) "data/trees/les-arbres.csv" else "/tmp/upload/data/trees/les-arbres.csv"

    // Spark
    val spark = if(args.isEmpty) SparkSession
      .builder()
      .master("local[1]")
      .appName("Paris Trees")
      .getOrCreate()
    else
      SparkSession
        .builder()
        .appName("Paris Trees")
        .getOrCreate()


    logger.warn("Using : " + spark.conf.getAll)

    val treesDataset = spark
      .read
      .option("header", "true")
      .option("delimiter", ";")
      .csv(dataPath)


    // Schema
    println("# Schema du dataset #")
    treesDataset.printSchema()


    // Extrait
    println("# Afichage du dataset complet (select *) #")
    treesDataset.show(10,false)


    // Requete simple
    println("# Requete simple #")
    val simpleRequest = treesDataset.select("LIBELLEFRANCAIS").distinct()
    simpleRequest.show(10)


    // Requete Select Expr
    println("# Requete Criconference max #")
    val maxCirc = treesDataset.selectExpr("max(`CIRCONFERENCEENCM`) as `Hauteur maximum (m)`")
    maxCirc.show(false)


    // Décompte par arrondissemnt
    println("# Arbres par arrondissement #")
    val treesPerZone = treesDataset
      .groupBy("ARRONDISSEMENT")
      .count()
      .orderBy("count")
      .withColumnRenamed("ARRONDISSEMENT","zone")
      .withColumnRenamed("count","tree_count")

    treesPerZone.cache()

    treesPerZone.show(100, false);


    // Stockage sur Cassandra
    /*
    treesPerZone
      .write
      .format("org.apache.spark.sql.cassandra")
      .options(
        Map(
          "spark.cassandra.connection.host" -> "cassandra",
          "table" -> "trees",
          "keyspace" -> "paris"
        )
      )
      .mode(SaveMode.Overwrite)
      .save()
    */


    // Dataset : DataFrame[TreesPerZone]
    import spark.implicits._
    val typedTreesPerZone = spark
      .read
      .option("header", true)
      .csv("data/output/treesPerZone.csv")
      .as[TreesPerZone]
    

    // scala.io.StdIn.readLine()



  }

}
