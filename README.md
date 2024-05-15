# Telegram-bot from GPB-IT-FACTORY "Мини-банк"

__Разработка приложения "Мини-банк", который будет состоять из трёх компонентов:__

- frontend (telegram-bot на java/kotlin);
- middle-слой (java/kotlin-сервис);
- backend (java/kotlin-сервис).

![Схема1](src/main/resources/static/img/image1.png)

### Диаграмма взаимодействия сервисов

![Схема2](src/main/resources/static/img/image2.png)

### Локальный запуск 
1. __Склонируйте репозиторий:__
  ```git clone https://github.com/gpb-it-factory/artemyev-telegram-bot.git ```
2. __Получите API-токен для Telegam-бота__, следуйте инструкциям на: <https://core.telegram.org/bots/tutorial#obtain-your-bot-token>
3. __Убедитесь, что у вас установлены__: Java 17, Gradle 8.7
4. __Сборка проекта__: в терминале выполните команду:  ``` gradle bootJar ``` Это соберет исполняемый JAR-файл в папке `build/libs`
5. __Запуск приложения__: ``` java -jar build/libs/artemyev-telegam-bot-0.0.1-SNAPSHOT.jar```

### Дальнейшие шаги по использованию сервиса...
...
### Контакты

- **Telegram:** [@nicknickart](https://t.me/nicknickart)   :blush:

