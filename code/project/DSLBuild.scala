import sbt._
import Keys._

// Eclipse plugin
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

// Dependency graph plugin
import net.virtualvoid.sbt.graph.Plugin._

// Assembly plugin
import sbtassembly.Plugin._
import AssemblyKeys._

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
    resolvers := Seq(NGSNexus, NGSSnapshots)
  , externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)
  )
}

// ----------------------------------------------------------------------------

object Publishing {
  import Repositories._

  val settings = Seq(
    publishTo := Some(
      if (version.value endsWith "SNAPSHOT") NGSSnapshots else NGSReleases
    )
  , credentials += Credentials(Path.userHome / ".config" / "dsl-client-java" / "nexus.config")
  , publishArtifact in (Compile, packageDoc) := false
  )
}

// ----------------------------------------------------------------------------

object Default {
  val settings =
    Defaults.defaultSettings ++
    eclipseSettings ++
    assemblySettings ++
    graphSettings ++
    Resolvers.settings ++
    Publishing.settings ++ Seq(
      organization := "com.dslplatform"

    , crossPaths := false
    , autoScalaLibrary := false
    , scalaVersion := "2.10.2"

    , javaHome := sys.env.get("JDK16_HOME").map(file(_))
    , javacOptions := Seq(
        "-deprecation"
      , "-encoding", "UTF-8"
      , "-Xlint:unchecked"
      , "-source", "1.6"
      , "-target", "1.6"
      )
    , unmanagedSourceDirectories in Compile := (javaSource in Compile).value :: Nil
    , unmanagedSourceDirectories in Test := Nil

    , EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
    , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
    )
}

// ----------------------------------------------------------------------------

object Dependencies {
  // JodaTime
  val jodaTime = "joda-time" % "joda-time" % "2.3"

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
  val aws = "com.amazonaws" % "aws-java-sdk" % "1.5.5"

  // Akka Actor (contains the Serializer)
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.2.1"
}

// ----------------------------------------------------------------------------

object NGSBuild extends Build {
  import Default._
  import Dependencies._

  val libVersion = "0.4.8"

  lazy val core = Project(
    "core"
  , file("core")
  , settings = Default.settings ++ Seq(
      name := "DSL-Client-Core"
    , version := libVersion
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
    , version := libVersion
    , libraryDependencies ++= Seq(
        slf4j
      , jodaTime
      , jackson
      , commonsIo
      , commonsCodec
      , httpClient
      , aws % "provided"
      )
    , assembleArtifact in packageScala := false
    , artifact in (Compile, assembly) ~= (_.copy(`classifier` = Some("assembly")))
    , test in assembly := {}
    , mainClass in assembly := Some("com.dslplatform.client.Bootstrap")
    , jarName in assembly := "dsl-client-%s.jar" format libVersion
    , excludedJars in assembly :=
        (fullClasspath in assembly).value.filter(_.data.getName endsWith ".jar")
    ) ++ addArtifact(artifact in (Compile, assembly), assembly)
  ) dependsOn(core)

  lazy val akka = Project(
    "akka"
  , file("akka")
  , settings = Default.settings ++ Seq(
      name := "DSL-Client-Akka"
    , version := libVersion
    , libraryDependencies ++= Seq(
        akkaActor % "provided"
      )
    )
  ) dependsOn(http)
}
