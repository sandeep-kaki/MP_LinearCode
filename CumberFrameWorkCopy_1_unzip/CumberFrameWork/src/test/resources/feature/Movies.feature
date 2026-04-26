@Movies
Feature: showing movies languages


  Scenario: Verify language options available in Movies filter
    Given user is on zomato homepage
    When user clicks on Movies tab
    And user opens Filters section
    And user opens Language filter
    And user print languages in console
    Then user should see all available movie languages
