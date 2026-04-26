@Login
 Feature:
          Verify login negative Test Case
  Scenario: Navigate back to home and verify login validation
   Given user is on zomato Homepage
    When  user clicks on Login icon
    And user enters invalid mobile number in Excel String
    And user clicks on Continue button
    Then user should see invalid mobile number error message
  
  
  
  @regression
   Scenario Outline:
                    Navigate back to home and verify login validation DDt
    Given user is on zomato Homepage
    When  user clicks on Login icon
    And user enters invalid mobile number "<number>"
    And user clicks on Continue button
    Then user should see invalid mobile number error message

    Examples:

|number|
|123456|





