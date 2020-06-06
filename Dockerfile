FROM alpine:3.11.6

RUN apk update && \
    apk add openjdk8

COPY ./target/sweater*.jar /app/app.jar

WORKDIR /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
