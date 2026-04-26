package TestRunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(

features="src/test/resources/feature",
glue="StepDefination",
dryRun=false,
monochrome=true,
//tags="@regression",//Run only Specfic tag Seanario
plugin= {"pretty","html:target/reports.html"}
//plugin={"pretty","json:target/report_json.json"}
//plugin= {"pretty","junit:tarwget/report_xml.xml"}
)
public class TestRun extends AbstractTestNGCucumberTests{

}
