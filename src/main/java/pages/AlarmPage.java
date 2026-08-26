package pages;
import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class AlarmPage extends BasePage {

    private final By addAlarmButton = AppiumBy.id("com.google.android.deskclock:id/fab");
    private final By alarmList = By.id("com.google.android.deskclock:id/alarm_recycler_view");
    private final By saveAlarmButton = By.id("com.google.android.deskclock:id/material_timepicker_ok_button");
    private final By alarmCards = By.className("androidx.cardview.widget.CardView");
    public final By alarmTime = AppiumBy.id("com.google.android.deskclock:id/digital_clock");

    public final By expandAlarmButton = AppiumBy.className("android.widget.ImageButton");
    public final By deleteAlarmButton = AppiumBy.androidUIAutomator(
            "new UiSelector()"
                    + ".className(\"android.widget.Button\")"
                    + ".text(\"Delete\")"
    );
    public final By AMButton = AppiumBy.id("com.google.android.deskclock:id/material_clock_period_am_button");
    public final By PMButton = AppiumBy.id("com.google.android.deskclock:id/material_clock_period_pm_button");


    public AlarmPage() {
        super();
    }

    public String[] splitTime(String time) {
        return time.replace(":", " ").split(" ");
    }

    public void tapAddAlarm() {
        click(addAlarmButton);
    }

    public void saveAlarm() {
        click(saveAlarmButton);
    }

    public boolean isAddAlarmButtonDisplayed() {
        return isDisplayed(alarmList);
    }

    public void selectHour(String hour) {
        By hourLocator = AppiumBy.accessibilityId(hour + " o'clock");
        waitUntilVisible(hourLocator);
        click(hourLocator);
    }

    public void selectMinutes(String minute) {
        By hourLocator = AppiumBy.accessibilityId(minute + " minutes");
        waitUntilVisible(hourLocator);
        click(hourLocator);
    }

    public String normalizeText(String text) {
        return text
                .trim()
                .replaceAll("\\p{Z}+", " ");
    }

    public String getAlarmTimeByPosition(String time) {
        var timeDisplayed =  getAlarmByPosition(time).findElement(alarmTime).getText();
        return normalizeText(timeDisplayed);
    }

    public int getAlarmCount() {
        WebElement recycler = driver.findElement(alarmList);
        List<WebElement> cards = recycler.findElements(alarmCards);
        return cards.size() ;
    }

    public WebElement getAlarmByPosition(String position) {
        WebElement recycler = driver.findElement(alarmList);
        List<WebElement> cards = recycler.findElements(alarmCards);
        int index = switch (position.toLowerCase()) {
            case "first" -> 0;
            case "second" -> 1;
            case "last" -> cards.size() - 1;
            default -> throw new IllegalArgumentException("Invalid alarm position: " + position);
        };
        return cards.get(index);
    }

    public WebElement getLastAlarm() {
        WebElement recycler = driver.findElement(alarmList);
        List<WebElement> cards = recycler.findElements(alarmCards);
        return  cards.get(cards.size() - 1);
    }



    public void deleteAlarm() {
        waitUntilVisible(deleteAlarmButton);
        click(deleteAlarmButton);
    }

    public void setBeforeOrAfterNoon(String clock) {
        if (Objects.equals(clock, "AM")) {
            click(AMButton);
        } else {
            click(PMButton);
        }
    }
}