Простой LongPolling Telegram-бот на Java для мониторинга https://bash.im/, может размещаться на Heroku. Структура
страниц сайта не меняется, поэтому работа ведется просто через GET-запросы и обработку ответов с помощью RestTemplate(
втч XML).

- Бот отслеживает появление новых цитат и комиксов, делает рассылку подписчикам
- Данные о id подписчиков и ссылки на страницы комиксов хранятся в базе данных
- Данные по bot.name и bot.token можно сохранить в папке ресурсов в private.properties (опционально), либо в системных
  переменных
- Настройки бота вынесены в application.properties 

Стек технологий:
- Java 11
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Gradle
- JUnit
- Telegram Bot API: https://core.telegram.org/bots/api
- Tesera API: https://api.tesera.ru/help/index.html

