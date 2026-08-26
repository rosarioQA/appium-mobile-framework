package pages;
import core.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ClockPage extends BasePage {

    public final By addTimezoneButton = AppiumBy.id("com.google.android.deskclock:id/fab");
    public final By searchEmptyResults = AppiumBy.id("com.google.android.deskclock:id/search_empty_view");
    public final By searchInput = AppiumBy.id("com.google.android.deskclock:id/open_search_view_edit_text");
    private final By city = AppiumBy.id("com.google.android.deskclock:id/city_name");
    private final By searchResults = AppiumBy.xpath("//android.support.v7.widget.RecyclerView[@resource-id='com.google.android.deskclock:id/search_results_view']/android.widget.LinearLayout");

    public ClockPage() {
        super();
    }

    public void tapAddTimezone() {
        click(addTimezoneButton);
        waitUntilVisible(searchEmptyResults);
    }
    public void searchTimezone(String timezone) {
        click(searchInput);
        type(searchInput, timezone);
    }

    public void selectResultByPosition(int position) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchResults, position));
        List<WebElement> results = driver.findElements(searchResults);
        wait.until(ExpectedConditions.elementToBeClickable(results.get(position))).click();
    }

    public String getLastAddedTimezone() {
        List<WebElement> timezones =  driver.findElements(city);
        WebElement lastCityAdded = timezones.get(timezones.size() -1);
        return lastCityAdded.getText();
    }
}