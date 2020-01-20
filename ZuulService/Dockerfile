FROM openjdk:13




VOLUME /tmp

EXPOSE 8020

ARG JAR_FILE=target/ZuulService-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} ZuulService.jar

ENTRYPOINT ["java","-jar","/ZuulService.jar"]