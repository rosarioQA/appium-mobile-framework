package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.MenuPage;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps {
    private final MenuPage menuPage = new MenuPage();

    @Given("the Clock application is open")
    public void clockApplicationIsOpen() {
        assertTrue(menuPage.isOpened());
    }

    @When("I open the Alarm tab")
    public void openAlarmTab() {
        menuPage.openAlarmTab();
    }

    @When("I open the Clock tab")
    public void openClockTab() {
        menuPage.openClockTab();
    }

    @When("I open the Timer tab")
    public void openTimerTab() {
        menuPage.openTimerTab();
    }

    @When("I open the Stopwatch tab")
    public void openStopwatchTab() {
        menuPage.openStopwatchTab();
    }

    @When("I open the Bedtime tab")
    public void openBedtimeTab() {
        menuPage.openBedtimeTab();
    }
}
