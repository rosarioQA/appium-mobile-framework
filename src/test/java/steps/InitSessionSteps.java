package steps;

import driver.DriverManager;
import io.cucumber.java.en.Given;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InitSessionSteps {

    @Given("an Appium session is started")
    public void anAppiumSessionIsStarted() {

        assertNotNull(
                DriverManager.getDriver(),
                "Driver should be initialized."
        );
    }
}