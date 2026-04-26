package StepDefination;





import org.openqa.selenium.chrome.ChromeDriver;


import PageObj.Activites;
import PageObj.EventCards;
import Utilites.ScreenShotUtility;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;



public class Activit extends BaseClass{
	
	@Given("User launches Chrome browser")
	public void user_launches_chrome_browser() {
	   
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		 driver.manage().window().maximize();
		 activtes=new Activites(driver);
		 logger.info("browser open");
	}

	@When("User opens URL {string}")
	public void user_opens_url(String url) {
	    driver.get(url);
	    
	}

	@When("User navigates to Activities page")
	public void user_navigates_to_activities_page() {
		activtes.activites();
	}

	@When("User opens filter")
	public void user_opens_filter() {

		 try {
		        Thread.sleep(2000); // ✅ lightweight sync for JS UI
		    } catch (InterruptedException e) {}

		 activtes.filters();
		}

	

	@When("User selects price sorting Low to High")
	public void user_selects_price_sorting_low_to_high() {
		activtes.lowToHigh();
	}

	@When("User applies the filter")
	public void user_applies_the_filter() {
		activtes.Applyfilters();
		try {
			Thread.sleep(5000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScreenShotUtility.TakeScreenShot(driver,"Activites");

	}

	@When("User selects Tomorrow Activities")
	public void user_selects_tomorrow_activities() {
		activtes.Tomarrow();
	    Event=new EventCards(driver);
	    
	}

	@When("User selects first Activity card")
	public void user_selects_first_activity_card() {
		Event.FirstImg();
	}

    @When("User print first activity name in console")
    public void user_print_first_activity_name_in_console() {
    Event.placeName();
    }

    @When("User print first activity price")
    public void user_print_first_activity_price() {
    Event.Price();
    }

    @Then("User should see activity place name and price and print result in console")
    public void user_should_see_activity_place_name_and_price_and_print_result_in_console() {
    System.out.println("Activity name and price printed successfully");
}
   
}