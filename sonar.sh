#!/bin/bash

set -e
sleep 3
mvn clean test jacoco:report
sleep 2
mvn clean verify sonar:sonar -Dsonar.projectKey=jecuz -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=ea5cf4a9ace7d631b7150931502915daf8f8055c

exit 0

mvn dependency:tree | findstr commons-logging