FROM openjdk:11-jdk-slim

EXPOSE 8041

VOLUME /tmp

ADD /target/UserService-1.0-SNAPSHOT.jar user-service-1.0.jar

ENTRYPOINT ["java","-jar","user-service-1.0.jar"]