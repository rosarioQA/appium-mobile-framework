package pages;
import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import java.util.Objects;

public class MenuPage extends BasePage {

    private final By alarmTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_alarm");
    private final By clockTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_clock");
    private final By timerTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_timer");
    private final By stopwatchTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_stopwatch");
    private final By bedTimeTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_bedtime");
    private final By sectionTitle = AppiumBy.id("com.google.android.deskclock:id/action_bar_title");

    public MenuPage() {
        super();
    }

    public void openAlarmTab() {
        click(alarmTab);
    }

    public void openClockTab() {
        click(clockTab);
    }

    public void openTimerTab() {
        click(timerTab);
    }

    public void openStopwatchTab() {
        click(stopwatchTab);
    }

    public void openBedtimeTab() {
        click(bedTimeTab);
    }

    public boolean isOpened() {
        return isDisplayed(alarmTab);
    }

    public boolean isTitleDisplayed(String title) {

        return Objects.equals(getText(sectionTitle), title);
    }
}