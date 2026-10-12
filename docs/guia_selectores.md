# 🎯 Guía rápida de selectores — Appium Android

> Como para añadir un selector nuevo en el formato del proyecto y elegir el mejor tipo.
> Herramienta para encontrarlos: **Appium Inspector**. Para arrancar el entorno, consulta la [guia rapida de inicio](guia_rapida.md).

---

## 1. Ranking: qué selector usar

| Puesto | Estrategia | Atributo en Inspector | Valoración | Por qué |
|:---:|---|---|:---:|---|
| 🥇 | `AppiumBy.id` | `resource-id` | ✅ Mejor | Único, estable, rápido y no depende del idioma |
| 🥈 | `AppiumBy.accessibilityId` | `content-desc` | ✅ Muy bueno | Rápido y pensado para identificar elementos (⚠️ puede depender del idioma) |
| 🥉 | `AppiumBy.androidUIAutomator` | `text`, `description`, combinaciones | 🟡 Bueno | Flexible y nativo; permite hacer scroll. Más verboso |
| 4 | `AppiumBy.className` | `class` | 🟠 Solo dentro de un padre | Casi nunca es único por sí solo; útil para listas |
| 5 | `AppiumBy.xpath` relativo | cualquier atributo | 🔴 Último recurso | Lento y frágil ante cambios de estructura |
| ❌ | `AppiumBy.xpath` absoluto o por índice | — | ⛔ Nunca | Se rompe con cualquier cambio de pantalla |

**Regla de oro:** en el Inspector, mira primero si el elemento tiene `resource-id`. Si no, `content-desc`. Si no, UiAutomator. XPath solo si no queda otra.

---

## 2. Del Inspector al código

| Ves en Inspector | Escribes en Java |
|---|---|
| `resource-id = com.google.android.deskclock:id/fab` | `AppiumBy.id("com.google.android.deskclock:id/fab")` |
| `content-desc = 8 o'clock` | `AppiumBy.accessibilityId("8 o'clock")` |
| `text = Delete` | `AppiumBy.androidUIAutomator("new UiSelector().text(\"Delete\")")` |
| `class = androidx.cardview.widget.CardView` | `AppiumBy.className("androidx.cardview.widget.CardView")` |

> ✅ Antes de copiarlo al código, compruébalo en Inspector con 🔍 **Search for element**. Debe devolver **exactamente 1 resultado**, salvo que buscas una lista.

---

## 3. Formato esperado en el proyecto

### Plantilla

```java
package pages;

import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class MiPantallaPage extends BasePage {

    // ── Selectores ─────────────────────────────────────────
    private final By saveButton = AppiumBy.id("com.google.android.deskclock:id/save");

    // ── Acciones ───────────────────────────────────────────
    public void tapSave() {
        click(saveButton);
    }
}
```

### Convenciones

| ✅ Hazlo así | ❌ Evita |
|---|---|
| `private final By` | `public final By` (el step no debe tocar selectores) |
| `AppiumBy.id(...)` | Mezclar `By.id` y `AppiumBy.id` en la misma clase |
| `camelCase` con sufijo de tipo: `saveButton`, `alarmTab`, `searchInput`, `alarmList`, `cityLabel` | `AMButton`, `btn1`, `elemento` |
| Selectores arriba y acciones abajo | Selectores declarados dentro de los métodos (salvo los dinámicos) |
| Usar `click()`, `type()`, `getText()` de `BasePage` | `driver.findElement(...)` directo, sin espera |

### Sufijos recomendados

`Button` · `Tab` · `Input` · `Label` / `Text` · `List` · `Card` · `Icon` · `Toggle`

---

## 4. Recetas por tipo de selector

### 🥇 Por `resource-id`

```java
private final By addAlarmButton = AppiumBy.id("com.google.android.deskclock:id/fab");
```

> Usa siempre el id **completo** (`paquete:id/nombre`).

### 🥈 Por `content-desc` (accessibility id)

```java
private final By menuButton = AppiumBy.accessibilityId("More options");
```

### 🥉 Por UiAutomator

```java
// Texto exacto
private final By deleteButton = AppiumBy.androidUIAutomator(
        "new UiSelector().text(\"Delete\")");

// Texto parcial
private final By resultLabel = AppiumBy.androidUIAutomator(
        "new UiSelector().textContains(\"Argent\")");

// Clase + texto (cuando el texto se repite en varios elementos)
private final By deleteButton = AppiumBy.androidUIAutomator(
        "new UiSelector().className(\"android.widget.Button\").text(\"Delete\")");

// Scroll hasta encontrar el elemento
private final By argentinaRow = AppiumBy.androidUIAutomator(
        "new UiScrollable(new UiSelector().scrollable(true))"
      + ".scrollIntoView(new UiSelector().text(\"Argentina\"))");
```

> ⚠️ Las comillas internas van escapadas: `\"`.

### 🔴 XPath (solo si no hay alternativa)

```java
// 🟠 Aceptable: relativo, por atributo
private final By cityLabel = AppiumBy.xpath("//*[@resource-id='com.google.android.deskclock:id/city_name']");

// ⛔ Nunca: absoluto o por posición
private final By cityLabel = AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/.../android.widget.TextView[2]");
```

> Si tu XPath solo usa `@resource-id` o `@content-desc`, cámbialo por `AppiumBy.id` o `AppiumBy.accessibilityId`: es lo mismo, pero más rápido.

---

## 5. Casos especiales

### Selector dinámico (depende de un parámetro)

Se construye dentro del método:

```java
public void selectHour(String hour) {
    By hourLocator = AppiumBy.accessibilityId(hour + " o'clock");
    click(hourLocator);
}
```

### Listas: buscar dentro de un contenedor

Primero localiza el **padre** con id y después los **hijos** por clase o id:

```java
private final By alarmList  = AppiumBy.id("com.google.android.deskclock:id/alarm_recycler_view");
private final By alarmCards = AppiumBy.className("androidx.cardview.widget.CardView");

public List<WebElement> getAlarms() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(alarmList));
    return driver.findElement(alarmList).findElements(alarmCards);
}
```

### Elemento dentro de otro elemento

```java
getAlarmByPosition("first").findElement(alarmTime).click();
```

### Esperar a que haya N elementos antes de usar un índice

```java
wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchResults, position));
```

---

## 6. Trampas habituales

| Trampa | Consecuencia | Solución |
|---|---|---|
| Selector por `text` o `content-desc` | Falla si el emulador cambia de idioma | Preferir `resource-id`; si no hay, documentar el idioma requerido |
| `findElement` sin espera | `NoSuchElementException` intermitente | Usar `click()` / `find()` de `BasePage` |
| Índices fijos (`[2]`, `get(1)`) | Rompe si cambia el orden o el número de elementos | Buscar por atributo; si hace falta índice, esperar a que la lista tenga elementos |
| `resource-id` repetido en varias filas | Se toca siempre el primero | Buscar dentro del contenedor de la fila |
| Selector probado con la pantalla en otro estado | No lo encuentra al ejecutar | Comprobar en Inspector con la pantalla en el mismo estado que el test |
| `text` con espacios o caracteres invisibles | La aserción falla "sin motivo" | Normalizar el texto (`normalizeText`) antes de comparar |
