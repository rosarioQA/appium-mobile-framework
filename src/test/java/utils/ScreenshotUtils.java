package utils;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static byte[] takeScreenshot(AppiumDriver driver, String scenarioName) {
        try {

            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            File destination = new File(
                    "target/screenshots/"
                            + sanitizeFileName(scenarioName)
                            + "_"
                            + timestamp
                            + ".png");
            File parent = destination.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IOException("Unable to create directory: " + parent.getAbsolutePath());
            }

            FileUtils.writeByteArrayToFile(destination, screenshot);

            return screenshot;

        } catch (IOException e) {
            throw new RuntimeException("Unable to save screenshot.", e);
        }
    }

    private static String sanitizeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "_");
    }
}