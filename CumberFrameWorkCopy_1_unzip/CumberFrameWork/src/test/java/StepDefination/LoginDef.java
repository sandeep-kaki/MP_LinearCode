package StepDefination;




import PageObj.Login;
import Utilites.ScreenShotUtility;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.Excel;



public class LoginDef extends BaseClass{

     
@Given("user is on zomato Homepage")
public void user_is_on_zomato_homepage() {
    driver.get("https://www.district.in");
    log = new Login(driver);
    logger.info("Login Link opened");
}

	 
@When("user clicks on Login icon")
public void user_clicks_on_login_icon() {
                       
    log.WonderlaImage();
}

@When("user enters invalid mobile number in Excel String")
public void user_enters_invalid_mobile_number_in_excel_string() {
	String name=Excel.GEtName();
	log.EnterNumber(name);
}
    
   
@When("user enters invalid mobile number {string}")
public void user_enters_invalid_mobile_number(String string) {
    log.EnterNumber(string);
    ScreenShotUtility.TakeScreenShot(driver,"login");
}
    @When("user clicks on Continue button")
    public void user_clicks_on_continue_button() {
      log.ClickConTinue();
    }

    @Then("user should see invalid mobile number error message")
    public void user_should_see_invalid_mobile_number_error_message() {
        log.getText();                  
    }

}
