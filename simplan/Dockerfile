# Docker file

# jdk 17 image start
FROM openjdk:17-jdk

# 인자 정리 -jar
ARG JAR_FILE=build/libs/*.jar

# jar file copy
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
