package core;

import driver.DriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitFactory {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private WaitFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a WebDriverWait using the default timeout.
     *
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                DEFAULT_TIMEOUT
        );
    }

    /**
     * Returns a WebDriverWait using a custom timeout.
     *
     * @param timeout timeout duration
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait(Duration timeout) {
        return new WebDriverWait(
                DriverManager.getDriver(),
                timeout
        );
    }
}