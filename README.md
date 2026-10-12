# Appium Android Automation Framework

Framework de automatización de pruebas móviles para **Android** construido con Java, Appium, Cucumber y JUnit 5, con reportes en Allure.

Es un proyecto de **práctica y aprendizaje** que automatiza la app **Reloj de Google** (`com.google.android.deskclock`), preinstalada en los emuladores de Android Studio. No hay que descargar ningún APK.

> 🧠 **¿Vuelves tras un tiempo?** Consulta la [guía rápida](docs/guia_rapida.md): qué arrancar, en qué orden y cómo lanzar un test en pocos minutos.

---

## Índice

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecutar pruebas](#ejecutar-pruebas)
- [Tags disponibles](#tags-disponibles)
- [Reportes Allure](#reportes-allure)
- [Ejemplo: de la feature al código](#ejemplo-de-la-feature-al-código)
- [Cómo añadir un test nuevo](#cómo-añadir-un-test-nuevo)
- [Problemas frecuentes](#problemas-frecuentes)
- [Roadmap](#roadmap)
- [Autor](#autor)

---

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Maven | 3.9.x |
| Appium Server | 3.x |
| Appium Java Client | 10.1.1 |
| Driver Appium Android | UiAutomator2 |
| Cucumber | 7.22.1 |
| JUnit (Jupiter / Platform Suite) | 5.12.2 |
| Allure (allure-cucumber7-jvm) | 2.35.3 |
| Maven Surefire Plugin | 3.5.3 |

---

## Arquitectura

```text
Feature (.feature, Gherkin)        → qué se prueba, en lenguaje natural
   ↓
Steps (step definitions)           → traduce cada frase Gherkin a código Java
   ↓
Page Objects                       → selectores y acciones de cada pantalla
   ↓
BasePage + WaitFactory             → acciones comunes con esperas explícitas
   ↓
DriverManager / DriverFactory      → crea y guarda la sesión de Appium (ThreadLocal)
   ↓
Servidor Appium (UiAutomator2)     → recibe los comandos
   ↓
Emulador / dispositivo Android     → ejecuta la app
```

Piezas de soporte:

- **TestRunner**: suite de JUnit Platform que lanza el motor de Cucumber, con glue en `steps` y `hooks`.
- **Hooks**: `@Before` abre la sesión de Appium. `@After` la cierra y, si el escenario falla, adjunta a Allure una captura de pantalla, las capabilities de la sesión y datos del entorno.
- **ConfigManager**: lee `config.properties` desde el classpath.

### Buenas prácticas aplicadas

- **Page Object Model**: los selectores viven en las páginas, nunca en los steps.
- **Esperas explícitas** centralizadas en `WaitFactory` (timeout por defecto: 10 s). No se usa `Thread.sleep`.
- **Driver en `ThreadLocal`**, preparado para ejecución en paralelo.
- **Normalización de texto** en las aserciones, para evitar fallos por espacios o caracteres no visibles.
- **Evidencias automáticas en fallos**: captura en Allure y copia en `target/screenshots/`.
- **Cucumber `Rule` y `Background`** para agrupar escenarios relacionados.

---

## Estructura del proyecto

```text
src
├── main
│   ├── java
│   │   ├── config      → ConfigManager
│   │   ├── core        → BasePage, WaitFactory
│   │   ├── driver      → DriverFactory, DriverManager
│   │   └── pages       → MenuPage, AlarmPage, ClockPage
│   └── resources
│       └── config.properties
│
└── test
    ├── java
    │   ├── hooks       → Hooks
    │   ├── runners     → TestRunner
    │   ├── steps       → BaseSteps, AlarmSteps, ClockSteps, InitSessionSteps
    │   └── utils       → AllureUtils, ScreenshotUtils
    │
    └── resources
        └── features    → alarm, clock, initSession
```

---

## Requisitos

- **Java 17**
- **Maven 3.9.x**
- **Node.js / npm**, necesarios para instalar Appium
- **Appium 3**: `npm install -g appium`
- **Driver UiAutomator2**: `appium driver install uiautomator2`
- **Android Studio** con Android SDK y al menos un dispositivo virtual (AVD)
- Variable de entorno **`ANDROID_HOME`** apuntando al SDK, con `platform-tools` en el `PATH` (para `adb`)
- **Allure CLI**, para ver los reportes
- *(Opcional)* **Appium Inspector**, para localizar selectores

> ⚠️ **Idioma y formato del emulador:** los selectores usan textos en inglés (`"8 o'clock"`, `"Delete"`, títulos `Alarm`/`Clock`) y el formato de 12 horas (AM/PM). El emulador debe estar **en inglés** y con **formato de hora de 12 h**.

Comprobar la instalación:

```bash
java -version
mvn -version
adb devices
appium --version
appium driver list --installed
allure --version
```

---

## Instalación

Clonar el repositorio:

```bash
git clone https://github.com/rosarioQA/appium-mobile-framework.git
cd appium-mobile-framework
```

Descargar dependencias y compilar **sin ejecutar los tests**. Los tests necesitan un emulador y Appium arrancados:

```bash
mvn clean install -DskipTests
```

---

## Configuración

La configuración de la sesión está en `src/main/resources/config.properties`:

```properties
platformName=Android
automationName=UiAutomator2
deviceName=Pixel_6
appPackage=com.google.android.deskclock
appActivity=com.android.deskclock.DeskClock

serverUrl=http://127.0.0.1:4723

newCommandTimeout=60
```

| Propiedad | Para qué sirve | ¿Cuándo cambiarla? |
|-----------|----------------|--------------------|
| `platformName` | Plataforma de la sesión | Por ahora solo `Android` |
| `automationName` | Driver de Appium | No cambiar |
| `deviceName` | Nombre del dispositivo | Si usas otro AVD (orientativo: Appium usa el dispositivo que detecta `adb`) |
| `appPackage` / `appActivity` | App y pantalla de inicio | Si se automatiza otra app |
| `serverUrl` | Dirección del servidor Appium | Si Appium corre en otro puerto o máquina |
| `newCommandTimeout` | Segundos sin comandos antes de cerrar la sesión | Si depuras paso a paso y la sesión caduca |

---

## Ejecutar pruebas

### 1. Orden de arranque

1. **Emulador**: arráncalo desde Android Studio o con `emulator -avd NOMBRE_DEL_AVD`, y comprueba con `adb devices` que aparece como `device`.
2. **Appium**: ejecuta `appium` en otra terminal y déjala abierta.
3. **Tests**: ejecútalos desde la carpeta del proyecto con los comandos de abajo.

### 2. Primera ejecución recomendada

Comprueba que el entorno funciona abriendo solo una sesión de Appium:

```bash
mvn clean test -Dcucumber.filter.tags="@initial"
```

### 3. Comandos habituales

| Qué quiero | Comando |
|------------|---------|
| Todos los escenarios | `mvn clean test` |
| Escenarios básicos | `mvn clean test -Dcucumber.filter.tags="@tier1"` |
| Una feature | `mvn clean test -Dcucumber.filter.tags="@alarm"` |
| Varias features | `mvn clean test -Dcucumber.filter.tags="@alarm or @clock"` |
| Excluir un tag | `mvn clean test -Dcucumber.filter.tags="@alarm and not @deleteAlarm"` |

> 💡 **¿`clean test` o solo `test`?** El proyecto configura `maven-clean-plugin` para borrar `allure-results/` y `allure-report/` en la fase `clean`. Si ejecutas solo `mvn test`, el reporte mezclará resultados de ejecuciones anteriores. Usa `mvn clean test` cuando quieras un reporte limpio.

> 🪟 **Windows / PowerShell:** si el filtro de tags falla, pon las comillas alrededor de todo el argumento:
> `mvn clean test "-Dcucumber.filter.tags=@tier1"`

---

## Tags disponibles

| Tag | Alcance | Descripción |
|-----|---------|-------------|
| `@initial` | Feature `initSession` | Solo verifica que se crea la sesión de Appium |
| `@alarm` | Feature `alarm` | Todos los escenarios de alarmas |
| `@clock` | Feature `clock` | Todos los escenarios del reloj mundial |
| `@tier1` | Escenarios | Navegación básica: abrir las pestañas Alarm y Clock |
| `@basic` | Escenario | Abrir la pestaña Alarm |
| `@addAlarm` | Escenario | Crear una alarma a las 8:10 PM |
| `@editAlarm` | Escenario | Cambiar la primera alarma a las 2:00 AM |
| `@deleteAlarm` | Escenario | Borrar la última alarma |
| `@addTimezone` | Escenario | Añadir "Argentina" al reloj mundial |

> ⚠️ **Los escenarios modifican el estado del emulador.** `@deleteAlarm` espera encontrar **exactamente 2 alarmas** y `@editAlarm` necesita al menos una. Si antes se ejecutó `@addAlarm`, habrá una alarma de más y `@deleteAlarm` fallará. Para repetir una ejecución limpia, borra las alarmas sobrantes o restablece el emulador (*Wipe Data* en el Device Manager).

---

## Reportes Allure

Los resultados se generan en `allure-results/`.

Ver el reporte directamente, porque genera uno temporal y lo abre en el navegador:

```bash
allure serve allure-results
```

Generar un reporte estático para conservarlo o compartirlo:

```bash
allure generate allure-results -o allure-report --clean
allure open allure-report
```

Cuando un escenario falla, el reporte incluye:

- Captura de pantalla del momento del fallo
- Capabilities de la sesión (plataforma, dispositivo, versión de Android, app)
- Entorno de ejecución (sistema operativo, versión de Java)

Las capturas también se guardan en `target/screenshots/`.

---

## Ejemplo: de la feature al código

**Feature** (`alarm.feature`):

```gherkin
@addAlarm
Scenario: Add a new alarm
  And I add a new alarm with "8:10 PM"
  Then a "last" alarm is added and displays "8:10 PM"
```

**Step definition** (`AlarmSteps.java`):

```java
@When("I add a new alarm with {string}")
public void addNewAlarmWith(String newTime) {
    alarmPage.tapAddAlarm();
    String[] result = alarmPage.splitTime(newTime);   // "8:10 PM" → [8, 10, PM]
    alarmPage.selectHour(result[0]);
    alarmPage.selectMinutes(result[1]);
    alarmPage.setBeforeOrAfterNoon(result[2]);
    alarmPage.saveAlarm();
}
```

**Page Object** (`AlarmPage.java`):

```java
public void selectHour(String hour) {
    By hourLocator = AppiumBy.accessibilityId(hour + " o'clock");
    waitUntilVisible(hourLocator);
    click(hourLocator);
}
```

El step no conoce ningún selector, solo llama a acciones de la página. Si cambia la interfaz, solo hay que tocar el Page Object.

---

## Cómo añadir un test nuevo

1. **Localiza los elementos** con Appium Inspector. Prioriza `resource-id` y `accessibility id` frente a XPath.
2. **Page Object**: añade los selectores (`By`) y las acciones en la página que corresponda dentro de `src/main/java/pages`, o crea una nueva que extienda `BasePage`.
3. **Steps**: crea el método en `src/test/java/steps` con la anotación Gherkin (`@Given`, `@When`, `@Then`). Llama solo a métodos del Page Object y pon las aserciones aquí.
4. **Feature**: escribe el escenario en `src/test/resources/features` y ponle un **tag** para poder ejecutarlo de forma aislada.
5. **Ejecuta solo ese escenario**: `mvn clean test -Dcucumber.filter.tags="@miNuevoTag"`.

---

## Problemas frecuentes

| Síntoma | Solución |
|---------|----------|
| `adb devices` vacío u `offline` | `adb kill-server` y `adb start-server`, o reinicia el emulador |
| `Connection refused` al crear la sesión | Appium no está arrancado o no escucha en `serverUrl` |
| `Address already in use` al lanzar `appium` | Ya hay un Appium abierto: ciérralo o reutilízalo |
| Error de driver al crear la sesión | `appium driver install uiautomator2` |
| No encuentra `"8 o'clock"`, `"Delete"`… | El emulador no está en inglés o no usa formato de 12 h |
| `@deleteAlarm` falla en el conteo | Hay más de 2 alarmas: ver [Tags disponibles](#tags-disponibles) |
| El IDE no reconoce tests o recursos | *Reload All Maven Projects* y revisa que `src/test/java` esté marcada como *Test Sources Root* |
| Una aserción de texto falla aunque "se ve igual" | Inspecciona el valor real: puede tener espacios, sufijos o caracteres no visibles |
| Allure muestra escenarios que no se ejecutaron | Ejecuta con `mvn clean test` para borrar resultados antiguos |

Para más detalle, consulta el [machete](MACHETE_ANDROID.md).

---

## Roadmap

- [x] Framework Android con Page Object Model, Cucumber y Allure
- [x] Evidencias automáticas (captura, capabilities, entorno) en escenarios fallidos
- [ ] Soporte iOS (XCUITest). `DriverFactory` ya tiene preparado el punto de extensión
- [ ] Selección de plataforma y entorno por línea de comandos (perfiles Maven)
- [ ] Ejecución en dispositivos en la nube (Sauce Labs)
- [ ] Integración continua con GitHub Actions

> Este repositorio es el proyecto **público de práctica**.
---

## Autor

**Rosario** · [github.com/rosarioQA](https://github.com/rosarioQA)