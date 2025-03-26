# Build Spring Boot
FROM openjdk:23-jdk AS j-build

WORKDIR /src

COPY dishcoveryServer/.mvn .mvn
COPY dishcoveryServer/src src
COPY dishcoveryServer/mvnw .
COPY dishcoveryServer/pom.xml .


RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true


# Copy the JAR file over to the final container
FROM openjdk:23-jdk 

WORKDIR /app

COPY --from=j-build /src/target/dishcoveryServer-0.0.1-SNAPSHOT.jar app.jar


ENV PORT=8080


EXPOSE ${PORT}

SHELL [ "/bin/sh", "-c" ]
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar

