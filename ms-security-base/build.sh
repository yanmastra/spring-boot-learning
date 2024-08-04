#!/bin/zsh

DIR=$(pwd)
cd ../ms-persistent-base || exit 0
mvn clean install

cd $DIR || exit 0
mvn clean install -DskipTests
