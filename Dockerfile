FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

ADD /src /app/src

ADD /pom.xml /app

RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM alpine:3.21.3

RUN apk update

RUN apk add openjdk17-jre

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

WORKDIR /usr/src/app

COPY --from=builder /app/target/*.jar jecuz.jar

EXPOSE 8080

LABEL key="app.jecuz"

ENTRYPOINT ["java", "-jar", "jecuz.jar"]