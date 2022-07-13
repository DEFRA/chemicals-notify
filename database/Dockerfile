FROM sndeuxfesacr001.azurecr.io/reach-liquibase-base:1.0

ADD src/main/resources/changelog /liquibase/changelog
RUN echo "changeLogFile: db.changelog-master.xml" >> /liquibase/liquibase.docker.properties
