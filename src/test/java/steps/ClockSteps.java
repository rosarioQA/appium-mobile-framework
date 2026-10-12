package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import pages.ClockPage;
import pages.MenuPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClockSteps {
    private final MenuPage menuPage = new MenuPage();
    private final ClockPage clockPage = new ClockPage();

    @Then("the Clock screen is displayed")
    public void alarmScreenDisplayed() {
        assertTrue(menuPage.isTitleDisplayed("Clock"));
    }

    @And("Add {string} to the list of timezones")
    public void addNewTimezone(String timezone) {
        clockPage.tapAddTimezone();
        clockPage.searchTimezone(timezone);
        clockPage.selectResultStartingWith(timezone);
    }

    @Then("the timezone {string} is displayed")
    public void theTimezoneIsAdded(String timezone) {
        assertTrue(clockPage.isTimezoneDisplayed(timezone),
                "No city starting with '" + timezone + "' was found in the Clock list");
    }
}
