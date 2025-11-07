# 🏦 BankSystem - Client

<div align="center">

![React](https://img.shields.io/badge/React-19.1-blue?style=for-the-badge&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue?style=for-the-badge&logo=typescript)
![Vite](https://img.shields.io/badge/Vite-7.1-646CFF?style=for-the-badge&logo=vite)
![Chakra UI](https://img.shields.io/badge/Chakra%20UI-3.28-319795?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)

**Клиентская часть (Frontend) учебного проекта банковской системы на React**

*Современный SPA интерфейс для управления клиентами и банковскими счетами*

[🚀 Быстрый старт](#-быстрый-старт) • [📚 Документация](#-документация) • [🏗️ Архитектура](#️-архитектура)

</div>

---

## 📋 О проекте

**BankSystem Client** — это **учебный проект** клиентской части банковской системы, реализованный на React в образовательных целях. Проект демонстрирует современные подходы к разработке фронтенд-приложений: компонентная архитектура, управление состоянием, работа с API, валидация форм и оптимизация производительности.

> ⚠️ **Важно:** Этот проект создан исключительно для образовательных целей и обучения технологиям React и современной фронтенд-разработки. Он не предназначен для использования в production среде или для реальных банковских операций.

### ✨ Ключевые особенности

- 🎨 **Современный UI/UX** — интерфейс на базе Chakra UI с поддержкой темной/светлой темы
- ⚡ **Высокая производительность** — оптимизация сборки через Vite, code splitting, lazy loading
- 🔄 **Управление состоянием** — React Query для кеширования и синхронизации данных с сервером
- ✅ **Валидация форм** — Zod схемы для типобезопасной валидации
- 🎯 **TypeScript** — полная типизация для надежности кода
- 🐳 **Docker ready** — оптимизированный Dockerfile с nginx для production

---

## 🛠️ Технологический стек

| Категория | Технологии |
|-----------|-----------|
| **Framework** | ![React](https://img.shields.io/badge/React-19.1-blue) ![React Router](https://img.shields.io/badge/React_Router-7.9-CA4245) |
| **Язык** | ![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue) |
| **Сборщик** | ![Vite](https://img.shields.io/badge/Vite-7.1-646CFF) |
| **UI библиотека** | ![Chakra UI](https://img.shields.io/badge/Chakra_UI-3.28-319795) ![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4.1-38B2AC) |
| **Управление состоянием** | ![React Query](https://img.shields.io/badge/React_Query-5.90-FF4154) |
| **HTTP клиент** | ![Axios](https://img.shields.io/badge/Axios-1.13-5A29E4) |
| **Валидация** | ![Zod](https://img.shields.io/badge/Zod-4.1-3D63C4) ![React Hook Form](https://img.shields.io/badge/React_Hook_Form-7.66-EC5990) |
| **Тестирование** | ![Vitest](https://img.shields.io/badge/Vitest-1.2-6E9F18) |
| **Контейнеризация** | ![Docker](https://img.shields.io/badge/Docker-Ready-2496ED) ![Nginx](https://img.shields.io/badge/Nginx-Ready-009639) |

---

## 🏗️ Архитектура

Проект использует **компонентную архитектуру** с четким разделением ответственности:

```
┌─────────────────────────────────────────┐
│         Pages Layer                     │
│   (Route Components, Page Logic)        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Features Layer                     │
│   (Business Features, Domain Logic)     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Components Layer                   │
│   (Reusable UI Components)              │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         API Layer                       │
│   (Services, Types, Interceptors)       │
└─────────────────────────────────────────┘
```

### 📁 Структура проекта

```
client/
├── src/
│   ├── api/              # API клиент и сервисы
│   │   ├── core/         # Базовый API клиент, interceptors
│   │   ├── services/     # Сервисы для работы с API
│   │   └── types/        # TypeScript типы для API
│   ├── components/       # Переиспользуемые UI компоненты
│   ├── features/         # Бизнес-логика по фичам
│   │   ├── clients/      # Функционал работы с клиентами
│   │   └── accounts/     # Функционал работы со счетами
│   ├── hooks/            # Custom React hooks
│   ├── pages/            # Страницы приложения
│   ├── routes/           # Конфигурация роутинга
│   ├── schemas/          # Zod схемы валидации
│   ├── utils/            # Утилиты и хелперы
│   └── theme/            # Тема приложения
├── public/               # Статические файлы
├── dist/                 # Собранное приложение (production)
├── Dockerfile            # Docker образ для production
├── nginx.conf            # Конфигурация nginx
├── vite.config.ts        # Конфигурация Vite
├── package.json          # Зависимости проекта
└── README.md             # Документация проекта
```

---

## 🚀 Быстрый старт

### Предварительные требования

- 📦 **Node.js 18+** или выше
- 📦 **Yarn** или **npm** для управления пакетами
- 🌐 **Браузер** с поддержкой современных стандартов ES6+

### 📦 Установка

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/vovgoo/BankSystem.git
cd BankSystem/client
```

2. **Установите зависимости**
```bash
yarn install
# или
npm install
```

3. **Настройте переменные окружения**

Создайте файл `.env` в корне папки `client`:
```bash
VITE_API_URL=http://localhost:8080
```

4. **Запустите приложение в режиме разработки**
```bash
yarn dev
# или
npm run dev
```

Приложение будет доступно по адресу: **http://localhost:5173**

### 🏗️ Сборка для production

```bash
yarn build
# или
npm run build
```

Собранное приложение будет находиться в папке `dist/`.

### 🐳 Запуск через Docker

```bash
# Сборка образа
docker build -t banksystem-client:latest .

# Запуск контейнера
docker run -p 3000:80 \
  -e VITE_API_URL=http://localhost:8080 \
  banksystem-client:latest
```

---

## 📚 Документация

### 🎯 Основные страницы

- **Dashboard** (`/`) — главная страница со статистикой системы
- **Clients** (`/clients`) — список всех клиентов с поиском и пагинацией
- **Client Details** (`/clients/:id`) — детальная информация о клиенте и его счетах

### 🔧 API интеграция

Клиент использует RESTful API для взаимодействия с сервером. Все запросы проходят через централизованный API клиент с автоматической обработкой ошибок и уведомлений.

**Основные сервисы:**
- `ClientsService` — управление клиентами
- `AccountsService` — управление счетами и транзакциями
- `StatsService` — получение статистики

### 🎨 UI компоненты

Проект использует библиотеку Chakra UI для построения интерфейса. Все компоненты находятся в папке `src/components/` и организованы по функциональности:

- **Layout** — компоненты макета (Header, MainLayout)
- **Forms** — компоненты форм (Input, Dialog)
- **Tables** — компоненты таблиц
- **UI** — базовые UI компоненты (Toaster, Tooltip)

---

## 📝 Лицензия

Этот проект распространяется под лицензией MIT. См. файл `LICENSE` для подробностей.

---

## 👨‍💻 Автор

**vovgoo**

- GitHub: [@vovgoo](https://github.com/vovgoo)

---

<div align="center">

**🎓 Учебный проект для изучения React и современных практик фронтенд-разработки**

⭐ Если проект помог вам в обучении, поставьте звезду!

</div>
