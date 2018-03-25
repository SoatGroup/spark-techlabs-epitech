package fr.soat.ml

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.{DistributedLDAModel, LDA}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SparkSession

object LDA extends App{

  Logger.getLogger("org").setLevel(Level.WARN)

  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("Word count")
    .getOrCreate()


  val sparkContext = spark.sparkContext


  // Load and parse the data
  val data = sparkContext.textFile("data/lda/lda.txt")
  val parsedData = data.map(s => Vectors.dense(s.trim.split(' ').map(_.toDouble)))
  // Index documents with unique IDs
  val corpus = parsedData.zipWithIndex.map(_.swap).cache()

  // Cluster the documents into three topics using LDA
  val ldaModel = new LDA().setK(3).run(corpus)

  // Output topics. Each is a distribution over words (matching word count vectors)
  println("Learned topics (as distributions over vocab of " + ldaModel.vocabSize + " words):")
  val topics = ldaModel.topicsMatrix
  for (topic <- Range(0, 3)) {
    print("Topic " + topic + ":")
    for (word <- Range(0, ldaModel.vocabSize)) { print(" " + topics(word, topic)); }
    println()
  }

  // Save and load model.
  ldaModel.save(sparkContext, "target/org/apache/spark/LatentDirichletAllocationExample/LDAModel")
  val sameModel = DistributedLDAModel.load(sparkContext,
    "target/org/apache/spark/LatentDirichletAllocationExample/LDAModel")
}
