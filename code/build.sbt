version in ThisBuild            := "0.4.14-SNAPSHOT"

organizationName in ThisBuild   := "Nova generacija softvera"

organization in ThisBuild       := "com.dslplatform"

externalResolvers in ThisBuild  := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

publishTo in ThisBuild          := Some(if (version.value endsWith "SNAPSHOT") Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging)

credentials in ThisBuild        += Credentials(Path.userHome / ".config" / "dsl-client-java" / "element.sonatype")

licenses in ThisBuild           += ("BSD-style", url("http://www.opensource.org/licenses/bsd-license.php"))

startYear in ThisBuild          := Some(2009)

scmInfo in ThisBuild            := Some(ScmInfo(url("https://github.com/ngs-doo/dsl-client-java.git"), "scm:git:https://github.com/ngs-doo/dsl-client-java.git"))

pomExtra  in ThisBuild          ~= (_ ++ {Developers.toXml})

publishMavenStyle in ThisBuild  := true

pomIncludeRepository in ThisBuild  := { _ => false }

homepage  in ThisBuild          := Some(url("https://dsl-platform.com"))

packagedArtifacts               := Map.empty
