package PageObject;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;



public class LoginPage {
   public Properties prop;
   public WebDriver driver;

    @FindBy(xpath = "//input[@name='username']")
  WebElement userField;

    @FindBy(xpath = "//input[@name='password']")
     WebElement passwordField;
    
   
    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitbtn;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitbtn;
    public LoginPage(WebDriver driver, Properties prop) {
        this.prop = prop;
       this.driver=driver;
        PageFactory.initElements(driver, this);
    }

    public void loginOrg() {
        userField.sendKeys(prop.getProperty("username"));
        passwordField.sendKeys(prop.getProperty("password"));
        submitbtn.click();
        
    }
}
