#!/bin/zsh
DIR=$(pwd)

export $(grep -v "^$" docker_env.env | grep -v "^#" | xargs)

export DB_LIST="$(cat databases-list.txt)"
export KEYCLOAK_DB_NAME=db_authentication

docker compose -f ./docker-compose.yml up postgres -d

if [[ -f docker/keycloak/Dockerfile ]]; then
  echo "docker/keycloak/Dockerfile exists!"
else
  envsubst  < docker/keycloak/Dockerfile.template > docker/keycloak/Dockerfile
fi

KC_IMAGE=$(docker images | grep spring-boot-learning-keycloak | grep 1.0.0)
if [[ -z "$KC_IMAGE" ]]; then
  docker build --tag spring-boot-learning-keycloak:1.0.0 -f docker/keycloak/Dockerfile .
else
  echo "$KC_IMAGE already exists!"
fi

docker compose -f ./docker-compose.yml up keycloak -d
sleep 3

export host="\$host"
export request_uri="\$request_uri"
export remote_addr="\$remote_addr"
export scheme="\$scheme"
echo "$host, $request_uri, $remote_addr, $scheme"
envsubst  < ./nginx/nginx.conf.template > ./nginx/nginx.conf
docker compose -f ./docker-compose.yml up nginx-proxy -d
sleep 3
open ${KEYCLOAK_BASE_URL}