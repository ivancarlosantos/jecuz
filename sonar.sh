#!/bin/bash

set -e
sleep 3
mvn clean test jacoco:report
sleep 2
mvn clean verify sonar:sonar -Dsonar.projectKey=jecuz -Dsonar.host.url=http://sonarqube:9030 -Dsonar.login=squ_2385046cb43ff4e10fbb1c3b8860ca9e55cbe878

exit 0

mvn dependency:tree | findstr commons-logging