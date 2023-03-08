FROM maven:3.6.0-jdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn clean install -U

FROM openjdk:8
COPY --from=build /home/app/target/worker-0.0.1-SNAPSHOT.jar /usr/local/lib/worker.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/usr/local/lib/worker.jar"]