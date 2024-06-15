#!/bin/zsh
DIR=$(pwd)
export $(grep -v "^$" ../docker_env.env | grep -v "^#" | xargs)

cd ../ms-security-base
mvn clean install -DskipTests

cd $DIR
export PROXY_ADDRESS_FORWARDING=true
#export _JAVA_OPTIONS=
mvn clean spring-boot:run -Djavax.net.ssl.trustStore=../docker/keycloak/server.keystore -Djavax.net.ssl.trustStorePassword=t3ruT4m4
