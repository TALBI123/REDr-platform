FROM gradle:8.14-jdk17 As build
WorkDir /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
RUN  chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN apk add --no-cache tzdata

# Créer un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Commande de démarrage avec optimisation mémoire JVM
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]