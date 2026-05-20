package pages;

import org.apache.log4j.Logger;
import utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class CabPage extends  ConfigReader {
    Logger log = Logger.getLogger(CabPage.class);
    WebDriver driver;
    WebDriverWait wait;

    public CabPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void bookCab() {
        log.info("Starting Cab booking flow");
        String reqMon=prop.getProperty("reqMonth");
        log.info("Clicked on cabs tab");
        WebElement cabsTab=wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cabs")));
        cabsTab.click();
        log.info("Selected Outstation tab");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//label[normalize-space()='Outstation']")))).click();
        log.info("Entering Source City");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("sourceName")))).click();
        WebElement fromCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_FromSector_show")));
        log.info("Getting From City name from config file");
        fromCity.sendKeys(prop.getProperty("fromCity"));
        boolean clicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement FromCityOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='auto_sugg_tttl' and contains(normalize-space(),'delhi')]")));
                FromCityOption.click();
                clicked = true;
                log.info("From City Selected from auto-suggest options");
                break;
            } catch (StaleElementReferenceException e) {
                log.info("Stale element, retrying... attempt " + (retry + 1));
            }
        }
        log.info("Getting destination city name from config file");
        WebElement toCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_ToSector_show")));
        toCity.sendKeys(prop.getProperty("toCity"));
        boolean toClicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement ToCityOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='auto_sugg_tttl' and starts-with(normalize-space(),'manali')]")));
                ToCityOption.click();
                toClicked = true;
                log.info("Successfully selected To City from auto-suggest options");
                break;
            } catch (StaleElementReferenceException e) {
                log.info("Stale element for ToCity, retrying... attempt " + (retry + 1));
            }
        }
        log.info("Selecting the required date (Dec 23) from date picker");
        WebElement datePicker=wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("datepicker"))));
        datePicker.click();
        log.info("Navigating until the required month is visible in the date picker");
        while(true){
            WebElement month=wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-datepicker-title")));
            if(!month.getText().equals(reqMon)){
                driver.findElement(By.xpath("//a[@data-handler='next']")).click();

            }
            else{
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='23']"))).click();
                log.info("Successfully selected the required date");
                break;
            }
        }
        log.info("Selecting pickup time as 6:30 AM");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='AM']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[text()='6 Hr.']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[text()='30 Min.']"))).click();
        log.info("Clicked on done after selecting time");
        wait.until(ExpectedConditions.elementToBeClickable(By.className("done_d"))).click();
        log.info("Clicked search button to view available cabs");
        wait.until(ExpectedConditions.elementToBeClickable(By.className("srch-btn-c"))).click();
        log.info("Filtering results to show only SUV cabs of any type");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='suv']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Any']"))).click();
        WebElement price=driver.findElement(By.xpath("//div[@class='nw_price']/div[2]"));
        WebElement carName=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_pro_ttl']")));
        log.info("Printing the available cabs and their prices");
        log.info("********** Available Suv Cabs **********");
        log.info("Car : "+carName.getText()+"-> "+price.getText());
    }
}



