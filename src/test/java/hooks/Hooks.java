package hooks;

import driver.DriverFactory;
import io.cucumber.java.*;
import org.openqa.selenium.*;
import utils.ConfigReader;
import utils.ScreenshotUtil;
import utils.LoggerUtil;
import io.qameta.allure.Allure;
import utils.ExcelUtil;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static WebDriver driver;

    // ✅ Runs BEFORE EACH SCENARIO but opens browser ONLY ONCE
    @Before
    public void setUp() {

        if (DriverFactory.getDriver() == null) {

            String browser = ConfigReader.getProperty("browser");

            driver = DriverFactory.initDriver(browser);
            driver.get(ConfigReader.getProperty("url"));

            System.out.println("✅ Browser launched ONLY ONCE");
        } else {
            driver = DriverFactory.getDriver();
        }
    }

    // ✅ DO NOT CLOSE BROWSER HERE
    @After
    public void afterScenario() {
        System.out.println("✅ Scenario completed");
    }

    // ✅ CLOSE ONLY AFTER ALL 3 TESTCASES
    @AfterAll
    public static void tearDownAll() {

        ExcelUtil.saveExcel();

        if (driver != null) {
            driver.quit();   // ✅ close only once
        }

        System.out.println("✅ Browser closed AFTER ALL TEST CASES");
    }
}
