version in ThisBuild := "0.4.14-SNAPSHOT"

val NGSNexus     = "NGS Nexus"     at "http://ngs.hr/nexus/content/groups/public/"
val NGSReleases  = "NGS Releases"  at "http://ngs.hr/nexus/content/repositories/releases/"
val NGSSnapshots = "NGS Snapshots" at "http://ngs.hr/nexus/content/repositories/snapshots/"

resolvers in ThisBuild := Seq(NGSNexus, NGSSnapshots)

externalResolvers in ThisBuild := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

publishTo in ThisBuild := Some(
      if (version.value endsWith "SNAPSHOT") NGSSnapshots else NGSReleases
    )

credentials in ThisBuild += Credentials(Path.userHome / ".config" / "dsl-client-java" / "nexus.config")
