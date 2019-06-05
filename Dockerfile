FROM openjdk:8-jdk-alpine
EXPOSE 9090
ADD target/challenge-1.0-SNAPSHOT.jar challenge.jar
ENTRYPOINT ["java" , "-jar" , "challenge.jar"]