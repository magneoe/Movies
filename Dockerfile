FROM maven:3.6.0-jdk-8

COPY ./ /tmp/


WORKDIR /tmp/

RUN mvn package -DforkCount=0

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "target/movies-0.0.1-SNAPSHOT.jar"]