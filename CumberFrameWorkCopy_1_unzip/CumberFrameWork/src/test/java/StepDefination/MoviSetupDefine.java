package StepDefination;



import PageObj.MoviesPage;
import Utilites.ScreenShotUtility;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class MoviSetupDefine extends BaseClass{
	
	
	@Given("user is on zomato homepage")
	public void user_is_on_zomato_homepage() {
	    driver.get("https://www.district.in/movies/");
	    Movi = new MoviesPage(driver);
	    logger.info("Link opened");
	}

	@When("user clicks on Movies tab")
	public void user_clicks_on_movies_tab() {
		Movi.clickMovies();
	}

	@When("user opens Filters section")
	public void user_opens_filters_section() {
		Movi.openFilters();
	}

	@When("user opens Language filter")
	public void user_opens_language_filter() {
	   Movi.openLanguageFilter(); 
	   ScreenShotUtility.TakeScreenShot(driver,"Movies");
	  
	}

	@When("user print languages in console")
	public void user_print_languages_in_console() {
	   Movi.printAllLanguages();
	}

	@Then("user should see all available movie languages")
	public void user_should_see_all_available_movie_languages() {
		System.out.println("Movies name print console Successfully ");
	}



}
