package Utilites;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotUtility {

    public static void TakeScreenShot(WebDriver driver, String pageName) {

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timeStamp = String.valueOf(System.currentTimeMillis());
            String destPath = System.getProperty("user.dir")+ "/ScreenShot/" + pageName + "_" + timeStamp + ".png";

            File dest = new File(destPath);
            FileUtils.copyFile(src, dest);

        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}