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

public class Login {
	WebDriver ldreiver;
	WebDriverWait wait;
	JavascriptExecutor js;
	
	public Login(WebDriver rdriver)
	{
		ldreiver=rdriver;
		PageFactory.initElements(rdriver, this);
		wait=new WebDriverWait(rdriver, Duration.ofSeconds(20));
		js = (JavascriptExecutor)rdriver;
	}
	
	//@FindBy(xpath="//div[@aria-label='User Avatar']")WebElement wonderlaImage;



@FindBy(xpath = "//div[@role='button' and @aria-label='User Avatar']")
WebElement userAvatar;



	@FindBy(xpath="//input[@maxlength='15']")WebElement Enternumber;
    
	@FindBy(xpath="//button[.//text()[contains(.,'Continue')]]")WebElement ClickContinue;
	
	By GetText =By.xpath("//p[contains(text(),'Please enter a valid mobile number.')]");
	

public void WonderlaImage() {
    wait.until(ExpectedConditions.elementToBeClickable(userAvatar));
    userAvatar.click();
}

	public void EnterNumber(String number) {
		Enternumber.sendKeys(number);
	}
	
	public void ClickConTinue() {
		ClickContinue.click();
	}
	
	public void getText(){
		WebElement ErrorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(GetText));
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("Test Case 3");
        System.out.println("Error Message: " + ErrorMsg.getText());
	}


	
}


