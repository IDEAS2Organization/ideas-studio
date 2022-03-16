FROM maven:3.8.4-openjdk-11-slim

COPY ideas-repo /ideas/repo
RUN cd /ideas/repo && mvn install
COPY ideas-studio /ideas/studio
RUN cd /ideas/studio && mvn package

RUN rm -r /ideas/repo

WORKDIR /ideas/studio
CMD mvn spring-boot:run
