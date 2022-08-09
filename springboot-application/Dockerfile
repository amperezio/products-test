FROM --platform=linux/amd64 openjdk:11
VOLUME /tmp
COPY target/Spring-SOAINT-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
