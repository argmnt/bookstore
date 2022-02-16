FROM adoptopenjdk:11-jre-hotspot

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /tmp

ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} /opt/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]