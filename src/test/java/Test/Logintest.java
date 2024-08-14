package Test;


import org.testng.annotations.Test;

import Baseclass.BaseWeb;
import PageObject.LoginPage;

public class Logintest extends BaseWeb {
	
    @Test
   public void verifyLogin() throws InterruptedException {
        driver.get(prop.getProperty("url")); 
        driver.manage().window().maximize();
        Thread.sleep(800);
        LoginPage loginPage = new LoginPage(driver, prop);
        loginPage.loginOrg();
    }
}
