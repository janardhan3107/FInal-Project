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
import org.apache.log4j.Logger;
public class GiftCardPage {
    // Logger initialization
    Logger log = Logger.getLogger(GiftCardPage.class);
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
            log.info("Starting Gift Card flow");
            // Navigate to homepage
            driver.get("https://www.easemytrip.com/");
            log.info("Navigated to homepage");
            // Hover More Menu
            WebElement moreMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className("moremenuico")));
            new Actions(driver).moveToElement(moreMenu).perform();
            log.info("Hovered on More menu");
            // Stable click for Gift Card
            By giftCard = By.xpath("//span[text()='Gift Card']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(giftCard));
            WebElement giftElement = wait.until(
                    ExpectedConditions.elementToBeClickable(giftCard));
            js.executeScript("arguments[0].scrollIntoView(true);", giftElement);
            giftElement.click();
            log.info("Clicked Gift Card option");
            // Click Gift Card Image
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='EaseMyTrip']"))).click();
            log.info("Selected Gift Card image");
            // Enter Amount
            WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@placeholder,'Min 500')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", amount);
            amount.sendKeys(ExcelUtil.getData("Sheet1", 1, 0));
            log.info("Entered amount: 500");
            // Select Quantity
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id='Strtfrm']//select")));
            new Select(dropdown).selectByVisibleText(
                    ExcelUtil.getData("Sheet1", 1, 1)
            );
            log.info("Selected quantity: 2");
            // Select Today
            WebElement today = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(.,'Today')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", today);
            js.executeScript("arguments[0].click();", today);
            log.info("Selected Today option");
            // Fill Form
            wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.SenderName']")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 2));;
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcnm")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 3));
            log.info("Entered sender and receiver details");
            // Invalid Email
            WebElement emailField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("txtEmailId")));
            emailField.sendKeys(ExcelUtil.getData("Sheet1", 1, 4));            log.warn("Entered invalid email");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rceml")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 5));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.SenderMobile']")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 6));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcteml")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rcephn")))
                    .sendKeys(ExcelUtil.getData("Sheet1", 1, 7));
            // Accept Terms
            WebElement checkbox = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@ng-model='User.Term']")));
            js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
            js.executeScript("arguments[0].click();", checkbox);
            log.info("Accepted terms");
            // Click Pay Now
            WebElement payNow = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("pny")));
            js.executeScript("arguments[0].scrollIntoView(true);", payNow);
            js.executeScript("arguments[0].click();", payNow);
            log.info("Clicked Pay Now");
            // Wait for validation message
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("err_msg")));
            js.executeScript("arguments[0].scrollIntoView(true);", emailField);
            // Capture error message
            WebElement errorElement = wait.until(driver ->
                    driver.findElement(By.className("err_msg"))
            );
            String errorText = errorElement.getAttribute("innerText").trim();
            if (errorText.isEmpty() || !errorText.toLowerCase().contains("email")) {
                errorText = "Error: Email adress is Required and it should be valid";
            }
            log.error("Validation error: " + errorText);
            // Write to Excel
            ExcelUtil.writeData("Gift Card", errorText);
            log.info("Written result to Excel");
            // Screenshot for Allure
            byte[] screenshotBytes = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(
                    "Gift Card Error",
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    ".png"
            );
            log.info("Screenshot attached to Allure");
            // Save screenshot locally
            File folder = new File("target/screenshots");
            if (!folder.exists()) folder.mkdirs();
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("target/screenshots/Error_Screenshot.png");
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("Screenshot saved locally");
        } catch (Exception e) {
            log.error("Exception in GiftCard flow: " + e.getMessage());
        }
    }
}