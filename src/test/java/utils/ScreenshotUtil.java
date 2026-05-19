package utils;

import org.openqa.selenium.*;
import driver.DriverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static String captureScreenshot(String fileName) {

        WebDriver driver = DriverFactory.getDriver();

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Path where screenshot is saved
        String path = "target/screenshots/" + fileName + ".png";

        File dest = new File(path);

        try {
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Screenshot failed: " + e.getMessage());
        }

        // Return RELATIVE path for Extent Report (relative to report location)
        // Report is at: target/extent-reports/ExtentReport.html
        // Screenshot is at: target/screenshots/
        // So relative path is: ../screenshots/fileName.png
        return "../screenshots/" + fileName + ".png";
    }
}