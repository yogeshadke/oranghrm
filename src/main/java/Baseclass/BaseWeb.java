package Baseclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;



public class BaseWeb {
    public static Properties prop;
    ExtentReports extent = new ExtentReports();
    public static WebDriver driver;
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();

    public static final String FILEPATH = System.getProperty("user.dir") + "\\src\\test\\resources\\configration.properties";

    @BeforeClass
    public static void setup() {
        // Load configuration properties
        prop = new Properties();
        try (FileInputStream ip = new FileInputStream(FILEPATH)) {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties", e);
        }

        // Initialize browser
        String browserName = prop.getProperty("browser", "edge");
        switch (browserName.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");
                driver = new FirefoxDriver();
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", "C:\\Users\\yogesh.adke\\Downloads\\edgedriver_win64\\msedgedriver.exe");
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }
    }
    
   
    @AfterMethod
    public static String getScreenshot(String methodName) {
        File sourceFile = ((TakesScreenshot) BaseWeb.driver).getScreenshotAs(OutputType.FILE);
        String targetDirPath = System.getProperty("user.dir") + "\\screenshots\\";
        File targetDir = new File(targetDirPath);

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        String targetFilePath = targetDirPath + methodName + ".png";
        File targetFile = new File(targetFilePath);
        
        int counter = 1;
        while (targetFile.exists()) {
            targetFilePath = targetDirPath + methodName + "_" + counter + ".png";
            targetFile = new File(targetFilePath);
            counter++;
        }

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetFilePath;
    }


    @AfterClass
    public void tearDown() {
        
            driver.quit();
        
    }
    
    

	
    
}
