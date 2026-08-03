package hooks;

import driver.DriverFactory;
import driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ScreenshotUtils;
import utils.AllureUtils;

public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.createDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        AppiumDriver driver = DriverManager.getDriver();

        if (scenario.isFailed()) {
            byte[] screenshot = ScreenshotUtils.takeScreenshot(
                    DriverManager.getDriver(),
                    scenario.getName());

            AllureUtils.attachScreenshot(screenshot);
            AllureUtils.attachCapabilities(driver);
            AllureUtils.attachEnvironment();
        }
        DriverManager.quitDriver();
    }
}