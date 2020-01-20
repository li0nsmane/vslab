FROM openjdk:13

VOLUME /tmp

EXPOSE 8010

ARG JAR_FILE=target/HystrixDashboard-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} HystrixDashboard.jar

ENTRYPOINT ["java","-jar","/HystrixDashboard.jar"]