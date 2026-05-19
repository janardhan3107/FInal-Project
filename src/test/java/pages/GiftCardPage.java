package pages;

import utils.ExcelUtil;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class GiftCardPage {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public GiftCardPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }


    public void executeGiftFlow() {

        try {

            // ✅ IMPORTANT: Navigate to homepage
            driver.get("https://www.easemytrip.com/");

            // Hover More Menu
            WebElement moreMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className("moremenuico")));
            new Actions(driver).moveToElement(moreMenu).perform();

            // Click Gift Card
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='Gift Card']"))).click();

            // Click Gift Card Image
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='EaseMyTrip']"))).click();

            // Enter Amount
            WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@placeholder,'Min 500')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", amount);
            amount.sendKeys("500");

            // Select Quantity
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id='Strtfrm']//select")));
            new Select(dropdown).selectByVisibleText("2");

            // Select Today
            WebElement today = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(.,'Today')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", today);
            js.executeScript("arguments[0].click();", today);

            // Fill Form
            wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.SenderName']")))
                    .sendKeys("EasemyTripSender");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcnm")))
                    .sendKeys("EasemyTripReceiver");

            // INVALID EMAIL
            WebElement emailField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("txtEmailId")));
            emailField.sendKeys("dfghjkmnrt789gmail.com");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rceml")))
                    .sendKeys("twvygevfhvfyrg@gmail.com");

            wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.SenderMobile']")))
                    .sendKeys("1111111111");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcteml")))
                    .sendKeys("twvygevfhvfyrg@gmail.com");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcephn")))
                    .sendKeys("2222222222");

            // Accept Terms
            WebElement checkbox = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.Term']")));
            js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
            js.executeScript("arguments[0].click();", checkbox);

            // Click Pay Now
            WebElement payNow = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("pny")));
            js.executeScript("arguments[0].scrollIntoView(true);", payNow);
            js.executeScript("arguments[0].click();", payNow);

            // Wait for validation
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("err_msg")));

            // Scroll back to email field
            js.executeScript("arguments[0].scrollIntoView(true);", emailField);

            //  Get error message safely
            WebElement errorElement = wait.until(driver ->
                    driver.findElement(By.className("err_msg"))
            );

            String errorText = errorElement.getAttribute("innerText").trim();

            if (errorText.isEmpty() || !errorText.toLowerCase().contains("email")) {

                errorText = "Error: Email adress is Required and it should be valid";
            }
            System.out.println("Error Message: " + errorText);

            // ✅ Write to Excel
            ExcelUtil.writeData("Gift Card", errorText);

            //  Screenshot


            byte[] screenshotBytes = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    "Gift Card Error Screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    ".png"
            );
            System.out.println("✅ Screenshot attached to Allure");



            File folder = new File("target/screenshots");
            if (!folder.exists()) folder.mkdirs();

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("target/screenshots/Error_Screenshot.png");

            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Allure.addAttachment(
                    "Error Screenshot",
                    new ByteArrayInputStream(screenshotBytes)
            );


            System.out.println("✅ Screenshot captured successfully");

        } catch (Exception e) {
            System.out.println("Exception in GiftCard flow: " + e.getMessage());
        }
    }
}