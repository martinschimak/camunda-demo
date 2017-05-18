#!/usr/bin/env bash

cd /Users/martin/dev/camunda/distribution/camunda-bpm-ee-tomcat-7.6.4-ee/server/apache-tomcat-8.0.24/bin/
./shutdown.sh
sleep 3
rm -rf /Users/martin/dev/camunda/distribution/camunda-bpm-ee-tomcat-7.6.4-ee/server/apache-tomcat-8.0.24/webapps/camunda-demo
rm -rf /Users/martin/dev/camunda/distribution/camunda-bpm-ee-tomcat-7.6.4-ee/server/apache-tomcat-8.0.24/webapps/camunda-demo.war
rm -rf /Users/martin/dev/camunda/distribution/camunda-bpm-ee-tomcat-7.6.4-ee/server/apache-tomcat-8.0.24/work/Catalina/localhost
cd -
