package sbt

import Keys._
import sbtassembly.AssemblyPlugin.autoImport.{assembly, assemblyMergeStrategy}
import sbtassembly.{MergeStrategy, PathList}

object ProjectSettings {

  lazy val commonSettings = Seq(
    organization := "com.kering.iods",
    scalaVersion := Versions.scala
  )

  object Versions {

    lazy val scala = "2.11.8"

    lazy val spark = "2.1.1"
    lazy val datastaxConnector = "2.0.2"
    lazy  val typeSafeLogging = "3.5.0"
  }

  object Dependencies {

    lazy val sparkCoreDependency = libraryDependencies +=
      "org.apache.spark" %% "spark-core" % Versions.spark

    lazy val sparkSqlDependency = libraryDependencies +=
      "org.apache.spark" %% "spark-sql" % Versions.spark

    lazy val sparkStreamingDependency = libraryDependencies +=
      "org.apache.spark" %% "spark-streaming" % Versions.spark

    lazy val sparkMllibDependency = libraryDependencies +=
      "org.apache.spark" %% "spark-mllib" % Versions.spark

    lazy val datastaxConnectorDependency = libraryDependencies +=
      "com.datastax.spark" %% "spark-cassandra-connector" % Versions.datastaxConnector

    lazy val lazyLoggingDependency = libraryDependencies +=
      "com.typesafe.scala-logging" %% "scala-logging" % Versions.typeSafeLogging

    object Overrides {

      lazy val jacksonOverrides = Seq(
        dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7",
        dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7",
        dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"
      )
    }
  }

  object Assembly {

    lazy val mergeStrategySetting: Setting[String => MergeStrategy] = assemblyMergeStrategy in assembly := {
      case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
      case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
      case PathList("org", "apache", xs @ _*) => MergeStrategy.last
      case PathList("ch","qos","logback", xs @ _*) => MergeStrategy.first
      case PathList("org", "slf4j", "slf4j-log4j12", xs @ _*) => MergeStrategy.last
      case PathList("com", "codhale", "metrics", xs @ _*) => MergeStrategy.discard
      case PathList("io", "dropwizard", "metrics", xs @ _*) => MergeStrategy.discard
      case PathList("org", "apache", "calcite", xs @ _*) => MergeStrategy.discard
      case PathList("org", "glassfish", "jersey", "core", xs @ _*) => MergeStrategy.discard
      case PathList("com", "sun", "jersey", xs @ _*) => MergeStrategy.discard
      case PathList("javax", "ws", "rs", xs @ _*) => MergeStrategy.discard
      case PathList("stax", xs @ _*) => MergeStrategy.discard
      case PathList("xml-apis", xs @ _*) => MergeStrategy.discard
      case PathList("javax", "xml", "stream", xs @ _*) => MergeStrategy.discard
      case PathList("org", "ow2", "asm", xs @ _*) => MergeStrategy.discard
      case PathList("org", "objenesis", xs @ _*) => MergeStrategy.discard
      case other => (assemblyMergeStrategy in assembly).value.apply(other)
    }

    lazy val disableTestsInAssembly = test in assembly := {}

  }

}
