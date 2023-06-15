FROM openjdk:8-jre-alpine
COPY target/card-api-1.jar app.jar
EXPOSE 8080
ENV TZ="Africa/Nairobi"
RUN date
ENTRYPOINT ["java","-jar","/app.jar"]
RUN apk add --no-cache openssh-client
