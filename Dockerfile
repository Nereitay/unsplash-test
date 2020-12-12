FROM  adoptopenjdk/openjdk11:latest
VOLUME /tmp
EXPOSE 8122
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","app.jar"]