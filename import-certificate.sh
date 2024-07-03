#!/bin/zsh
export $(grep -v "^$" ./docker_env.env | grep -v "^#" | xargs)
keytool -import -alias "${SERVER_HOST}" -keystore  "${JAVA_HOME}/lib/security/cacerts" -file ./nginx/certs/self-signed.crt
keytool -import -alias "localhost:28082" -keystore  "${JAVA_HOME}/lib/security/cacerts" -file ./nginx/certs/self-signed.crt
