FROM openjdk:13

VOLUME /tmp

EXPOSE 8761

ARG JAR_FILE=target/EurekaServer-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} EurekaServer.jar

ENTRYPOINT ["java","-jar","/EurekaServer.jar"]