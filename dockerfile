FROM openjdk:13-alpine3.10
ADD /target/movies-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090:8090
ENTRYPOINT ["java","-jar", "app.jar"]