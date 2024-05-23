FROM maven:3.9.6-amazoncorretto-21-debian-bookworm AS builder

WORKDIR /usr/src/mymaven
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests


FROM amazoncorretto:21-alpine-jdk

WORKDIR /app
COPY --from=builder /usr/src/mymaven/target/*-jar-with-dependencies.jar ./app.jar
COPY src/main/resources/logback.xml .
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
