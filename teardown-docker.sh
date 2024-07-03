#!/bin/zsh
export $(grep -v "^$" ./docker_env.env | grep -v "^#" | xargs)

source docker_env.sh
docker-compose down
