#!/usr/bin/env sh

cd jffs-admin-backend || cd..
docker build -t chonku/jffs-admin-backend:LATEST . || cd..

cd jffs-admin-ui || cd..
docker build -t chonku/jffs-admin-ui:LATEST . || cd..

