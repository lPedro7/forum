FROM openjdk:14
CMD ["mkdir", "app"]
WORKDIR app/
COPY target/forum-0.0.1-SNAPSHOT.jar app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app/app.jar"]