FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ARG JWT_SECRET_KEY
ARG POSTGRES_USERNAME
ARG POSTGRES_PASSWORD
ARG POSTGRES_HOST
ARG CACHE_HOST

ENV JWT_TOKEN_EXPIRATION=86400000
ENV JWT_REFRESH_TOKEN_EXPIRATION=172800000

ENTRYPOINT ["java", "-jar", "app.jar"]