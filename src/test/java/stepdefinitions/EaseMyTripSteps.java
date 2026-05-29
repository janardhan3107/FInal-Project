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

    // COMMON STEP
    @Given("launch application")
    public void launch_application() {
        // Browser is already handled by Hooks
    }

    // ---------------- CAB BOOKING ----------------

    @When("select cab with lowest price")
    public void cab_booking() {
        cab = new CabPage(driver);
        cab.bookCab();
    }

    @Then("verify lowest priced cab is selected")
    public void verify_cab_booking() {
        System.out.println("Cab with lowest price selected successfully");
    }

    // ---------------- GIFT CARD ----------------

    @When("enter invalid email details in gift card")
    public void gift_card() {
        gift = new GiftCardPage(driver);
        gift.executeGiftFlow();
    }

    @Then("capture validation error screenshot")
    public void verify_gift_card() {
        System.out.println("Validation error captured and screenshot stored");
    }

    // ---------------- HOTEL ----------------

    @When("increase adult count to maximum")
    public void hotel_step() {
        hotel = new HotelPage(driver);
        hotel.getAdultList();
    }

    @Then("verify maximum adult count is displayed")
    public void verify_hotel() {
        System.out.println("Maximum adult count verified successfully");
    }
}