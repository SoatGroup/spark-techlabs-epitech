name := "spark-techlabs-epitech"
version := "1.0"
scalaVersion := "2.11.0"

// Spark core
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0"

// Spark SQL
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.0"

// Spark Streaming
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.3.0" % "provided"

// Spark MLlib
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.3.0"

// Logger
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"

// plot
libraryDependencies += "co.theasi" %% "plotly" % "0.2.0"