#!/bin/bash
# Convert source XMLs to JSON, and vice versa - conversion scripts xml2json and json2xml need to be on the path
cd source
for f in *.xml
do	
	xml2json $f > ../reference/$f.json
done
cd ../reference
# Convert resulting JSONs to XML again
for g in *.json
do
	json2xml $g > $g.xml
done
