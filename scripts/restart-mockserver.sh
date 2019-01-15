#!/bin/bash

. ~/.bash_profile

APP_PATH=$1

cd $APP_PATH
ps -fea | grep "mock-server.*jar" | awk '{print $2}' | xargs kill -9
touch nohup.out
echo "" > nohup.out
JAR=`ls mock-server*.jar`
nohup java -jar $JAR > /dev/null < /dev/null &
