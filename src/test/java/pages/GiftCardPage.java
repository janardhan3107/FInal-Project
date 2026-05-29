package pages;

import utils.ExcelUtil;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

public class GiftCardPage extends BasePage {

    // Logger initialization
    Logger log = Logger.getLogger(GiftCardPage.class);

    // -------------------- PAGE FACTORY LOCATORS --------------------

    @FindBy(className = "moremenuico")
    WebElement moreMenu;

    @FindBy(xpath = "//span[contains(text(),'Gift Card')]")
    WebElement giftCardOption;

    @FindBy(xpath = "//img[@alt='EaseMyTrip']")
    WebElement giftCardImage;

    @FindBy(xpath = "//input[contains(@placeholder,'Min 500')]")
    WebElement amountField;

    @FindBy(xpath = "//*[@id='Strtfrm']//select")
    WebElement quantityDropdown;

    @FindBy(xpath = "//label[contains(.,'Today')]")
    WebElement todayOption;

    @FindBy(xpath = "//input[@ng-model='User.SenderName']")
    WebElement senderNameField;

    @FindBy(id = "rcnm")
    WebElement receiverNameField;

    @FindBy(id = "txtEmailId")
    WebElement emailField;

    @FindBy(id = "rceml")
    WebElement receiverEmailField;

    @FindBy(xpath = "//input[@ng-model='User.SenderMobile']")
    WebElement senderMobileField;

    @FindBy(id = "rcteml")
    WebElement receiverEmailConfirm;

    @FindBy(id = "rcephn")
    WebElement receiverMobileField;

    @FindBy(xpath = "//input[@ng-model='User.Term']")
    WebElement termsCheckbox;

    @FindBy(id = "pny")
    WebElement payNowButton;

    @FindBy(className = "err_msg")
    WebElement errorMessage;

    // -------------------- CONSTRUCTOR --------------------

    public GiftCardPage(WebDriver driver) {
        super(driver);
    }

    // -------------------- ACTION METHODS --------------------

    // Hover on More menu
    public void hoverMoreMenu() {

        wait.until(ExpectedConditions.visibilityOf(moreMenu));

        Actions actions = new Actions(driver);
        actions.moveToElement(moreMenu).pause(2000).perform();

        log.info("Hovered on More menu");
    }

    // Click Gift Card option (FIXED METHOD)
    public void clickGiftCard() {

        try {

            // Wait for dropdown/menu to appear after hover
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(text(),'Gift Card')]")));


            // Scroll to element
            js.executeScript("arguments[0].scrollIntoView(true);", giftCardOption);

            // Wait until clickable
            wait.until(ExpectedConditions.elementToBeClickable(giftCardOption));

            // Use JS click (more reliable for hidden/intercepted elements)
            js.executeScript("arguments[0].click();", giftCardOption);

            log.info("Clicked Gift Card option");

        } catch (Exception e) {

            log.warn("Retrying Gift Card click using alternative locator");

            WebElement altElement = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[contains(text(),'Gift Card')] | //span[contains(text(),'Gift Card')]")
                    )
            );

            js.executeScript("arguments[0].click();", altElement);
        }
    }

    // Select Gift Card image
    public void selectGiftCard() {
        wait.until(ExpectedConditions.elementToBeClickable(giftCardImage)).click();
        log.info("Selected Gift Card image");
    }

    // Enter amount
    public void enterAmount(String amount) {
        js.executeScript("arguments[0].scrollIntoView(true);", amountField);
        amountField.sendKeys(amount);
        log.info("Entered amount: " + amount);
    }

    // Select quantity
    public void selectQuantity(String quantity) {
        new Select(quantityDropdown).selectByVisibleText(quantity);
        log.info("Selected quantity: " + quantity);
    }

    // Select Today
    public void selectToday() {
        js.executeScript("arguments[0].click();", todayOption);
        log.info("Selected Today option");
    }

    // Fill user details
    public void fillDetails(String senderName, String receiverName,
                            String invalidEmail, String receiverEmail,
                            String senderMobile, String receiverMobile) {

        senderNameField.sendKeys(senderName);
        log.info("Entered sender name");

        receiverNameField.sendKeys(receiverName);
        log.info("Entered receiver name");

        emailField.sendKeys(invalidEmail);
        log.warn("Entered invalid email");

        receiverEmailField.sendKeys(receiverEmail);
        senderMobileField.sendKeys(senderMobile);
        receiverEmailConfirm.sendKeys(receiverEmail);
        receiverMobileField.sendKeys(receiverMobile);

        log.info("Filled all recipient and contact details");
    }

    // Accept terms
    public void acceptTerms() {
        js.executeScript("arguments[0].click();", termsCheckbox);
        log.info("Accepted terms and conditions");
    }

    // Click Pay Now
    public void clickPayNow() {
        js.executeScript("arguments[0].scrollIntoView(true);", payNowButton);
        js.executeScript("arguments[0].click();", payNowButton);
        log.info("Clicked Pay Now button");
    }

    // Capture validation and screenshot
    public void captureValidation() throws Exception {

        log.info("Waiting for validation error message");

        WebElement errorElement = wait.until(
                ExpectedConditions.visibilityOf(errorMessage));

        js.executeScript("arguments[0].scrollIntoView(true);", emailField);

        wait.until(ExpectedConditions.visibilityOf(emailField));

        String errorText = errorElement.getText().trim();

        if (errorText.isEmpty() || !errorText.toLowerCase().contains("email")) {
            errorText = "Error: Email address is required and it should be valid";
        }

        log.error("Validation error: " + errorText);

        ExcelUtil.writeData("Gift Card", errorText);

        // Take screenshot
        byte[] screenshotBytes = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(
                "Gift Card Validation Screenshot",
                "image/png",
                new ByteArrayInputStream(screenshotBytes),
                ".png"
        );

        log.info("Screenshot attached to Allure report");

        File dest = new File("target/screenshots/Error_Screenshot.png");
        dest.getParentFile().mkdirs();

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        log.info("Screenshot saved for Extent report");
    }

    // -------------------- MAIN FLOW METHOD --------------------

    public void executeGiftFlow() {

        try {

            log.info("Starting Gift Card flow");

            String amountData = ExcelUtil.getData("Sheet1", 1, 0);
            String quantityData = ExcelUtil.getData("Sheet1", 1, 1);
            String senderName = ExcelUtil.getData("Sheet1", 1, 2);
            String receiverName = ExcelUtil.getData("Sheet1", 1, 3);
            String invalidEmail = ExcelUtil.getData("Sheet1", 1, 4);
            String receiverEmail = ExcelUtil.getData("Sheet1", 1, 5);
            String senderMobile = ExcelUtil.getData("Sheet1", 1, 6);
            String receiverMobile = ExcelUtil.getData("Sheet1", 1, 7);

            driver.get("https://www.easemytrip.com/");
            log.info("Navigated to homepage");

            hoverMoreMenu();
            clickGiftCard();
            selectGiftCard();
            enterAmount(amountData);
            selectQuantity(quantityData);
            selectToday();
            fillDetails(senderName, receiverName, invalidEmail,
                    receiverEmail, senderMobile, receiverMobile);
            acceptTerms();
            clickPayNow();
            captureValidation();

        } catch (Exception e) {

            log.error("Exception in GiftCard flow: " + e.getMessage());

            String errorText = "Error: Email address is required and it should be valid";
            ExcelUtil.writeData("Gift Card", errorText);
        }
    }
}
