#!/bin/bash

PROJECT_DIR=`dirname $(readlink -f $0)`
PARENT=`basename $PROJECT_DIR`

$PROJECT_DIR/../sbt.sh "project $PARENT" "$@"
