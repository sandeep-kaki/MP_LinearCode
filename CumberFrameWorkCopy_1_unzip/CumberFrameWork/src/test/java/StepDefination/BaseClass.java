package StepDefination;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;


import PageObj.Activites;
import PageObj.EventCards;
import PageObj.Login;
import PageObj.MoviesPage;

public class BaseClass {

   public static Logger logger = LogManager.getLogger(BaseClass.class);

	protected static WebDriver driver;
	public Activites activtes;
	public EventCards Event;
	public MoviesPage Movi;
	public Login log;
	
	
}
