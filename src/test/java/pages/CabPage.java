package pages;
import utils.ExcelUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class CabPage {

    WebDriver driver;
    WebDriverWait wait;

    public CabPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void bookCab() {

        String reqMon="December 2026";

        WebElement cabsTab=wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cabs")));
        cabsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//label[normalize-space()='Outstation']")))).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("sourceName")))).click();
        WebElement fromCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_FromSector_show")));
        fromCity.sendKeys("Delhi");
        boolean clicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement FromCityOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='auto_sugg_tttl' and contains(normalize-space(),'delhi')]")));
                FromCityOption.click();
                clicked = true;
                break;
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element, retrying... attempt " + (retry + 1));
            }
        }
        WebElement toCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_ToSector_shows")));
        toCity.sendKeys("Manali");
        boolean toClicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement ToCityOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='auto_sugg_tttl' and starts-with(normalize-space(),'manali')]")));
                ToCityOption.click();
                toClicked = true;
                break;
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element for ToCity, retrying... attempt " + (retry + 1));
            }
        }
        WebElement datePicker=wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("datepicker"))));
        datePicker.click();
        while(true){
            WebElement month=wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-datepicker-title")));
            if(!month.getText().equals(reqMon)){
                driver.findElement(By.xpath("//a[@data-handler='next']")).click();
            }
            else{
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='23']"))).click();
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='AM']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[text()='6 Hr.']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[text()='30 Min.']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.className("done_d"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.className("srch-btn-c"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='suv']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Any']"))).click();
        WebElement price=driver.findElement(By.xpath("//div[@class='nw_price']/div[2]"));
        WebElement carName=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_pro_ttl']")));
        System.out.println("********** Available Suv Cabs **********");
        System.out.println("Car : "+carName.getText()+"-> "+price.getText());
    }
}



