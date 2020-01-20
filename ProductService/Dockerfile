FROM openjdk:13


VOLUME /tmp

EXPOSE 8001

ARG JAR_FILE=target/ProductService-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} ProductService.jar

ENTRYPOINT ["java","-jar","/ProductService.jar"]