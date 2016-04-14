scalacOptions ++= Seq("feature", "-language:_")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.1"
, "org.joda" % "joda-convert" % "1.2"
)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.5")
addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.7.5")
