# This file is a template, and might need editing before it works on your project.
FROM maven:3.5-jdk-11 as BUILD

COPY . /usr/src/app
RUN mvn --batch-mode -f /usr/src/app/pom.xml clean install

FROM openjdk:11-jdk
ENV PORT 8080
EXPOSE 8080
COPY --from=BUILD /usr/src/app/target /opt/target
WORKDIR /opt/target

CMD ["/bin/bash", "-c", "find -type f -name '*.jar' | xargs java -jar"]
