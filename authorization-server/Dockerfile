FROM openjdk:13


VOLUME /tmp

EXPOSE 8090

ARG JAR_FILE=target/authorization-server-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} authorization-server.jar

ENTRYPOINT ["java","-jar","/authorization-server.jar"]