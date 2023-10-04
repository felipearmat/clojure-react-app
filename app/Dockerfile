FROM openjdk:8-alpine

ADD target/app-0.0.1-SNAPSHOT-standalone.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
