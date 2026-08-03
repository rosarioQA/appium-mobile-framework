package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ClockMenuPage;
import pages.AlarmPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClockSteps {
    private final ClockMenuPage clockPage = new ClockMenuPage();
    private final AlarmPage alarmPage = new AlarmPage();

    @Given("Clock application is open")
    public void clockApplicationIsOpen() {
        assertTrue(clockPage.isOpened());
    }

    @When("I open the Alarm tab")
    public void openAlarmTab() {
        clockPage.openAlarmTab();
    }

    @When("Add new alarm")
    public void createNewAlarm() {
        alarmPage.tapAddAlarm();
        alarmPage.selectHour(8);
        alarmPage.selectMinute(10);
        alarmPage.saveAlarm();
    }

    @Then("the Alarm screen is displayed")
    public void alarmScreenDisplayed() {
        assertTrue(alarmPage.isAddAlarmButtonDisplayed());
    }

    @Then("the Alarm is added")
    public void alarmAdded() {
        assertEquals( "8:10", alarmPage.getLastAlarmTime());
    }
}