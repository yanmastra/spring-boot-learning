#!/bin/zsh
DIR=$(pwd)

export $(grep -v "^$" docker_env.env | grep -v "^#" | xargs)

export DB_LIST="$(cat databases-list.txt)"
export KEYCLOAK_DB_NAME=db_authentication

docker compose -f ./docker-compose.yml up postgres -d

envsubst  < docker/keycloak/Dockerfile.template > docker/keycloak/Dockerfile

docker compose -f ./docker-compose.yml up keycloak -d
sleep 3
host="\$host"
request_uri="\$request_uri"
remote_addr="\$remote_addr"
scheme="\$scheme"
envsubst  < ./nginx/nginx.conf.template > ./nginx/nginx.conf
docker compose -f ./docker-compose.yml up nginx-proxy -d
sleep 3