# Stage 1 - Build
FROM adoptium/temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTestsgit

# Stage 2 - Run
FROM adoptium/temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/expansetracker-0.0.1-SNAPSHOT.jar moneymanager.v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "moneymanager.v1.0.jar"]
