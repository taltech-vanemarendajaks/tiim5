FROM gradle:jdk25-corretto AS builder

WORKDIR /build
COPY . .

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]