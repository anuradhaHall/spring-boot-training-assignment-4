FROM openjdk:11
VOLUME /temp
COPY target/*.jar assignment4-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/assignment4-0.0.1-SNAPSHOT.jar"]