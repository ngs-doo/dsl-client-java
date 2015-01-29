#!/bin/bash
cd "`dirname "$0"`"

# -XX:MaxPermSize JVM option is not required if you are running on JVM 8+

exec java -Xss2m -Xms2g -Xmx2g \
  -XX:ReservedCodeCacheSize=256m \
  -XX:+TieredCompilation \
  -XX:+CMSClassUnloadingEnabled \
  -XX:+UseNUMA \
  -XX:+UseParallelGC \
  -Dscalac.patmat.analysisBudget=off \
  -XX:MaxPermSize=256m \
  -jar project/strap/gruj_vs_sbt-launch-0.13.x.jar "$@"
