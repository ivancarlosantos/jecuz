#!/bin/bash

set -e
sleep 3
mvn clean test jacoco:report
sleep 2
mvn clean verify sonar:sonar -Dsonar.projectKey=jecuz -Dsonar.host.url=http://localhost:9030 -Dsonar.login=squ_b69c553c24b83b91965901daa19f382776d78b4d

exit 0

mvn dependency:tree | findstr commons-logging