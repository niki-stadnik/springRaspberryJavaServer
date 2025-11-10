FROM mcr.microsoft.com/openjdk/jdk:25-ubuntu

COPY target/homebrain.jar homebrain.jar

EXPOSE 1994

ENTRYPOINT ["java", "-jar", "/homebrain.jar"]