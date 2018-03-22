import breeze.util.LazyLogger
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import com.typesafe.scalalogging.slf4j.LazyLogging

object ParisTrees extends LazyLogging {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Paris Trees")
      .getOrCreate()

    val treesDataset = spark
      .read
      .option("header", "true")
      .option("delimiter", ";")
      .csv("data/trees/les-arbres.csv")


    // Schema
    println("# Schema du dataset #")
    treesDataset.printSchema()


    // Extrait
    println("# Afichage du dataset complet (select *) #")
    treesDataset.show(10,false)


    // Requete simple
    println("# Requete simple #")
    treesDataset.select("LIBELLEFRANCAIS").distinct().show(10);


    // Requete Select Expr
    println("# Requete Criconference max #")
    treesDataset.selectExpr("max(`CIRCONFERENCEENCM`) as `Hauteur maximum (m)`").show(false)

    // Décompte par arrondissemnt
    println("# Arbres par arrondissement #")
    treesDataset
      .groupBy("ARRONDISSEMENT")
      .count()
      .orderBy("count")
      .withColumnRenamed("count","Nombre d'arbres")
      .show(100, false);




    scala.io.StdIn.readLine()



  }

}
