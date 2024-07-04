#!/bin/zsh
export $(grep -v "^$" ./docker_env.env | grep -v "^#" | xargs)

keytool -delete -noprompt -alias "${SERVER_HOST}" -keystore  "${JAVA_HOME}/lib/security/cacerts" -storepass changeit
keytool -import -alias "${SERVER_HOST}" -keystore  "${JAVA_HOME}/lib/security/cacerts" -file ./nginx/certs/self-signed.crt -storepass changeit

keytool -delete -noprompt -alias "localhost:28082" -keystore  "${JAVA_HOME}/lib/security/cacerts" -storepass changeit
keytool -import -alias "localhost:28082" -keystore  "${JAVA_HOME}/lib/security/cacerts" -file ./nginx/certs/self-signed.crt -storepass changeit
