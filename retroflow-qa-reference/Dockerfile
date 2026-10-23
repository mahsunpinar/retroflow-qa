# RetroFlow QA reference: hosted build (one instance per group)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Hosted on a public URL, so the H2 web console stays off. Everything else
# (including the leftover stack trace setting) is deliberately left as it is.
ENV SPRING_H2_CONSOLE_ENABLED=false
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

# Railway sets PORT, locally it falls back to 8080
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
