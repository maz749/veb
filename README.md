# JSF Point Validation Application

Веб-приложение на базе JavaServer Faces Framework для проверки попадания точки в заданную область на координатной плоскости.

## Описание

Приложение позволяет пользователю задавать координаты точки (X, Y) и радиус области (R), после чего проверяет, попадает ли точка в заданную область. Область определена согласно варианту 4 и состоит из:

- **Прямоугольник в 1-й четверти**: x: [0, R/2], y: [0, R]
- **Четверть круга во 2-й четверти**: радиус R/2
- **Треугольник в 4-й четверти**: x: [0, R/2], y: [-R/2, 0]

## Функциональность

### Стартовая страница (index.xhtml)
- Шапка с ФИО студента, номером группы и варианта
- Интерактивные часы с автообновлением каждые 13 секунд
- Ссылка для перехода на основную страницу

### Основная страница (main.xhtml)
- **Ввод координат:**
  - X: выбор из кнопок (-4, -3, -2, -1, 0, 1, 2, 3, 4)
  - Y: текстовое поле с валидацией диапазона (-3 до 5)
  - R: слайдер ICEfaces (от 2 до 5 с шагом 0.25)

- **Интерактивный график:**
  - Динамическая отрисовка области на Canvas
  - Отображение всех проверенных точек
  - Клик по графику для добавления новой точки

- **Таблица результатов:**
  - История всех проверок с пагинацией
  - Сохранение в базу данных Oracle/H2

## Технологии

- **JSF 2.3** (Mojarra)
- **JPA 2.2** (Hibernate)
- **CDI 2.0** (Weld)
- **PrimeFaces 12.0** - компоненты UI
- **ICEfaces 4.3** - ace:sliderEntry
- **H2/Oracle Database** - хранение данных
- **Gradle** - система сборки

## Структура проекта

```
veb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jsf/pointvalidation/
│   │   │       ├── beans/          # Managed Beans
│   │   │       │   ├── ResultsBean.java
│   │   │       │   ├── ClockBean.java
│   │   │       │   └── GraphBean.java
│   │   │       ├── entity/         # JPA Entity
│   │   │       │   └── PointResult.java
│   │   │       └── util/           # Утилиты
│   │   │           └── AreaChecker.java
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   ├── faces-config.xml
│   │       │   └── beans.xml
│   │       ├── resources/
│   │       │   ├── css/
│   │       │   │   └── styles.css
│   │       │   └── js/
│   │       │       └── canvas.js
│   │       ├── index.xhtml
│   │       └── main.xhtml
│   └── test/
└── build.gradle
```

## Сборка и развертывание

### Требования
- JDK 8 или выше
- Gradle 7.x или выше
- Apache Tomcat 9.x / WildFly / GlassFish (сервер приложений)

### Сборка проекта

```bash
# Собрать WAR файл
./gradlew war

# WAR файл будет создан в build/libs/jsf-point-validation.war
```

### Развертывание

#### На Tomcat:
1. Скопировать `build/libs/jsf-point-validation.war` в `<TOMCAT_HOME>/webapps/`
2. Запустить Tomcat: `<TOMCAT_HOME>/bin/startup.sh`
3. Открыть в браузере: `http://localhost:8080/jsf-point-validation/`

#### На WildFly:
1. Скопировать WAR в `<WILDFLY_HOME>/standalone/deployments/`
2. Запустить WildFly: `<WILDFLY_HOME>/bin/standalone.sh`
3. Открыть в браузере: `http://localhost:8080/jsf-point-validation/`

## Конфигурация базы данных

### H2 (по умолчанию для разработки)
База данных создается автоматически при первом запуске в домашней директории пользователя.

### Oracle Database
Для использования Oracle DB отредактируйте `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="javax.persistence.jdbc.driver" value="oracle.jdbc.OracleDriver"/>
<property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:XE"/>
<property name="javax.persistence.jdbc.user" value="your_username"/>
<property name="javax.persistence.jdbc.password" value="your_password"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.Oracle12cDialect"/>
```

Также добавьте Oracle JDBC драйвер в `build.gradle`:
```gradle
implementation 'com.oracle.database.jdbc:ojdbc8:21.1.0.0'
```

## Архитектура

### Managed Beans

- **ResultsBean** (Session-scoped) - управление результатами проверок
- **ClockBean** (Application-scoped) - предоставление текущего времени
- **GraphBean** (Request-scoped) - отрисовка графика

### Navigation Rules

Правила навигации определены в `faces-config.xml`:
- `index → main` - переход к основному приложению
- `main → index` - возврат на стартовую страницу

### Валидация

- **X**: выбор из предопределенных значений через командные кнопки
- **Y**: валидация диапазона от -3 до 5 с помощью `f:validateDoubleRange`
- **R**: ограничение через параметры ICEfaces слайдера (2-5, шаг 0.25)

## Особенности реализации

1. **Часы с автообновлением**: используется `p:poll` с интервалом 13 секунд
2. **Интерактивный график**: Canvas + JavaScript для обработки кликов
3. **Динамическая отрисовка**: график обновляется при изменении R или добавлении точек
4. **Персистентность**: все результаты сохраняются в БД через JPA
5. **AJAX**: минимальная перезагрузка страницы благодаря `f:ajax`

## Автор

**ФИО**: Иванов Иван Иванович
**Группа**: P3212
**Вариант**: 4

## Лицензия

Учебный проект. Все права защищены.
