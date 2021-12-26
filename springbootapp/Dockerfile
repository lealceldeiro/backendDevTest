FROM maven:3.8.2-openjdk-11 as BUILD_STAGE
COPY pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline

COPY src ./src
RUN mvn -B clean package

FROM openjdk:11-jre-slim as RUN_STAGE
# https://spring.io/guides/gs/spring-boot-docker/
RUN mkdir -p "/springbootapp"
RUN adduser --system --group springbootapp
RUN chown -R springbootapp /springbootapp

COPY --from=BUILD_STAGE target/springbootapp-0.0.1-SNAPSHOT.jar ../springbootapp/springbootapp.jar

LABEL author="Asiel Leal Celdeiro"
EXPOSE 5000

USER springbootapp:springbootapp

ENTRYPOINT java -jar /springbootapp/springbootapp.jar
