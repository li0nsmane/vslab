FROM openjdk:13




VOLUME /tmp

EXPOSE 8003

ARG JAR_FILE=target/UserService-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} UserService.jar

ENTRYPOINT ["java","-jar","/UserService.jar"]