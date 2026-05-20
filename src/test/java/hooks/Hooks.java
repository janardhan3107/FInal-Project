package hooks;

import driver.DriverFactory;
import io.cucumber.java.*;
import org.openqa.selenium.*;
import utils.ConfigReader;
import utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import utils.ExcelUtil;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static WebDriver driver;

    // BEFORE EACH SCENARIO - launch browser
    @Before
    public void setUp() {

        if (DriverFactory.getDriver() == null) {

            String browser = ConfigReader.getProperty("browser");

            driver = DriverFactory.initDriver(browser);
            driver.get(ConfigReader.getProperty("url"));

            System.out.println("Browser launched ONLY ONCE");
        } else {
            driver = DriverFactory.getDriver();
        }
    }

    // AFTER EACH SCENARIO
    @After
    public void afterScenario(Scenario scenario) {

        try {

            //  ONLY for Gift Card Test Case
            if (scenario.getName().equalsIgnoreCase("Gift Card Test Case")) {

                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                //  ALLURE
                Allure.addAttachment(
                        scenario.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                );

                //  CUCUMBER
                scenario.attach(screenshot, "image/png", "Gift Card Screenshot");

                //  EXTENT (save file)
                String path = ScreenshotUtil.captureScreenshot(scenario.getName());
                System.out.println("Screenshot saved at: " + path);

                System.out.println("Screenshot captured ONLY for Gift Card scenario");
            }

        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }

        System.out.println("Scenario completed");
    }

    // AFTER ALL SCENARIOS
    @AfterAll
    public static void tearDownAll() {

        ExcelUtil.saveExcel();

        if (driver != null) {
            driver.quit();
        }

        System.out.println("Browser closed AFTER ALL TEST CASES");
    }
}