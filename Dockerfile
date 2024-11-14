FROM amazoncorretto:21.0.5-alpine3.17

WORKDIR /

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]