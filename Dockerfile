FROM openjdk:17.0.2
EXPOSE 8081:8080
ARG DB_TYPE
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ARG DB_NAME
ENV DB_TYPE=$DB_TYPE \
    DB_URL=$DB_URL \
    DB_USER=$DB_USER \
    DB_PASSWORD=$DB_PASSWORD \
    DB_NAME=$DB_NAME
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/user-management.jar
ENTRYPOINT ["java", "-jar", "/app/user-management.jar"]