#FROM maven:3.5.4-jdk-8-alpine as builder
#
#COPY ./eshop/pom.xml ./pom.xml
#COPY ./eshop/src ./src
#RUN mvn clean package

FROM tomcat:8.0
COPY ./eshop/target/EShop-1.0.0.war /usr/local/tomcat/webapps/
COPY ./eshop/conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

EXPOSE 8080