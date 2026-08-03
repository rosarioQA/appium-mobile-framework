# Appium Android Automation Framework

Framework de automatización de pruebas para Android desarrollado con:

- Java 17
- Appium 3
- Cucumber 7
- JUnit 5
- Maven
- Allure Reports

## Objetivo

Automatizar pruebas funcionales sobre aplicaciones Android siguiendo buenas prácticas de diseño:

- Page Object Model
- Driver Factory
- Hooks de Cucumber
- Esperas explícitas
- Reportes Allure

---

## Tecnologías

| Tecnología | Versión |
|------------|----------|
| Java | 17 |
| Maven | 3.9.x |
| Appium | 3.x |
| Java Client | 10.x |
| Cucumber | 7.x |
| JUnit | 5 |
| Allure | 2.x |

---

## Estructura del proyecto

```
src
├── main
│   └── java
│       ├── core
│       ├── pages
│       └── utils
│
└── test
    ├── java
    │   ├── hooks
    │   ├── runners
    │   └── steps
    │
    └── resources
        └── features
```

---

## Requisitos

- Java 17
- Android Studio
- Android SDK
- Appium Server
- Emulator o dispositivo físico

Comprobar instalación:

```bash
java -version
mvn -version
adb devices
appium
```

---

## Instalación

Clonar el repositorio

```bash
git clone https://github.com/usuario/appium-framework.git
```

Entrar al proyecto

```bash
cd appium-framework
```

Instalar dependencias

```bash
mvn clean install
```

---

## Ejecutar pruebas

Ejecutar todos los escenarios

```bash
mvn test
```

Ejecutar por tags

```bash
mvn test -Dcucumber.filter.tags="@Smoke"
```

Ejecutar múltiples tags

```bash
mvn test -Dcucumber.filter.tags="@Smoke or @Regression"
```

---

## Reportes Allure

Generar reporte

```bash
allure serve allure-results
```

---

## Arquitectura

El proyecto sigue el patrón:

Feature
↓
Steps
↓
Page Objects
↓
BasePage
↓
DriverFactory

---

## Buenas prácticas

- No utilizar Thread.sleep()
- Utilizar waits explícitos
- Centralizar locators
- Mantener Page Objects pequeños
- Una responsabilidad por clase

---

## Autor

Rosario Segovia