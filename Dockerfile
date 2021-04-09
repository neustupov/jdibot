FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/javadevinterviewbot-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c "touch /app.jar"
EXPOSE 5000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]