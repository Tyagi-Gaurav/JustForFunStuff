FROM chonku/java:v0.1.8

ARG DB_USER
ENV DB_USER=$DB_USER
ARG DB_PWD
ENV DB_PWD=$DB_PWD
ARG DB_NAME
ENV DB_NAME=$DB_NAME
ARG DB_HOST
ENV DB_HOST=$DB_HOST
ARG DB_SCHEME
ENV DB_SCHEME=$DB_SCHEME

ADD target/jffs-backend-DEV-SNAPSHOT-jar-with-dependencies.jar /data/application.jar

COPY ./startup.sh /data/startup.sh

CMD ["/bin/bash", "/data/startup.sh"]

EXPOSE 8080
EXPOSE 8081
