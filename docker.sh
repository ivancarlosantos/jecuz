#!/bin/bash

sleep 2
echo 'create network jecuz_app'
sleep 2

docker network create --driver bridge jecuz_app
sleep 3
curl -fsSL https://raw.githubusercontent.com/ivancarlosantos/installer/refs/heads/master/progress_bar_spinner.sh | bash
sleep 3

echo "Instalar Postgres"
docker run --name='jecuz_db' --network='jecuz_app' -d -p 5432:5432 -e POSTGRES_PASSWORD='12345' -e POSTGRES_USER='postgres' -e POSTGRES_DB='jecuz_db' postgres:15
sleep 5
curl -fsSL https://raw.githubusercontent.com/ivancarlosantos/installer/refs/heads/master/progress_bar_spinner.sh | bash

echo "Instalar PGAdmin SGBD"
docker run --name='pgadmin' --network='jecuz_app' -d -p 15432:80 -e PGADMIN_DEFAULT_EMAIL='jecuz@jecuz.com' -e PGADMIN_DEFAULT_PASSWORD='12345' dpage/pgadmin4:latest
sleep 5

echo 'Instalar RabbitMQ'
curl -fsSL https://raw.githubusercontent.com/ivancarlosantos/installer/refs/heads/master/progress_bar_spinner.sh | bash
sleep 3
docker run --name='rabbitmq' --hostname rabbit-host --network='jecuz_app' -d -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management-alpine
sleep 3