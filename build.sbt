import sbt.ProjectSettings.Dependencies._
import sbtassembly.MergeStrategy
import sbt.ProjectSettings.Dependencies._
import sbt.ProjectSettings.{Assembly, commonSettings}

name := "spark-hubtalk-epitech"
version := "1.0"
scalaVersion := "2.11.0"

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
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case other => MergeStrategy.first
}

lazy val commonSettings = Seq(
  organization := "com.kering.iods",
  scalaVersion := "2.11.8"
)

lazy val spark_epitech = project.in(file("."))
  .settings(name := "spark-hubtalk-epitech",
    sparkCoreDependency,
    sparkSqlDependency,
    sparkStreamingDependency,
    datastaxConnectorDependency,
    lazyLoggingDependency,
    sparkMllibDependency,
    extendedCommonSettings
  )


lazy val extendedCommonSettings = commonSettings ++ Seq(
  Assembly.disableTestsInAssembly,
  Assembly.mergeStrategySetting,
  assemblyJarName := s"spark-hubtalk-epitech-assembly-${version.value}.jar"
) ++ Overrides.jacksonOverrides


unmanagedResourceDirectories in Compile += { baseDirectory.value / "src/main/resources/" }