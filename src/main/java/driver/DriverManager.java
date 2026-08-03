package driver;
import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Stores the driver instance for the current thread.
     * @param driver AppiumDriver instance
     */
    public static void setDriver(AppiumDriver driver) {
        DRIVER.set(driver);
    }

    /**
     * Returns the driver associated with the current thread.
     * @return AppiumDriver instance
     * @throws IllegalStateException if the driver has not been initialized
     */
    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "Driver has not been initialized for the current thread.");
        }
        return driver;
    }

    /**
     * Closes the driver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        AppiumDriver driver = DRIVER.get();

        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}