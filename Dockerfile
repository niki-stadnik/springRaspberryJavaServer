FROM openjdk:25

COPY target/homebrain.jar homebrain.jar

EXPOSE 1994

ENTRYPOINT ["java", "-jar", "/homebrain.jar"]