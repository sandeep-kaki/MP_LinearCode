package PageObj;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MoviesPage {
	 WebDriver driver;
	    WebDriverWait wait;
	    JavascriptExecutor js;

	    public MoviesPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        js = (JavascriptExecutor) driver;
	    }

	    // ---------------- LOCATORS ----------------

	    // Movies tab
	    @FindBy(xpath = "//a[normalize-space()='Movies']")
	    WebElement moviesTab;

	    // Filters button
	    @FindBy(xpath = "//span[normalize-space()='Filters']")
	    WebElement filtersBtn;

	    // Language filter button
	    @FindBy(xpath = "//span[normalize-space()='Language']")
	    WebElement languageBtn;

	  
	    By languagesLocator = By.xpath(
	    	    "//span[normalize-space()='Language']" +
	    	    "/ancestor::div[contains(@class,'dds-flex-col')]" +
	    	    "/following-sibling::div" +
	    	    "//span[contains(@class,'dds-text-base') and normalize-space()]"
	    	);
	   


public void clickMovies() {
    wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//a[contains(text(),'Movies')]")
    ));
    wait.until(ExpectedConditions.elementToBeClickable(moviesTab));
    moviesTab.click();
}


	    public void openFilters() {
	        wait.until(ExpectedConditions.visibilityOf(filtersBtn));
	        js.executeScript("arguments[0].click();", filtersBtn);
	    }

	    public void openLanguageFilter() {
	        wait.until(ExpectedConditions.visibilityOf(languageBtn));
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", languageBtn);
	        js.executeScript("arguments[0].click();", languageBtn);
	    }
	    public void printAllLanguages() {

	        // wait for Language heading (always present)
	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//span[normalize-space()='Language']")));

	        List<WebElement> languages = driver.findElements(languagesLocator);
	        System.out.println("");
	        System.out.println("=========================================");
	        System.out.println("TestCase 2");
	        System.out.println("Languages available:");
	        System.out.println("Count: " + languages.size());

	        for (WebElement lang : languages) {
	            System.out.println(" → " + lang.getText().trim());
	        }
	    }
	   
}
