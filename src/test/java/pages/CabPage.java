package pages;

import org.apache.log4j.Logger;
import utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import utils.ExcelUtil;
import java.time.Duration;

public class CabPage extends ConfigReader {
    Logger log = Logger.getLogger(CabPage.class);
    WebDriver driver;
    WebDriverWait wait;

    // Page Factory locators using @FindBy annotation
    @FindBy(linkText = "Cabs")
    private WebElement cabsTab;

    @FindBy(xpath = "//label[normalize-space()='Outstation']")
    private WebElement outstationTab;

    @FindBy(id = "sourceName")
    private WebElement sourceCity;

    @FindBy(id = "a_FromSector_show")
    private WebElement fromCityInput;

    @FindBy(xpath = "//div[@class='auto_sugg_tttl' and contains(normalize-space(),'delhi')]")
    private WebElement fromCityOption;

    @FindBy(id = "a_ToSector_show")
    private WebElement toCityInput;

    @FindBy(xpath = "//div[@class='auto_sugg_tttl' and starts-with(normalize-space(),'manali')]")
    private WebElement toCityOption;

    @FindBy(id = "datepicker")
    private WebElement datePicker;

    @FindBy(className = "ui-datepicker-title")
    private WebElement datePickerMonth;

    @FindBy(xpath = "//a[@data-handler='next']")
    private WebElement nextMonthButton;

    @FindBy(xpath = "//a[normalize-space()='23']")
    private WebElement dateOption23;

    @FindBy(xpath = "//label[text()='AM']")
    private WebElement amButton;

    @FindBy(xpath = "//li[text()='6 Hr.']")
    private WebElement sixHourOption;

    @FindBy(xpath = "//li[text()='30 Min.']")
    private WebElement thirtyMinOption;

    @FindBy(className = "done_d")
    private WebElement doneButton;

    @FindBy(className = "srch-btn-c")
    private WebElement searchButton;

    @FindBy(xpath = "//span[normalize-space()='suv']")
    private WebElement suvFilter;

    @FindBy(xpath = "//span[normalize-space()='Any']")
    private WebElement anyFilter;

    @FindBy(xpath = "//div[@class='nw_price']/div[2]")
    private WebElement carPrice;

    @FindBy(xpath = "//div[@class='_pro_ttl']")
    private WebElement carName;

    public CabPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Initialize Page Factory elements
        PageFactory.initElements(driver, this);
    }

    public void bookCab() {
        log.info("Starting Cab booking flow");
        String reqMon = prop.getProperty("reqMonth");

        log.info("Clicked on cabs tab");
        wait.until(ExpectedConditions.elementToBeClickable(cabsTab)).click();

        log.info("Selected Outstation tab");
        wait.until(ExpectedConditions.elementToBeClickable(outstationTab)).click();

        log.info("Entering Source City");
        wait.until(ExpectedConditions.elementToBeClickable(sourceCity)).click();

        log.info("Getting From City name from config file");
        fromCityInput.sendKeys(prop.getProperty("fromCity"));

        boolean clicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement FromCityOption = wait.until(ExpectedConditions.elementToBeClickable(fromCityOption));
                FromCityOption.click();
                clicked = true;
                log.info("From City Selected from auto-suggest options");
                break;
            } catch (StaleElementReferenceException e) {
                log.info("Stale element, retrying... attempt " + (retry + 1));
            }
        }

        log.info("Getting destination city name from config file");
        toCityInput.sendKeys(prop.getProperty("toCity"));

        boolean toClicked = false;
        for(int retry = 0; retry < 3; retry++) {
            try {
                WebElement ToCityOption = wait.until(ExpectedConditions.elementToBeClickable(toCityOption));
                ToCityOption.click();
                toClicked = true;
                log.info("Successfully selected To City from auto-suggest options");
                break;
            } catch (StaleElementReferenceException e) {
                log.info("Stale element for ToCity, retrying... attempt " + (retry + 1));
            }
        }

        log.info("Selecting the required date (Dec 23) from date picker");
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();

        log.info("Navigating until the required month is visible in the date picker");
        while(true){
            WebElement month = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-datepicker-title")));
            if(!month.getText().equals(reqMon)){
                nextMonthButton.click();
            }
            else{
                wait.until(ExpectedConditions.elementToBeClickable(dateOption23)).click();
                log.info("Successfully selected the required date");
                break;
            }
        }

        log.info("Selecting pickup time as 6:30 AM");
        wait.until(ExpectedConditions.elementToBeClickable(amButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(sixHourOption)).click();
        wait.until(ExpectedConditions.elementToBeClickable(thirtyMinOption)).click();

        log.info("Clicked on done after selecting time");
        wait.until(ExpectedConditions.elementToBeClickable(doneButton)).click();

        log.info("Clicked search button to view available cabs");
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

        log.info("Filtering results to show only SUV cabs of any type");
        wait.until(ExpectedConditions.elementToBeClickable(suvFilter)).click();
        wait.until(ExpectedConditions.elementToBeClickable(anyFilter)).click();

        //WebElement price = carPrice;
        //WebElement carNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_pro_ttl']")));

        log.info("Printing the available cabs and their prices");
        log.info("********** Available Suv Cabs **********");
        log.info("Car : " + carName.getText() + "-> " + carPrice.getText());
        ExcelUtil.writeData("Cab Booking", "Car: " + carName.getText() + "-> " + carPrice.getText());
        log.info("Cab details written to Excel file successfully");
    }
}