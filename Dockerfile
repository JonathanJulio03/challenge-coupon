FROM eclipse-temurin:23-jdk AS builder

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre

WORKDIR /app
COPY --from=builder /app/target/coupon-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms1g", "-Xmx2g", "-XX:+UseG1GC", "-jar", "app.jar"]
