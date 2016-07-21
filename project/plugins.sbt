scalaVersion := "2.10.6"
scalacOptions ++= Seq("feature", "-language:_")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.4"
, "org.joda" % "joda-convert" % "1.2"
)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
