package pages;
import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AlarmPage extends BasePage {

    private final By addAlarmButton = AppiumBy.id("com.google.android.deskclock:id/fab");
    private final By alarmList = By.id("com.google.android.deskclock:id/alarm_recycler_view");
    private final By saveAlarmButton = By.id("com.google.android.deskclock:id/material_timepicker_ok_button");
    private final By alarmCards = By.className("androidx.cardview.widget.CardView");
    private final By alarmTime = AppiumBy.id("com.google.android.deskclock:id/digital_clock");


    public AlarmPage() {
        super();
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

    public void selectHour(int hour) {
        By hourLocator = AppiumBy.accessibilityId(hour + " o'clock");
        waitUntilVisible(hourLocator);
        click(hourLocator);
    }

    public void selectMinute(int minute) {
        By hourLocator = AppiumBy.accessibilityId(minute + " minutes");
        waitUntilVisible(hourLocator);
        click(hourLocator);
    }

    private String normalizeText(String text) {
        String time = text.replace("PM", "").replace("AM", "");
        return time
                .replaceAll("\\p{Z}", "")
                .trim();
    }

    public String getLastAlarmTime() {
        WebElement recycler = driver.findElement(alarmList);
        List<WebElement> cards = recycler.findElements(alarmCards);

        var timeDisplayed =  cards.get(cards.size() - 1)
                .findElement(alarmTime)
                .getText();

        return normalizeText(timeDisplayed);
    }
}