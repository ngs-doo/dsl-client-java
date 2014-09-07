#!/bin/bash
cd "`dirname "$0"`"

exec java -Xss2m -Xms2g -Xmx2g \
  -XX:MaxPermSize=512m \
  -XX:ReservedCodeCacheSize=256m \
  -XX:+TieredCompilation \
  -XX:+CMSClassUnloadingEnabled \
  -XX:+UseNUMA \
  -XX:+UseParallelGC \
  -Dscalac.patmat.analysisBudget=off \
  -jar project/strap/gruj_vs_sbt-launch-0.13.x.jar "$@"
