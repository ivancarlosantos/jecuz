#!/bin/bash

sleep 2

echo "Build Test Image"
sleep 2

docker build -t image-release-test .
sleep 2

echo "Build test Successfully"
sleep 2

echo "start application Jecuz Limpeza"
sleep 2
docker run --name='build-local-test' --network='jecuz_app' -d -p 8080:8080 -e DB_URL='jdbc:postgresql://jecuz_db:5432/jecuz_db' -e DB_USERNAME='postgres' -e DB_PASSWORD='12345' image-release-test