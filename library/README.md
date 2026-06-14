# Библиотека

Приложение для управления библиотекой, использующее Spring Data JDBC и PostgreSQL.

- [Библиотека](#библиотека)
  - [Стек технологий](#стек-технологий)
  - [Требования](#требования)
  - [Настройка](#настройка)
    - [1. Запуск PostgreSQL](#1-запуск-postgresql)
    - [2. Сборка и запуск исполняемого JAR](#2-сборка-и-запуск-исполняемого-jar)
  - [Тестирование](#тестирование)
  - [Схема БД](#схема-бд)
  - [Остановка базы данных](#остановка-базы-данных)

![](<screenshots/CleanShot 2026-06-14 at 23.24.28.gif>)

![](<screenshots/CleanShot 2026-06-14 at 23.32.42@2x.png>)

![](<screenshots/CleanShot 2026-06-14 at 23.34.05@2x.png>)

![](<screenshots/CleanShot 2026-06-14 at 23.38.09@2x.png>)

## Стек технологий

| Компонент       | Технология                        |
| --------------- | --------------------------------- |
| Фреймворк       | Spring Boot 4.0.6                 |
| Доступ к данным | Spring Data JDBC                  |
| Database        | PostgreSQL 18                     |
| Пул соединений  | HikariCP (добавлен автоматически) |
| Тестирование    | JUnit 5, Testcontainers           |
| Сборка          | Maven                             |

## Требования

- Java 21+
- Maven 3.9+
- Docker (для PostgreSQL и тестов)

## Настройка

### 1. Запуск PostgreSQL

Из директории `/library`:

```bash
docker compose up -d
```

Эта команда запустит контейнер PostgreSQL 18 на `localhost:5432` с базой данных `library`, пользователем `library`, паролем `library`.

### 2. Сборка и запуск исполняемого JAR

Приложение упаковывается в исполняемый "fat JAR" (с вложенными зависимостями, объединенными `spring.factories`, автоматически обнаруживаемой точкой входа).

Из корневой директории проекта:

```bash
mvn clean package -pl library -am
```

Для запуска используйте следующую команду:

```bash
java -jar library/target/library-*.jar
```

При первом запуске Spring Boot автоматически:
- Создает таблицы с помощью `schema.sql`
- Заполняет их начальными данными из `data.sql`

Если нужно включить логгирование, используйте следующую команду:

```bash
java -jar library/target/library-*.jar --spring.profiles.active=dev # (or --logging.level.root=INFO).
```

## Тестирование

Тестирование использует [Testcontainers](https://java.testcontainers.org/) для автоматического запуска временного контейнера PostgreSQL – не нужно настраивать базу данных вручную, достаточно просто запустить Docker.

```bash
mvn test -pl library
```

## Схема БД

![DB](<screenshots/CleanShot 2026-06-14 at 23.50.40@2x.png>)

## Остановка базы данных

```bash
docker compose down # или также добавьте флаг -v для удаления сохраненных данных
```
