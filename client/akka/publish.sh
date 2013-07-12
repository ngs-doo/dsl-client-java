#!/bin/bash

echo Will publish the project to the maven server
`dirname $0`/sbt.sh --no-jrebel "$@" clean publish
