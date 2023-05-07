FROM eclipse-temurin:17.0.6_10-jdk
COPY /scrapper/target/scrapper-1.0-SNAPSHOT.jar scrapper-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "--enable-preview", "-jar","/scrapper-1.0-SNAPSHOT.jar"]