@Activites
Feature: Select lowest price Activity
  @Sanity @regression
  Scenario: Select lowest price Activity for Tomorrow
    Given User launches Chrome browser
    When User opens URL "https://www.district.in"
    And User navigates to Activities page
    And User opens filter
    And User selects price sorting Low to High
    And User applies the filter
    And User selects Tomorrow Activities
    And User selects first Activity card
    And User print first activity name in console
    And User print first activity price
    Then User should see activity place name and price and print result in console

        
        
        
        
        
        
        
        
        
        
