FROM openjdk:8-jre-alpine

ARG dbname
ENV dbname ${dbname}

COPY hsqldb.jar .

EXPOSE 9001

CMD java -classpath hsqldb.jar org.hsqldb.server.Server -database.0 ${dbname} -dbname.0 ${dbname}
