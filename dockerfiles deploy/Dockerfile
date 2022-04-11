FROM maven:3.8.4-openjdk-11-slim

# Generar archivos necesarios para tomcat
COPY ideas-repo /ideas/repo
RUN cd /ideas/repo && mvn install
COPY ideas-studio /ideas/studio
RUN cd /ideas/studio && mvn package
WORKDIR /ideas/studio

RUN cp -r target /target

FROM tomcat:9.0.62-jre11-temurin-focal

COPY --from=0 /target/*.war ./webapps/ROOT.war
COPY --from=0 /ideas/studio/src/main/webapp/WEB-INF/config/studio-configuration.json /config/ROOT.war

CMD /bin/bash -c "catalina.sh run & sleep 40s && cp /config/studio-configuration.json /usr/local/tomcat/webapps/ROOT/WEB-INF/config/ && while true; do sleep 1s; done"