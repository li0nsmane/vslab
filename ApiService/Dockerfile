FROM openjdk:13

VOLUME /tmp

EXPOSE 8002

ARG JAR_FILE=target/CategoryService-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} CategoryService.jar

ENTRYPOINT ["java","-jar","/CategoryService.jar"]