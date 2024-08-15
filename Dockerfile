FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /usr/src/app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package

FROM quay.io/wildfly/wildfly:33.0.0.Final-2-jdk17

WORKDIR /opt/jboss/wildfly/standalone/deployments/

COPY --from=build /usr/src/app/target/*.war .

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
