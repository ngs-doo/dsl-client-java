import sbt._
import Keys._

// Eclipse plugin
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

// Dependency graph plugin
import net.virtualvoid.sbt.graph.Plugin._

// Assembly plugin
import sbtassembly.Plugin._

// ----------------------------------------------------------------------------

trait Default {
  val defaultSettings =
    Defaults.defaultSettings ++
    eclipseSettings ++
    assemblySettings ++
    graphSettings ++ Seq(
      version := "0.4.14-SNAPSHOT"
    , organization := "com.dslplatform"

    , crossPaths := false
    , autoScalaLibrary := false
    , scalaVersion := "2.10.3"

    , javaHome := sys.env.get("JDK16_HOME").map(file(_))
    , javacOptions := Seq(
        "-deprecation"
      , "-encoding", "UTF-8"
      , "-Xlint:unchecked"
      , "-source", "1.6"
      , "-target", "1.6"
      )
    , javacOptions in doc := Seq(
        "-source", "1.6"
      )
    , unmanagedSourceDirectories in Compile := Seq((javaSource in Compile).value)
    , unmanagedSourceDirectories in Test := Nil
    , EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
    )
}

// ----------------------------------------------------------------------------

trait Dependencies {
  // JodaTime
  val jodaTime = "joda-time" % "joda-time" % "2.3"

  // Json serialization
  val jackson = "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.1"

  // Logging facade
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.5"

  // Apache HttpClient
  val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.3.2"

  // Apache commons
  val commonsIo = "commons-io" % "commons-io" % "2.4"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.9"

  // Amazon Web Services SDK (S3 type)
  val aws = "com.amazonaws" % "aws-java-sdk" % "1.7.1"

  // Akka Actor (contains the Serializer)
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.2.3"

  // Android SDK
  val androidSDK = "com.google.android" % "android" % "4.1.1.4"

  // Testing
  val jUnit = "junit" % "junit" % "4.11"
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
      , aws % "provided"
      , jUnit % "test"
      )
    , unmanagedSourceDirectories in Test += (javaSource in Test).value
    , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
    )
  ) dependsOn(http)

  lazy val akka = Project(
    "akka"
  , file("akka")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client-Akka"
    , libraryDependencies += akkaActor % "provided"
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

  import AssemblyKeys._

  lazy val root = Project(
    "root"
  , file(".")
  , settings = defaultSettings ++ Seq(
      name := "DSL-Client"
    , mainClass in assembly := Some("com.dslplatform.client.Bootstrap")
    , jarName in assembly := "dsl-client-%s.jar" format version.value
    , excludedJars in assembly := (fullClasspath in assembly).value.filter(_.data.getName endsWith ".jar")
    , EclipseKeys.skipProject := true
/*
    , assembleArtifact in packageScala := false
    , artifact in (Compile, assembly) ~= (_.copy(`classifier` = Some("assembly")))
    , test in assembly := {}
    ) ++ addArtifact(artifact in (Compile, assembly), assembly)
*/
    )
  ) dependsOn(http)
}
