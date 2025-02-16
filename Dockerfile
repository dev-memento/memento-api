FROM amd64/amazoncorretto:21
WORKDIR /app
COPY ./build/libs/memento-0.0.1-SNAPSHOT.jar  /app/memento.jar
CMD ["java", "-Duser.timezone=UTC", "-Dspring.profiles.active=dev", "-jar", "memento.jar"]

