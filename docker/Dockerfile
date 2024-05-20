FROM eclipse-temurin:21-jdk-alpine

RUN adduser apprunner -D

RUN mkdir /app && chown apprunner /app

USER apprunner

COPY ./web-api/target/architect-burgers-webapi-0.0.1-SNAPSHOT.jar /app/

ENV archburgers.datasource.driverClass=org.postgresql.Driver
ENV archburgers.datasource.dbUrl=jdbc:postgresql://pgserver/archburgers?autoReconnect=true
ENV archburgers.datasource.dbUser=myDbUser
ENV archburgers.datasource.dbPass=muDbPass

EXPOSE 8090

CMD java -jar /app/architect-burgers-webapi-0.0.1-SNAPSHOT.jar
