#!/bin/bash

set -e

MEM_FLAGS="-Xms512m -Xmx1024m"

java $MEM_FLAGS -javaagent:/usr/local/newrelic/newrelic.jar -jar /data/application.jar