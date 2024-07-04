#!/usr/bin/env bash

echo "Current release tag is ${RELEASE_TAG}"

cd jffs-ui || exit
#docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:"${CURRENT_TAG}" . || cd ..

#docker image push chonku/jffs-ui:"${CURRENT_TAG}"

#mvn clean package