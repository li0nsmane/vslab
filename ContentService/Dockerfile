FROM openjdk:13

VOLUME /tmp

EXPOSE 8004

ARG JAR_FILE=target/ContentService-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} ContentService.jar

ENTRYPOINT ["java","-jar","/ContentService.jar"]