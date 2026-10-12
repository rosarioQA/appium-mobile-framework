# 🧠 Guia rápida — Appium Android

> Guía rápida para volver al proyecto.
> 
> Orden: **emulador → Appium → test → reporte**. Necesitas **3 terminales** abiertas.

---

## 0. Qué es cada cosa

| Pieza | Para qué sirve |
|-------|----------------|
| Android Studio / emulador | El "móvil" virtual donde corre la app |
| Appium (servidor) | Recibe órdenes del test y las manda al móvil |
| Maven | Lanza los tests (`mvn test`) |
| Cucumber (`.feature`) | Escenarios escritos en Gherkin |
| Steps → Page Objects | Código Java que ejecuta cada paso |
| Allure | Reporte visual de resultados |

Flujo: `Feature → Steps → Page Objects → BasePage → DriverFactory → Appium → emulador`

---

## 1. Comprobar que todo está instalado

```bash
java -version                    # debe ser 17
mvn -version                     # 3.9.x
adb version
appium --version                 # 3.x
appium driver list --installed   # debe aparecer uiautomator2
allure --version
```

Si falta el driver de Android:

```bash
appium driver install uiautomator2
```

---

## 2. Arrancar el emulador (Terminal 1 o Android Studio)

**Opción A – Android Studio:** `More Actions → Virtual Device Manager → ▶ Play` en tu AVD.

**Opción B – Terminal:**

```bash
emulator -list-avds              # ver el nombre de tus AVD
emulator -avd NOMBRE_DEL_AVD     # arrancarlo
```

Espera a que el móvil llegue a la pantalla de inicio y comprueba que se ve:

```bash
adb devices
```

Debe salir una línea con `emulator-5554   device`.
Si sale vacío u `offline`, ver sección *Problemas típicos*.

---

## 3. Arrancar el servidor Appium (Terminal 2)

```bash
appium
```

Déjalo abierto. Debe decir que escucha en `http://0.0.0.0:4723`.
**No cierres esta terminal** mientras corran los tests.

---

## 4. Ejecutar tests (Terminal 3, dentro de la carpeta del proyecto)

```bash
cd ruta/al/proyecto
```

| Qué quiero | Comando |
|------------|---------|
| Todo, con resultados limpios | `mvn clean test` |
| Solo un tag | `mvn clean test -Dcucumber.filter.tags="@tier1"` |
| Varios tags | `mvn clean test -Dcucumber.filter.tags="@smoke or @regression"` |
| Sin limpiar (rápido) | `mvn test` |

> Los tags dependen de lo que hayas puesto encima de cada escenario en los `.feature`
> (por ejemplo `@tier1`, `@local`). Búscalos con: `grep -r "@" src/test/resources/features`
>
> En PowerShell/Windows, si falla el filtro, usa las comillas así:
> `mvn clean test "-Dcucumber.filter.tags=@tier1"`

---

## 5. Ver el reporte

```bash
allure serve allure-results
```

Se abre en el navegador. Si ves más escenarios de los que ejecutaste, son resultados
viejos: lanza siempre con `mvn clean test`.

---

## 6. Al terminar

1. Cerrar el reporte de Allure (`Ctrl + C` en su terminal).
2. Parar Appium (`Ctrl + C` en la Terminal 2).
3. Cerrar el emulador.

---

## ✅ Resumen en 5 líneas

```bash
# 1) Abrir emulador (Android Studio o: emulator -avd NOMBRE)
adb devices                                   # 2) comprobar que aparece "device"
appium                                        # 3) en otra terminal, dejarlo abierto
mvn clean test -Dcucumber.filter.tags="@tier1"  # 4) en otra terminal
allure serve allure-results                   # 5) ver reporte
```

---

## 🛠️ Problemas típicos

| Síntoma | Qué hacer |
|---------|-----------|
| `adb devices` vacío u `offline` | `adb kill-server && adb start-server`, o reiniciar el emulador |
| `adb: command not found` | Falta añadir `platform-tools` del Android SDK al PATH (`ANDROID_HOME`) |
| Test falla al crear la sesión / "connection refused" | Appium no está arrancado o está en otro puerto (por defecto 4723) |
| `Address already in use` al lanzar `appium` | Ya hay un Appium abierto; ciérralo o reutilízalo |
| Falla por driver | `appium driver install uiautomator2` |
| La app no abre / no se encuentra el APK | Revisar la ruta del APK y el `appPackage`/`appActivity` en la configuración |
| Maven no reconoce tests/recursos en el IDE | `Reload All Maven Projects`; revisar que `src/test/java` no esté marcada como "test resources root" |
| Aserción de texto falla aunque "se ve igual" | Imprimir el valor real: puede tener sufijos, espacios o caracteres ocultos |
| Allure muestra escenarios que no ejecuté | Usar `mvn clean test` para borrar `allure-results` antiguos |
| Un elemento no se encuentra | Añadir espera explícita (`ExpectedConditions.elementToBeClickable`) antes de usarlo |

---

## 📍 Dónde está cada cosa en el proyecto

```
src/main/java/pages      → Page Objects (selectores y acciones de cada pantalla)
src/main/java/driver     → DriverFactory (crea el driver de Appium)
src/test/java/steps      → Step definitions (frase Gherkin → código Java)
src/test/java/hooks      → Hooks de Cucumber (before/after)
src/test/java/runners    → Runner de JUnit
src/test/resources/features → Archivos .feature
allure-results/          → Resultados del último run
```