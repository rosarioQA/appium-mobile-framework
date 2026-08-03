package pages;
import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class ClockMenuPage extends BasePage {

    private final By alarmTab = AppiumBy.id("com.google.android.deskclock:id/tab_menu_alarm");

    public ClockMenuPage() {
        super();
    }

    public void openAlarmTab() {
        click(alarmTab);
    }

    public boolean isOpened() {
        return isDisplayed(alarmTab);
    }
}