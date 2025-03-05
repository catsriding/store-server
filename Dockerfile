# Build stage
FROM gradle:8.12.1-jdk23 as build
WORKDIR /app
COPY . .

RUN gradle clean build -x test --no-daemon

# Package stage
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /app/api/build/libs/*.jar /app/application.jar

EXPOSE 8080
ENV SECURITY_JWT_SECRET=default_secret \
    STORAGE_DATABASE_READER_URL=localhost:3306/sandbox \
    STORAGE_DATABASE_READER_USERNAME=root \
    STORAGE_DATABASE_READER_PASSWORD=password123 \
    STORAGE_DATABASE_WRITER_URL=localhost:3306/sandbox \
    STORAGE_DATABASE_WRITER_USERNAME=root \
    STORAGE_DATABASE_WRITER_PASSWORD=password123

ENTRYPOINT ["java", "-jar", "application.jar", "--security.jwt.secret=${SECURITY_JWT_SECRET}", "--storage.database.reader.url=${STORAGE_DATABASE_READER_URL}", "--storage.database.reader.username=${STORAGE_DATABASE_READER_USERNAME}", "--storage.database.reader.password=${STORAGE_DATABASE_READER_PASSWORD}", "--storage.database.writer.url=${STORAGE_DATABASE_WRITER_URL}", "--storage.database.writer.username=${STORAGE_DATABASE_WRITER_USERNAME}", "--storage.database.writer.password=${STORAGE_DATABASE_WRITER_PASSWORD}"]
CMD ["--spring.profiles.active=test"]