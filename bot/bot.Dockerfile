FROM eclipse-temurin:17.0.6_10-jdk
COPY /bot/target/bot-1.0-SNAPSHOT.jar bot-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/bot-1.0-SNAPSHOT.jar"]