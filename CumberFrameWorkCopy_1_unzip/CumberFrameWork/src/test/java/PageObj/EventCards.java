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

public class EventCards {
	WebDriver ldreiver;
    JavascriptExecutor js;
    WebDriverWait wait;

    public EventCards(WebDriver rdriver) {
        ldreiver = rdriver;
        PageFactory.initElements(rdriver, this);
        wait = new WebDriverWait(rdriver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) rdriver;
    }

  
    @FindBy(xpath="(//a[contains(@href,'/activities/')])[1]")
    WebElement firstCard;

    By GetPlaceName=By.xpath("(//div[contains(@class,'html-render-container')])[1]");
    
    By GetPrice = By.xpath("(//span[contains(text(),'₹118')])[1]");




//By GetPrice = By.xpath("//span[starts-with(normalize-space(),'₹')]");





public void FirstImg() {

    // wait for real activity cards
    wait.until(ExpectedConditions.visibilityOf(firstCard));

    js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstCard);

    try { Thread.sleep(1000); } catch (Exception e) {
    	
    }

    js.executeScript("arguments[0].click();", firstCard);

    
    wait.until(ExpectedConditions.urlMatches(".*/activities/.*"));
    
}

    public void placeName() {
    WebElement placeName1 =
        wait.until(ExpectedConditions.visibilityOfElementLocated(GetPlaceName));
    System.out.println("");
    System.out.println("First test Case");
    System.out.println("Place Name: " + placeName1.getText());
}







public void Price() {

    WebElement price = wait.until(
        ExpectedConditions.visibilityOfElementLocated(GetPrice)
    );

    System.out.println("Price: " + price.getText().trim());
}




   
}



