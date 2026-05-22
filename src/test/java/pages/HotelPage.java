package pages;

import utils.ExcelUtil;
import io.qameta.allure.Allure;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.util.*;

import org.apache.log4j.Logger;

public class HotelPage extends BasePage {

    // Logger initialization
    Logger log = Logger.getLogger(HotelPage.class);

    // -------------------- PAGE FACTORY LOCATORS --------------------

    @FindBy(css = "span.hotelmenuico")
    WebElement hotelMenu;

    @FindBy(xpath = "//div[contains(@class,'roomGuests')]//i[contains(@class,'down_arw_htl')]")
    WebElement guestDropdownArrow;

    @FindBy(id = "Adults_room_1_1_plus")
    WebElement plusButton;

    @FindBy(id = "Adults_room_1_1")
    WebElement adultCountText;

    // -------------------- CONSTRUCTOR --------------------

    public HotelPage(WebDriver driver) {
        super(driver);
    }

    // -------------------- ACTION METHODS --------------------

    // Navigate to homepage
    public void navigateToHome() {
        driver.get("https://www.easemytrip.com/");
        log.info("Navigated to homepage");
    }

    // Click hotel menu
    public void clickHotelMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(hotelMenu)).click();
        log.info("Clicked Hotel menu");
    }

    // Open guest dropdown
    public void openGuestDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(guestDropdownArrow)).click();
        log.info("Opened guest dropdown");
    }

    // Extract adult list
    public List<Integer> extractAdultCounts() {

        log.info("Located adult selector");

        List<Integer> adultList = new ArrayList<>();

        // Safe initial value
        String text = wait.until(ExpectedConditions.visibilityOf(adultCountText)).getText().trim();
        int currentValue = text.isEmpty() ? 1 : Integer.parseInt(text);

        adultList.add(currentValue);
        log.info("Initial adult count: " + currentValue);

        // Loop until max value
        while (true) {

            String beforeText = adultCountText.getText().trim();
            int before = beforeText.isEmpty() ? currentValue : Integer.parseInt(beforeText);

            plusButton.click();

            try {
                wait.until(ExpectedConditions.not(
                        ExpectedConditions.textToBePresentInElement(adultCountText, String.valueOf(before))
                ));
            } catch (TimeoutException e) {
                log.warn("Reached maximum adult count limit");
                break;
            }

            String afterText = adultCountText.getText().trim();

            if (afterText.isEmpty()) break;

            int after = Integer.parseInt(afterText);

            if (after == before) break;

            adultList.add(after);
            currentValue = after;

            log.info("Adult count increased to: " + after);
        }

        return adultList;
    }

    // -------------------- MAIN FLOW METHOD --------------------

    public void getAdultList() {

        try {

            log.info("Starting Hotel adult extraction flow");

            // Execute steps
            navigateToHome();
            clickHotelMenu();
            openGuestDropdown();

            List<Integer> adultList = extractAdultCounts();

            // Output
            log.info("Final Adult Counts: " + adultList);
            System.out.println("Adult Counts: " + adultList);

            // Write result to Excel
            ExcelUtil.writeData("Hotel", adultList.toString());
            log.info("Hotel data written to Excel");

            // Allure logging
            Allure.addAttachment("Hotel Adult Counts", adultList.toString());

        } catch (Exception e) {
            log.error("Exception in Hotel flow: " + e.getMessage());
        }
    }
}