#!/bin/bash

echo Will publish the project to a local ivy repository
`dirname $0`/sbt.sh --no-jrebel "$@" clean publish-local
