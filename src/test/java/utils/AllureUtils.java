package utils;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.util.Map;

import io.appium.java_client.AppiumDriver;


public final class AllureUtils {

    private AllureUtils() {
    }

    public static void attachScreenshot(byte[] screenshot) {
        Allure.addAttachment(
                "Screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }

    public static void attachText(String title, String text) {
        Allure.addAttachment(
                title,
                "text/plain",
                text
        );
    }

    public static void attachJson(String title, String json) {
        Allure.addAttachment(
                title,
                "application/json",
                json
        );
    }

    public static void attachCapabilities(AppiumDriver driver) {
        Map<String, Object> capabilities = driver.getCapabilities().asMap();

        StringBuilder sb = new StringBuilder();

        addCapability(sb, "Platform", capabilities, "platformName");
        addCapability(sb, "Android Version", capabilities, "appium:platformVersion", "platformVersion");
        addCapability(sb, "Device", capabilities, "appium:deviceName", "deviceName");
        addCapability(sb, "Automation", capabilities, "appium:automationName", "automationName");
        addCapability(sb, "App Package", capabilities, "appium:appPackage", "appPackage");
        addCapability(sb, "App Activity", capabilities, "appium:appActivity", "appActivity");
        attachText("Capabilities", sb.toString());
    }

    private static void addCapability(StringBuilder sb, String label, Map<String, Object> capabilities, String... keys) {
        for (String key : keys) {
            Object value = capabilities.get(key);
            if (value != null) {
                sb.append(String.format("%-18s : %s%n", label, value));
                return;
            }
        }
    }

    public static void attachEnvironment() {
        String environment = """
            OS: %s
            Java: %s
            User: %s
            """.formatted(
                System.getProperty("os.name"),
                System.getProperty("java.version"),
                System.getProperty("user.name")
        );
        attachText("Environment", environment);
    }
}