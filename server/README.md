# 🏦 BankSystem

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Gradle](https://img.shields.io/badge/Gradle-8.10-02303A?style=for-the-badge&logo=gradle)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)

**Современная банковская система на Spring Boot с полной защитой транзакций и оптимизацией производительности**

[🚀 Быстрый старт](#-быстрый-старт) • [📚 Документация](#-api-документация) • [🧪 Тестирование](#-тестирование) • [🏗️ Архитектура](#️-архитектура)

</div>

---

## 📋 О проекте

**BankSystem** — это RESTful API для управления банковскими операциями, реализованное на Spring Boot. Система поддерживает управление клиентами, счетами и финансовыми транзакциями с гарантированной безопасностью и целостностью данных.

### ✨ Ключевые особенности

- 🔒 **Полная защита транзакций** — оптимистичная блокировка через `@Version` предотвращает race conditions
- ⚡ **Оптимизированная производительность** — batch загрузка данных, минимизация запросов к БД
- 📊 **Мониторинг и статистика** — Spring Boot Actuator для health checks и метрик
- 🔄 **Версионирование БД** — Liquibase для управления миграциями схемы
- 📖 **Полная документация** — Swagger/OpenAPI с примерами всех endpoints
- ✅ **Покрытие тестами** — Unit и интеграционные тесты для всех слоев
- 🐳 **Docker ready** — оптимизированный Dockerfile для production

---

## 🛠️ Технологический стек

| Категория | Технологии |
|-----------|-----------|
| **Framework** | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-brightgreen) ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.5.7-brightgreen) ![Spring Validation](https://img.shields.io/badge/Spring_Validation-3.5.7-brightgreen) |
| **База данных** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![Liquibase](https://img.shields.io/badge/Liquibase-Enabled-yellow) |
| **Документация** | ![Swagger](https://img.shields.io/badge/Swagger_OpenAPI-3.0-green) |
| **Мониторинг** | ![Actuator](https://img.shields.io/badge/Spring_Actuator-Enabled-success) |
| **Инструменты** | ![Lombok](https://img.shields.io/badge/Lombok-Enabled-orange) ![Gradle](https://img.shields.io/badge/Gradle-8.10-02303A) |
| **Тестирование** | ![JUnit 5](https://img.shields.io/badge/JUnit_5-Enabled-25A162) ![Mockito](https://img.shields.io/badge/Mockito-Enabled-green) ![H2](https://img.shields.io/badge/H2_Database-Testing-blue) |
| **Контейнеризация** | ![Docker](https://img.shields.io/badge/Docker-Ready-2496ED) |

---

## 🏗️ Архитектура

Проект использует **слоистую архитектуру** с четким разделением ответственности:

```
┌─────────────────────────────────────────┐
│         Controller Layer                 │
│   (REST API, Swagger Documentation)      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Service Layer                    │
│   (Business Logic, Transactions)          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│       Repository Layer                   │
│   (Data Access, JPA/Hibernate)           │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Entity Layer                     │
│   (Domain Models, JPA Entities)          │
└─────────────────────────────────────────┘
```

### 📁 Структура проекта

```
BankSystem/
├── src/
│   ├── main/
│   │   ├── java/com/vovgoo/BankSystem/
│   │   │   ├── controller/      # REST контроллеры
│   │   │   ├── service/         # Бизнес-логика
│   │   │   ├── repository/      # Доступ к данным
│   │   │   ├── entity/          # JPA сущности
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Обработка исключений
│   │   │   ├── mapper/          # Маппинг сущностей
│   │   │   └── config/          # Конфигурация (Swagger, Properties)
│   │   └── resources/
│   │       ├── application.yml  # Основная конфигурация
│   │       └── db/changelog/    # Liquibase миграции
│   └── test/                    # Unit и интеграционные тесты
├── Dockerfile                   # Docker образ для production
├── .dockerignore               # Исключения для Docker
├── build.gradle                 # Gradle конфигурация
└── README.md                   # Документация проекта
```

---

## 🚀 Быстрый старт

### Предварительные требования

- ☕ **Java 21** или выше
- 🐘 **PostgreSQL 15+** (или Docker для запуска PostgreSQL)
- 🔨 **Gradle 8.10+** (опционально, используется wrapper)

### 📦 Установка

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/vovgoo/BankSystem.git
cd BankSystem/server
```

2. **Настройте базу данных**

Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE BankSystem;
```

Или используйте Docker:
```bash
docker run --name bank-postgres \
  -e POSTGRES_DB=BankSystem \
  -e POSTGRES_USER=vovgoo \
  -e POSTGRES_PASSWORD=StrongestLocalPassword \
  -p 5433:5432 \
  -d postgres:15
```

3. **Настройте переменные окружения**

Создайте файл `.env` или установите переменные окружения:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/BankSystem
export SPRING_DATASOURCE_USERNAME=vovgoo
export SPRING_DATASOURCE_PASSWORD=StrongestLocalPassword
```

4. **Запустите приложение**

С помощью Gradle:
```bash
./gradlew bootRun
```

Или соберите и запустите JAR:
```bash
./gradlew build
java -jar build/libs/BankSystem-0.0.1-SNAPSHOT.jar
```

### 🐳 Запуск через Docker

```bash
# Сборка образа
docker build -t bank-system:latest .

# Запуск контейнера
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/BankSystem \
  -e SPRING_DATASOURCE_USERNAME=vovgoo \
  -e SPRING_DATASOURCE_PASSWORD=StrongestLocalPassword \
  bank-system:latest
```

---

## 📚 API Документация

После запуска приложения, Swagger UI доступен по адресу:

**🌐 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### 📋 Доступные endpoints

#### 👥 Клиенты (`/api/v1/clients`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/clients/search` | Поиск клиентов по фамилии с пагинацией |
| `GET` | `/api/v1/clients/{id}` | Получить детальную информацию о клиенте |
| `POST` | `/api/v1/clients` | Создать нового клиента |
| `PUT` | `/api/v1/clients` | Обновить данные клиента |
| `DELETE` | `/api/v1/clients/{id}` | Удалить клиента (только если нет счетов) |

#### 💰 Счета (`/api/v1/accounts`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/v1/accounts` | Создать новый счет для клиента |
| `DELETE` | `/api/v1/accounts/{id}` | Удалить счет (только если баланс = 0) |
| `POST` | `/api/v1/accounts/deposit` | Пополнить счет |
| `POST` | `/api/v1/accounts/withdraw` | Снять средства со счета |
| `POST` | `/api/v1/accounts/transfer` | Перевод между счетами с комиссией |

#### 📊 Статистика (`/api/v1/stats`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/stats` | Получить общую статистику по системе |

### 💡 Примеры запросов

#### Создание клиента
```bash
curl -X POST http://localhost:8080/api/v1/clients \
  -H "Content-Type: application/json" \
  -d '{
    "lastName": "Иванов",
    "phone": "+375441112233"
  }'
```

#### Создание счета
```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

#### Перевод между счетами
```bash
curl -X POST http://localhost:8080/api/v1/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": "111e4567-e89b-12d3-a456-426614174000",
    "toAccountId": "222e4567-e89b-12d3-a456-426614174000",
    "amount": 1000.50
  }'
```

---

## 🧪 Тестирование

Проект включает **полное покрытие тестами** для всех слоев приложения:

### 📊 Статистика тестов

- ✅ **12 тестовых классов**
- ✅ **Unit тесты** для сервисов, контроллеров, сущностей
- ✅ **Интеграционные тесты** с H2 in-memory базой данных
- ✅ **JPA тесты** для проверки работы с базой данных

## 🎯 Особенности реализации

### 💰 Финансовая точность

- Все денежные операции используют `BigDecimal` для точности расчетов
- Комиссия при переводе настраивается через `application.yml`
- Валидация масштаба (не более 2 знаков после запятой)

### 🔄 Управление миграциями

Liquibase автоматически применяет миграции при запуске приложения:
- Версионирование схемы БД
- Откат изменений при необходимости
- История всех изменений

### 🎨 Архитектурные паттерны

- **DTO Pattern** — разделение слоев через Data Transfer Objects
- **Repository Pattern** — абстракция доступа к данным
- **Service Layer Pattern** — инкапсуляция бизнес-логики
- **Global Exception Handler** — централизованная обработка ошибок

---

## 📈 Производительность

### Оптимизации

✅ **Batch загрузка** счетов через `findByIdIn()`  
✅ **JOIN FETCH** для избежания N+1 проблем  
✅ **Оптимистичная блокировка** для concurrent операций  
✅ **Connection pooling** через HikariCP (по умолчанию в Spring Boot)

---

## 🤝 Вклад в проект

Мы приветствуем вклад в проект! Пожалуйста:

1. Fork проекта
2. Создайте feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit изменения (`git commit -m 'Add some AmazingFeature'`)
4. Push в branch (`git push origin feature/AmazingFeature`)
5. Откройте Pull Request

---

## 📝 Лицензия

Этот проект распространяется под лицензией MIT. См. файл `LICENSE` для подробностей.

---

## 👨‍💻 Автор

**vovgoo**

- GitHub: [@vovgoo](https://github.com/vovgoo)

---

<div align="center">

**Сделано с ❤️ используя Spring Boot в качестве образовательных целей**

⭐ Если проект был полезен, поставьте звезду!

</div>

