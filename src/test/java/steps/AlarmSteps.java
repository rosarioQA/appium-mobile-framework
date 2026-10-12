package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AlarmPage;
import pages.MenuPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlarmSteps {
    private final MenuPage menuPage = new MenuPage();
    private final AlarmPage alarmPage = new AlarmPage();

    @When("I add a new alarm with {string}")
    public void addNewAlarmWith(String newTime) {
        alarmPage.tapAddAlarm();
        String[] result = alarmPage.splitTime(newTime);
        alarmPage.selectHour(result[0]);
        alarmPage.selectMinutes(result[1]);
        alarmPage.setBeforeOrAfterNoon(result[2]);
        alarmPage.saveAlarm();
    }

    @Then("the Alarm screen is displayed")
    public void alarmScreenDisplayed() {
        assertTrue(menuPage.isTitleDisplayed("Alarm"));
        assertTrue(alarmPage.isAddAlarmButtonDisplayed());
    }

    @Then("a {string} alarm is added and displays {string}")
    public void theAlarmIsAddedAndDisplays(String position, String expectedTime) {
        assertEquals( expectedTime, alarmPage.getAlarmTimeByPosition(position));
    }

    @Then("the {string} alarm displays {string}")
    public void theAlarmDisplay(String position, String newTime) {
        assertEquals(alarmPage.normalizeText(newTime), alarmPage.getAlarmTimeByPosition(position));
    }

    @And("I delete the last alarm")
    public void deleteAlarm() {
        alarmPage.deleteAlarmByPosition("last");
    }

    @And("^the Alarm tab displays (\\d+) alarm(s)?$")
    public void iOpenTheAlarmTabWithAlarmsDisplayed(int expectedAlarms, String _multipleAlarms) {
        int alarmsCount = alarmPage.getAlarmCount();
        assertEquals(alarmsCount, expectedAlarms);
    }

    @And("I set the {string} alarm to {string}")
    public void setTheFirstAlarmWith(String position, String newTime) {
        alarmPage.openAlarmTimeByPosition(position);
        String[] result = alarmPage.splitTime(newTime);
        alarmPage.selectHour(result[0]);
        alarmPage.selectMinutes(result[1]);
        alarmPage.setBeforeOrAfterNoon(result[2]);
        alarmPage.saveAlarm();
    }
}
