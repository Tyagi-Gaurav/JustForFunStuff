#!/usr/bin/env bash

CURRENT_TAG=$(git describe --abbrev=0 --tags)

echo "Current release tag is $CURRENT_TAG"

cd jffs-ui || exit
docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:"${CURRENT_TAG}" . || cd ..

docker image push chonku/jffs-ui:"${CURRENT_TAG}"

#mvn clean package