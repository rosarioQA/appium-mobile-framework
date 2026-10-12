package pages;

import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AlarmPage extends BasePage {

    private final By addAlarmButton = AppiumBy.id("com.google.android.deskclock:id/fab");
    private final By alarmList = AppiumBy.id("com.google.android.deskclock:id/alarm_recycler_view");
    private final By alarmCard = AppiumBy.className("androidx.cardview.widget.CardView");
    private final By alarmTimeLabel = AppiumBy.id("com.google.android.deskclock:id/digital_clock");
    private final By expandAlarmButton = AppiumBy.className("android.widget.ImageButton");
    private final By deleteAlarmButton = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.Button\").text(\"Delete\")");

    private final By saveAlarmButton = AppiumBy.id("com.google.android.deskclock:id/material_timepicker_ok_button");
    private final By amButton = AppiumBy.id("com.google.android.deskclock:id/material_clock_period_am_button");
    private final By pmButton = AppiumBy.id("com.google.android.deskclock:id/material_clock_period_pm_button");

    public AlarmPage() {
        super();
    }

    public void tapAddAlarm() {
        click(addAlarmButton);
    }

    public boolean isAddAlarmButtonDisplayed() {
        return isDisplayed(addAlarmButton);
    }

    public void selectHour(String hour) {
        click(AppiumBy.accessibilityId(hour + " o'clock"));
    }

    public void selectMinutes(String minutes) {
        click(AppiumBy.accessibilityId(minutes + " minutes"));
    }

    public void setBeforeOrAfterNoon(String period) {
        click("AM".equalsIgnoreCase(period) ? amButton : pmButton);
    }

    public void saveAlarm() {
        click(saveAlarmButton);
    }

    public int getAlarmCount() {
        return getAlarms().size();
    }

    public String getAlarmTimeByPosition(String position) {
        String time = getAlarmByPosition(position).findElement(alarmTimeLabel).getText();
        return normalizeText(time);
    }

    public void openAlarmTimeByPosition(String position) {
        getAlarmByPosition(position).findElement(alarmTimeLabel).click();
    }

    public void deleteAlarmByPosition(String position) {
        getAlarmByPosition(position).findElement(expandAlarmButton).click();
        click(deleteAlarmButton);
    }

    public String[] splitTime(String time) {
        return time.replace(":", " ").split(" ");
    }

    public String normalizeText(String text) {
        return text
                .trim()
                .replaceAll("\\p{Z}+", " ");
    }

    private List<WebElement> getAlarms() {
        return find(alarmList).findElements(alarmCard);
    }

    private WebElement getAlarmByPosition(String position) {
        List<WebElement> alarms = getAlarms();
        int index = switch (position.toLowerCase()) {
            case "first" -> 0;
            case "second" -> 1;
            case "last" -> alarms.size() - 1;
            default -> throw new IllegalArgumentException("Invalid alarm position: " + position);
        };
        return alarms.get(index);
    }
}