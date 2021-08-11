organization := "io.kontainers"

name := "micrometer-akka"

scalaVersion := "2.13.6"

crossScalaVersions := Seq("2.11.12", "2.12.14", scalaVersion.value)

scalacOptions += "-target:jvm-1.8"

def sysPropOrDefault(propName: String, default: String): String = Option(System.getProperty(propName)) match {
  case Some(propVal) if !propVal.trim.isEmpty => propVal.trim
  case _ => default
}

val akkaDefaultVersion = "2.5.32"
def akkaVersion(scalaVersion: String) = sysPropOrDefault("akka.version", akkaDefaultVersion)
val aspectjweaverVersion = "1.9.7"
val micrometerVersion = "1.7.3"

update / checksums := Nil

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.32",
  "io.micrometer" % "micrometer-core" % micrometerVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion(scalaVersion.value),
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion(scalaVersion.value),
  "com.typesafe" % "config" % "1.4.1",
  "org.aspectj" % "aspectjweaver" % aspectjweaverVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion(scalaVersion.value) % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion(scalaVersion.value) % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.5" % Test
)

enablePlugins(JavaAgent)
javaAgents += "org.aspectj" % "aspectjweaver" % aspectjweaverVersion % Test

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

Test / parallelExecution := false
logBuffered := false

Test / javaOptions += s"""-Dconfig.resource=${sysPropOrDefault("config.resource", "application.conf")}"""

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

homepage := Some(url("https://github.com/kontainers/micrometer-akka"))

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

releasePublishArtifactsAction := PgpKeys.publishSigned.value

pomExtra := (
  <scm>
    <url>git@github.com:kontainers/micrometer-akka.git</url>
    <connection>scm:git:git@github.com:kontainers/micrometer-akka.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pjfanning</id>
      <name>PJ Fanning</name>
      <url>https://github.com/pjfanning</url>
    </developer>
  </developers>
)
