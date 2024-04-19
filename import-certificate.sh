#!/bin/zsh
keytool -import -alias "4.mahotama.com" -keystore  "${JAVA_HOME}/lib/security/cacerts" -file ./nginx/certs/self-signed-3.crt
