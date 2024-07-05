FROM openjdk:17
EXPOSE 8081
ADD build/libs/artemyev-telegram-bot-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar", "/app/app.jar"]