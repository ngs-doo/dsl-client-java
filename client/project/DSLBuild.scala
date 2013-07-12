import sbt._
import Keys._

// ----------------------------------------------------------------------------

object Repositories {
  val NGSNexus     = "NGS Nexus"     at "http://ngs.hr/nexus/content/groups/public/"
  val NGSReleases  = "NGS Releases"  at "http://ngs.hr/nexus/content/repositories/releases/"
  val NGSSnapshots = "NGS Snapshots" at "http://ngs.hr/nexus/content/repositories/snapshots/"
}

// ----------------------------------------------------------------------------

object Resolvers {
  import Repositories._

  val settings = Seq(
    resolvers := Seq(
      NGSNexus
    )
  , externalResolvers <<= resolvers map { r =>
      Resolver.withDefaultResolvers(r, mavenCentral = false)
    }
  )
}

// ----------------------------------------------------------------------------

object Publishing {
  import Repositories._

  val settings = Seq(
    publishTo <<= (version) { version => Some(
      if (version.endsWith("SNAPSHOT")) NGSSnapshots else NGSReleases
    )}
  , publishArtifact in (Compile, packageDoc) := false
  )
}

// ----------------------------------------------------------------------------

object Default {
  // Eclipse plugin
  import com.typesafe.sbteclipse.plugin.EclipsePlugin._

  //Dependency graph plugin
  import net.virtualvoid.sbt.graph.Plugin._

  val settings =
    Defaults.defaultSettings ++
    eclipseSettings ++
    graphSettings ++
    Resolvers.settings ++
    Publishing.settings ++ Seq(
      organization := "com.dslplatform"

    , crossPaths := false
    , autoScalaLibrary := false
    , scalaVersion := "2.10.1"

    , javaHome := sys.env.get("JDK16_HOME").map(file(_))
    , javacOptions := Seq(
        "-deprecation"
      , "-encoding", "UTF-8"
      , "-Xlint:unchecked"
      , "-source", "1.6"
      , "-target", "1.6"
      ) 
    , unmanagedSourceDirectories in Compile <<= (javaSource in Compile)(_ :: Nil)
    , unmanagedSourceDirectories in Test := Nil

    , EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
    , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
    , EclipseKeys.skipParents in ThisBuild := false
    )
}

// ----------------------------------------------------------------------------

object Dependencies {
  // JodaTime
  val jodaTime = "joda-time" % "joda-time" % "2.2"

  // Json serialization
  val jackson = "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"

  // Logging facade
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.5"

  // Apache HttpClient
  val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.2.5"

  // Apache commons
  val commonsIo = "commons-io" % "commons-io" % "2.4"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.8"

  // Amazon Web Services SDK
  val aws = "com.amazonaws" % "aws-java-sdk" % "1.5.0"

  // Akka Actor (contains the Serializer)
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.2.0"
}

// ----------------------------------------------------------------------------

object NGSBuild extends Build {
  import Default._
  import Dependencies._

  lazy val core = Project(
    "core"
  , file("core")
  , settings = Default.settings ++ Seq(
      name := "DSL-Client-Core"
    , version := "0.4.0"
    , libraryDependencies ++= Seq(
        jodaTime
      )
    )
  )

  lazy val http = Project(
    "http"
  , file("http")
  , settings = Default.settings ++ Seq(
      name := "DSL-Client-HTTP"
    , version := "0.4.0"
    , libraryDependencies ++= Seq(
        slf4j
      , jodaTime
      , jackson
      , commonsIo
      , commonsCodec
      , httpClient
      , aws % "provided"
      )
    )
  ) dependsOn(core)

  lazy val akka = Project(
    "akka"
  , file("akka")
  , settings = Default.settings ++ Seq(
      name := "DSL-Client-Akka"
    , version := "0.4.0"
    , libraryDependencies ++= Seq(
        akkaActor % "provided"
      )
    )
  ) dependsOn(http)
}
