Feature: EaseMyTrip Automation

  Scenario: Cab Booking Test Case
    Given launch application
    When select cab with lowest price
    Then verify lowest priced cab is selected

  Scenario: Gift Card Test Case
    Given launch application
    When enter invalid email details in gift card
    Then capture validation error screenshot

  Scenario: Hotel Test Case
    Given launch application
    When increase adult count to maximum
    Then verify maximum adult count is displayed
