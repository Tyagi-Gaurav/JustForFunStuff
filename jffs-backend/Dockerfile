FROM chonku/java:v0.1.8

ADD target/jffs-backend-DEV-SNAPSHOT-jar-with-dependencies.jar /data/application.jar

COPY ./startup.sh /data/startup.sh

CMD ["/bin/bash", "/data/startup.sh"]

EXPOSE 8080
EXPOSE 8081
