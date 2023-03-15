FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
