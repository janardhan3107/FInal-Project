package stepdefinitions;

import io.cucumber.java.en.*;
import pages.*;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;

public class EaseMyTripSteps {

    WebDriver driver = DriverFactory.getDriver();

    CabPage cab;
    GiftCardPage gift;
    HotelPage hotel;

    @Given("launch application")
    public void launch_application() {
        // handled by Hooks (browser already opened)
    }

    // ✅ STEP 1 (Test Case 1)
    @When("perform cab booking")
    public void cab_booking() {
        cab = new CabPage(driver);
        cab.bookCab();
    }

    // ✅ STEP 2 (Test Case 2)
    @And("perform gift card validation")
    public void gift_card() {
        gift = new GiftCardPage(driver);
        gift.executeGiftFlow();
    }

    // ✅ STEP 3 (Test Case 3)
    @And("perform hotel adult extraction")
    public void hotel_step() {
        hotel = new HotelPage(driver);
        hotel.getAdultList();
    }

    // ✅ FINAL VERIFICATION
    @Then("verify all tasks completed")
    public void verify_all_tasks() {
        System.out.println("✅ All tasks executed in single browser successfully");
    }
}
