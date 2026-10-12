package pages;

import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class ClockPage extends BasePage {

    private static final String CITY_LABEL_ID = "com.google.android.deskclock:id/city_name";

    private final By addTimezoneButton = AppiumBy.id("com.google.android.deskclock:id/fab");
    private final By cityLabel = AppiumBy.id(CITY_LABEL_ID);

    private final By searchInput = AppiumBy.id("com.google.android.deskclock:id/open_search_view_edit_text");
    private final By searchEmptyLabel = AppiumBy.id("com.google.android.deskclock:id/search_empty_view");

    public ClockPage() {
        super();
    }

    public void tapAddTimezone() {
        click(addTimezoneButton);
        find(searchEmptyLabel);
    }

    public void searchTimezone(String timezone) {
        click(searchInput);
        type(searchInput, timezone);
    }

    public void selectResultStartingWith(String timezone) {
        click(cityLabelStartingWith(timezone + ","));
    }

    public boolean isTimezoneDisplayed(String timezone) {
        find(cityLabel);
        if (!driver.findElements(cityLabelStartingWith(timezone)).isEmpty()) {
            return true;
        }
        return !driver.findElements(scrollToCityLabelStartingWith(timezone)).isEmpty();
    }

    private By cityLabelStartingWith(String prefix) {
        return AppiumBy.androidUIAutomator(citySelector(prefix));
    }

    private By scrollToCityLabelStartingWith(String prefix) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(" + citySelector(prefix) + ")");
    }

    private String citySelector(String prefix) {
        return "new UiSelector().resourceId(\"" + CITY_LABEL_ID + "\").textStartsWith(\"" + prefix + "\")";
    }
}