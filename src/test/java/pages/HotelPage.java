package pages;

import utils.ExcelUtil;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.*;

public class HotelPage {

    WebDriver driver;
    WebDriverWait wait;

    public HotelPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void getAdultList() {

        try {

            // ✅ IMPORTANT → Navigate to homepage
            driver.get("https://www.easemytrip.com/");

            // Click hotel menu
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span.hotelmenuico"))).click();

            // Open guest dropdown
            WebElement arrow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'roomGuests')]//i[contains(@class,'down_arw_htl')]")));
            arrow.click();

            // Locate elements
            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("Adults_room_1_1_plus")));

            WebElement number = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("Adults_room_1_1")));

            List<Integer> adultList = new ArrayList<>();

            // ✅ SAFE INITIAL VALUE
            String text = number.getText().trim();
            int currentValue = text.isEmpty() ? 1 : Integer.parseInt(text);
            adultList.add(currentValue);

            // ✅ LOOP UNTIL MAX VALUE
            while (true) {

                String beforeText = number.getText().trim();

                int before = beforeText.isEmpty() ? currentValue : Integer.parseInt(beforeText);

                plusBtn.click();

                try {
                    wait.until(ExpectedConditions.not(
                            ExpectedConditions.textToBePresentInElement(number, String.valueOf(before))
                    ));
                } catch (TimeoutException e) {
                    break; // reached max
                }

                String afterText = number.getText().trim();

                // ✅ SAFE CHECK
                if (afterText.isEmpty()) break;

                int after = Integer.parseInt(afterText);

                if (after == before) break;

                adultList.add(after);
                currentValue = after;
            }

            // ✅ OUTPUT
            System.out.println("✅ Adult Counts: " + adultList);

            // ✅ Write result to Excel
            ExcelUtil.writeData("Hotel", adultList.toString());

            // ✅ Allure logging
            Allure.addAttachment("Hotel Adult Counts", adultList.toString());

        } catch (Exception e) {
            System.out.println("❌ Exception in Hotel flow: " + e.getMessage());
        }
    }
}