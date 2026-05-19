package hooks;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.Status;
import driver.DriverFactory;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import reports.ExtentManager;
import utils.ScreenshotUtil;

public class ExtentHooks {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Before
    public void startTest(Scenario scenario) {
        ExtentTest extentTest = extent.createTest(scenario.getName());
        test.set(extentTest);
    }

    @After
    public void endTest(Scenario scenario) {
        // Check if test failed
        if (scenario.isFailed()) {
            // Capture screenshot on failure
            String screenshotPath = ScreenshotUtil.captureScreenshot(
                    scenario.getName() + "_" + System.currentTimeMillis()
            );

            // Attach to Extent Report
            test.get().fail("Test Failed")
                    .addScreenCaptureFromPath(screenshotPath);

            test.get().log(Status.FAIL, "Screenshot: " + screenshotPath);
        } else if (scenario.getStatus().toString().equals("PASSED")) {
            test.get().log(Status.PASS, "Test Passed");
        }

        extent.flush();
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}