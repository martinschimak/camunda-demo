#!/usr/bin/env bash

mvn clean install
cp ./target/camunda-demo-1.0-SNAPSHOT.war /Users/martin/dev/camunda/distribution/camunda-bpm-ee-tomcat-7.6.4-ee/server/apache-tomcat-8.0.24/webapps/camunda-demo.war
