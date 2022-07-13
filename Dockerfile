FROM defradigital/java:latest-jre

ARG BUILD_VERSION

USER root

RUN mkdir -p /usr/src/reach-notify
WORKDIR /usr/src/reach-notify

COPY ./target/reach-notify-${BUILD_VERSION}.jar /usr/src/reach-notify/reach-notify.jar
COPY ./target/agent/applicationinsights-agent.jar /usr/src/reach-notify/applicationinsights-agent.jar
COPY ./target/classes/applicationinsights.json /usr/src/reach-notify/applicationinsights.json

RUN chown jreuser /usr/src/reach-notify
USER jreuser

EXPOSE 8080

CMD java -javaagent:/usr/src/reach-notify/applicationinsights-agent.jar \
-Xmx${JAVA_MX:-512M} -Xms${JAVA_MS:-256M} -jar reach-notify.jar
