package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BasePage {

    // Protected so all child pages can use
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    // -------------------- CONSTRUCTOR --------------------
    public BasePage(WebDriver driver) {

        this.driver = driver;

        // Initialize PageFactory elements for child classes
        PageFactory.initElements(driver, this);

        // Initialize wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Initialize JavaScript executor
        js = (JavascriptExecutor) driver;
    }
}
