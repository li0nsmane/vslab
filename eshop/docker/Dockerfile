FROM java:8-jdk-alpine

FROM tomcat:8.0
COPY ./target/EShop-1.0.0.war /usr/local/tomcat/webapps/
COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

EXPOSE 8080
