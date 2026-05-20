package pages;

import utils.ExcelUtil;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.*;
import org.apache.log4j.Logger;

public class HotelPage {

    // Logger initialization
    Logger log = Logger.getLogger(HotelPage.class);

    WebDriver driver;
    WebDriverWait wait;

    public HotelPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void getAdultList() {

        try {

            log.info("Starting Hotel adult extraction flow");

            // Navigate to homepage
            driver.get("https://www.easemytrip.com/");
            log.info("Navigated to homepage");

            // Click hotel menu
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span.hotelmenuico"))).click();
            log.info("Clicked Hotel menu");

            // Open guest dropdown
            WebElement arrow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'roomGuests')]//i[contains(@class,'down_arw_htl')]")));
            arrow.click();
            log.info("Opened guest dropdown");

            // Locate elements
            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("Adults_room_1_1_plus")));

            WebElement number = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("Adults_room_1_1")));

            log.info("Located adult selector");

            List<Integer> adultList = new ArrayList<>();

            // Safe initial value
            String text = number.getText().trim();
            int currentValue = text.isEmpty() ? 1 : Integer.parseInt(text);
            adultList.add(currentValue);

            log.info("Initial adult count: " + currentValue);

            // Loop until max value
            while (true) {

                String beforeText = number.getText().trim();
                int before = beforeText.isEmpty() ? currentValue : Integer.parseInt(beforeText);

                plusBtn.click();

                try {
                    wait.until(ExpectedConditions.not(
                            ExpectedConditions.textToBePresentInElement(number, String.valueOf(before))
                    ));
                } catch (TimeoutException e) {
                    log.warn("Reached maximum adult count limit");
                    break;
                }

                String afterText = number.getText().trim();

                if (afterText.isEmpty()) break;

                int after = Integer.parseInt(afterText);

                if (after == before) break;

                adultList.add(after);
                currentValue = after;

                log.info("Adult count increased to: " + after);
            }

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
