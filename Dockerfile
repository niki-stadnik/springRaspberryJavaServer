FROM mcr.microsoft.com/openjdk/jdk:25-ubuntu

COPY target/homebrain.jar homebrain.jar

EXPOSE 1994

ENTRYPOINT ["java", "-jar", "/homebrain.jar"]


#mvn package
#click the run icon here, Edit 'Dockerfile'... , change image tag to next number
#docker tag homebrain:11.00 nikistadnik/homebrain:latest
#docker push nikistadnik/homebrain:latest