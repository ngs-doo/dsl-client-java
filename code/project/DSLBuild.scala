import sbt._
import Keys._

// Eclipse plugin
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

// Dependency graph plugin
import net.virtualvoid.sbt.graph.Plugin._

// ----------------------------------------------------------------------------

trait Default {
  val defaultSettings =
    Defaults.defaultSettings ++
    eclipseSettings ++
    graphSettings ++ Seq(
      version := "0.4.15-SNAPSHOT"
    , organization := "com.dslplatform"

    , scalaVersion := "2.10.4"
    , crossPaths := false
    , autoScalaLibrary := false

    , javacOptions in doc := Seq(
        "-encoding", "UTF-8"
      , "-source", "1.6"
      ) ++ (sys.env.get("JDK16_HOME") match {
        case Some(jdk16Home) => Seq("-bootclasspath", 
            Seq("rt", "jsse")
             .map(jdk16Home + "/jre/lib/" + _ + ".jar")
             .mkString(";"))
        case _ => Nil
      })
    , javacOptions := Seq(
        "-deprecation"
      , "-Xlint"
      , "-target", "1.6"
      ) ++ (javacOptions in doc).value

    , unmanagedSourceDirectories in Compile := Seq((javaSource in Compile).value)
    , unmanagedSourceDirectories in Test := Seq((javaSource in Test).value)
    , EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
    , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
    , EclipseKeys.eclipseOutput := Some(".target")
    )
}

// ----------------------------------------------------------------------------

trait Dependencies {
  // JodaTime
  val jodaTime = "joda-time" % "joda-time" % "2.3"

  // Json serialization
  val jackson = "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.2"

  // Logging facade
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"

  // Apache HttpClient
  val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.3.3"

  // Apache commons
  val commonsIo = "commons-io" % "commons-io" % "2.4"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.9"

  // Amazon Web Services SDK (S3 type)
  val aws = "com.amazonaws" % "aws-java-sdk" % "1.7.5" % "provided"

  // Akka Actor (contains the Serializer)
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.3.1" % "provided"

  // Android SDK
  val androidSDK = "com.google.android" % "android" % "4.1.1.4" % "provided"

  // Testing
  val junit = "junit" % "junit" % "4.11" % "test"
  val jsonAssert = "org.skyscreamer" % "jsonassert" % "1.2.3" % "test"
  val xmlUnit = "xmlunit" % "xmlunit" % "1.4" % "test"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.1" % "test"
}

// ----------------------------------------------------------------------------

object NGSBuild extends Build with Default with Dependencies {
  lazy val core = Project(
    "core"
  , file("core")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-Core"
    , libraryDependencies += jodaTime
    )
  )

  lazy val http = Project(
    "http"
  , file("http")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-HTTP"
    , libraryDependencies ++= Seq(
        slf4j
      , jodaTime
      , jackson
      , commonsIo
      , commonsCodec
      , junit
      , jsonAssert
      , xmlUnit
      , logback
      )
    )
  ) dependsOn(core)

  lazy val httpApache = Project(
    "http-apache"
  , file("http-apache")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-HTTP-Apache"
    , libraryDependencies ++= Seq(
        httpClient
      , aws
      , junit
      , logback
      )
    )
  ) dependsOn(http)

  lazy val akka = Project(
    "akka"
  , file("akka")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-Akka"
    , libraryDependencies += akkaActor
    )
  ) dependsOn(httpApache)

  lazy val httpAndroid = Project(
    "http-android"
  , file("http-android")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-HTTP-Android"
    , libraryDependencies += androidSDK
    )
  ) dependsOn(http)

  lazy val root = Project(
    "root"
  , file(".")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client"
    , EclipseKeys.skipProject := true
    )
  ) dependsOn(http)
}
