# #FROM maven:3.6-jdk-8 as builder
# FROM maven:3.6-ibmjava-8-alpine as builder
# ARG sname
# COPY ./${sname}/pom.xml ./pom.xml
# # significally improves buildtime if service code changed
# RUN mvn verify clean --fail-never
# COPY ./${sname}/src ./src
# RUN mvn package -DskipTests

# FROM tomcat:jdk8
# ARG sname
# ARG sPort
# COPY --from=builder /target/${sname}-1.0.0.war /usr/local/tomcat/webapps/
# COPY ./tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
# RUN echo ${sname}:${sPort}
# EXPOSE ${sPort}

FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG sname
ARG sPort
COPY ./${sname}/target/${sname}-1.0.0.war service.war
ENTRYPOINT ["java","-jar","/service.war"]
EXPOSE ${sPort}