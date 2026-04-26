package PageObj;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Activites {
	WebDriver ldreiver;
	JavascriptExecutor js;
	WebDriverWait wait;
	
	public Activites(WebDriver rdriver)
	{
		ldreiver=rdriver;
		
		PageFactory.initElements(rdriver, this);	
		wait=new WebDriverWait(rdriver,Duration.ofSeconds(10));
		js = (JavascriptExecutor) rdriver;
	}
	
	@FindBy(xpath="//a[contains(text(),'Activities')]") WebElement Activites;
	

   @FindBy(xpath="//span[contains(.,'Filter')]")WebElement Filters;
   
	//@FindBy(xpath="//span[contains(text(),'Filters')]") WebElement Filters;
	
	@FindBy(xpath="//label[contains(text(),'Low to High')]")WebElement LowToHigh;
	
	@FindBy(css="button[aria-label='Apply Filters']")WebElement ApplyFilters;
	
	@FindBy(css="button[aria-label='Tomorrow']")WebElement TomarrowBtn;
	

public void activites() {
    wait.until(ExpectedConditions.elementToBeClickable(Activites));
    Activites.click();

    // Wait until filters visible = page loaded
    wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//span[contains(.,'Filter')]")));
}

	
//	public void Scrollup() {
//		 js.executeScript("window.scrollBy(0, -400);");
//	}
	

public void filters() {

    wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//span[contains(.,'Filter')]")));

    js.executeScript("window.scrollBy(0,300);");

    wait.until(ExpectedConditions.elementToBeClickable(Filters));
    js.executeScript("arguments[0].click();", Filters);
}

	
	
	public void lowToHigh() {
		LowToHigh.click();
	}
	
     public void Applyfilters() {
    	 ApplyFilters.click();
     }
     
     
     public void Tomarrow() {
    	 wait.until(ExpectedConditions.elementToBeClickable( TomarrowBtn));
    	 js.executeScript("arguments[0].click();", TomarrowBtn);
     }
    
}


