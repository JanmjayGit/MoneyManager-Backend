FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/expansetracker-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT["java", "-jar", "moneymanager-v1.0.jar"]