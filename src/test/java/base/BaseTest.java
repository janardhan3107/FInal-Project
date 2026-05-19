package base;

import driver.DriverFactory;
import org.openqa.selenium.WebDriver;

public class BaseTest {

    protected WebDriver driver;

    public void setup(String browser) {
        driver = DriverFactory.initDriver(browser);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void tearDown() {
        DriverFactory.quitDriver();
    }
}