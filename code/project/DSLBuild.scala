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
      scalaVersion := "2.11.2"
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
    )
}

// ----------------------------------------------------------------------------

trait Dependencies {
  // JodaTime
  val jodaTime = "joda-time" % "joda-time" % "2.4"

  // Json serialization
  val jackson = "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.1.3"

  // Logging facade
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"

  // Amazon Web Services SDK (S3 type)
  val aws = "com.amazonaws" % "aws-java-sdk" % "1.8.7"

  // Akka Actor (contains the Serializer)
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.3.4"

  // Android SDK
  val androidSDK = "com.google.android" % "android" % "4.1.1.4"

  // Testing
  val junit = "junit" % "junit" % "4.11"
  val jsonAssert = "org.skyscreamer" % "jsonassert" % "1.2.3"
  val xmlUnit = "xmlunit" % "xmlunit" % "1.5"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
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
      //, jodaTime
      , akkaActor % "provided"
      , aws % "provided"
      , androidSDK % "provided"
      , jackson
      , junit % "test"
      , jsonAssert % "test"
      , xmlUnit % "test"
      , logback % "test"
      )
    )
  ) dependsOn(core)

  def aggregatedCompile =  ScopeFilter(inProjects(core, http), inConfigurations(Compile))

  def aggregatedTest = ScopeFilter(inProjects(core, http), inConfigurations(Test))

  def aggregatedModules = Seq(
    sources in Compile                        := sources.all(aggregatedCompile).value.flatten,
    unmanagedSources in Compile               := unmanagedSources.all(aggregatedCompile).value.flatten,
    unmanagedSourceDirectories in Compile     := unmanagedSourceDirectories.all(aggregatedCompile).value.flatten,
    unmanagedResourceDirectories in Compile   := unmanagedResourceDirectories.all(aggregatedCompile).value.flatten,
    sources in Test                           := sources.all(aggregatedTest).value.flatten,
    unmanagedSources in Test                  := unmanagedSources.all(aggregatedTest).value.flatten,
    unmanagedSourceDirectories in Test        := unmanagedSourceDirectories.all(aggregatedTest).value.flatten,
    unmanagedResourceDirectories in Test      := unmanagedResourceDirectories.all(aggregatedTest).value.flatten,
    libraryDependencies                       := libraryDependencies.all(aggregatedCompile).value.flatten
  )

  lazy val root = (project in file(".")) settings ((defaultSettings ++ aggregatedModules): _*)
}
