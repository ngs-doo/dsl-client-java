#!/bin/bash
cd "`dirname "$0"`"

java -Xss2m -Xms2g -Xmx2g \
  -XX:MaxPermSize=512m \
  -XX:ReservedCodeCacheSize=256m \
  -XX:+TieredCompilation \
  -XX:+CMSClassUnloadingEnabled \
  -XX:+UseNUMA \
  -XX:+UseParallelGC \
  -Dscalac.patmat.analysisBudget=off \
  -Dinput.encoding=Cp1252 \
  -jar project/strap/gruj_vs_sbt-launch-0.13.x.jar %*
