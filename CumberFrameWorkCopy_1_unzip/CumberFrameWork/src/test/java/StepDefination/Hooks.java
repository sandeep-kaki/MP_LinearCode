
package StepDefination;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.File;

//import java.io.File;

import org.openqa.selenium.chrome.ChromeDriver;

import Utilites.ScreenShotUtility;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks extends BaseClass {

    @Before(order=1)
    public void setupDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
    }
    
//    @After(order=2)
//    public void ScreenShotDelit() {
//    	//locating ScreenShot
//	 File folder=new File("C:\\Users\\2475070\\eclipse-workspace\\CumberFrameWork\\ScreenShot\\");
//		File[] files=folder.listFiles();
//		//Delet file
//		if(files != null) {
//			for(File file : files) {
//				file.delete();
//			}
//		}
// }
    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            ScreenShotUtility.TakeScreenShot(driver, scenario.getName());
        }

        driver.quit();
    }
}
