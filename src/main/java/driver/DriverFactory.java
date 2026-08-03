package driver;
import config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a new driver session and stores it in DriverManager.
     * @return AppiumDriver instance
     */
    public static AppiumDriver createDriver() {
        AppiumDriver driver = createLocalDriver();
        DriverManager.setDriver(driver);
        return driver;
    }

    /**
     * Creates a local Appium driver.
     */
    private static AppiumDriver createLocalDriver() {
        String platform = ConfigManager.getProperty("platformName");
        return switch (platform.toLowerCase()) {
            case "android" -> createAndroidDriver();
            case "ios" -> createIOSDriver();
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }

    /**
     * Builds the UiAutomator2 options for Android execution.
     * @return UiAutomator2Options
     */
    private static UiAutomator2Options buildAndroidOptions() {

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(ConfigManager.getProperty("platformName"));
        options.setAutomationName(ConfigManager.getProperty("automationName"));
        options.setDeviceName(ConfigManager.getProperty("deviceName"));
        options.setAppPackage(ConfigManager.getProperty("appPackage"));
        options.setAppActivity(ConfigManager.getProperty("appActivity"));
        options.setNewCommandTimeout(Duration.ofSeconds(Long.parseLong(ConfigManager.getProperty("newCommandTimeout"))));

        return options;
    }

    /**
     * Creates an Android driver.
     * @return Android Appium driver
     */
    private static AppiumDriver createAndroidDriver() {
        UiAutomator2Options options = buildAndroidOptions();
        try {
            return new AndroidDriver(
                    URI.create(ConfigManager.getProperty("serverUrl")).toURL(),
                    options
            );

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL.", e);
        }
    }

    /**
     * Placeholder for future iOS implementation.
     */
    private static AppiumDriver createIOSDriver() {
        throw new UnsupportedOperationException(
                "iOS support has not been implemented yet.");
    }
}