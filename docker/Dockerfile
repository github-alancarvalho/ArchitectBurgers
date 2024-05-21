FROM eclipse-temurin:21-jdk-alpine

RUN adduser apprunner -D

RUN mkdir /app

COPY ./temp_libs/* /app/
COPY ./entrypoint.sh /app/

RUN chown -R apprunner /app
RUN chmod a+x /app/entrypoint.sh

USER apprunner

ENV archburgers.datasource.driverClass=org.postgresql.Driver
ENV archburgers.datasource.dbUrl=jdbc:postgresql://pgserver/archburgers?autoReconnect=true
ENV archburgers.datasource.dbUser=myDbUser
ENV archburgers.datasource.dbPass=muDbPass

EXPOSE 8090

CMD /app/entrypoint.sh
