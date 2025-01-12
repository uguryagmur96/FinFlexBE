FROM maven:3.9.4-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package


FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/mail-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "mail-service.jar"]