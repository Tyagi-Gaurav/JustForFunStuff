#!/bin/bash

set -e

MEM_FLAGS="-Xms512m -Xmx1024m"

java $MEM_FLAGS -jar /data/application.jar