package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.IOException;

public class ExtentManager {

    private static ExtentReports extent;
    public static ExtentTest test;

    public static ExtentReports getInstance() {

        if (extent == null) {

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("target/extent-reports/ExtentReport.html");

            try {
                spark.loadXMLConfig("extent-config.xml");
            } catch (IOException e) {
                System.out.println("Failed to load extent config: " + e.getMessage());
            }

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "EaseMyTrip Automation");
            extent.setSystemInfo("Tester", "Automation User");
            extent.setSystemInfo("Environment", "QA");
        }

        return extent;
    }
}