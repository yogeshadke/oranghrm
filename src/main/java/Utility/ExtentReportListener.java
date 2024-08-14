package Utility;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import Baseclass.BaseWeb;


public class ExtentReportListener implements ITestListener {

   // public static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "\\Qa.orangehrm\\reports";
   // public static final String FILE_NAME = "TestExecutionReport.html";
    
    public static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static String screenshotPath;
    @SuppressWarnings("unused")
	public static ExtentReports init() {
//        try {
//           /// Files.createDirectories(Paths.get(OUTPUT_FOLDER));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String repName="Test-Report-" +timestamp+".html";
        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\"+repName);
        reporter.config().setReportName("Open Cart Automation Test Results");

        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("System", "MAC");
        extentReports.setSystemInfo("Author", "Yogesh");
        extentReports.setSystemInfo("Build#", "1.1");
        extentReports.setSystemInfo("Team", "OpenCart QA Team");
        extentReports.setSystemInfo("Customer Name", "NAL");
        extentReports.setSystemInfo("ENV NAME", System.getProperty("env"));

        return extentReports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("Test Suite started!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite is ending!");
        extent.flush();
        test.remove();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        
        System.out.println(methodName + " started!");

        ExtentTest extentTest = extent.createTest(methodName, result.getMethod().getDescription())
                                      .assignCategory(className);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        System.out.println(methodName + " passed!");

        test.get().pass("Test passed");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        System.out.println(methodName + " failed!");

        
		try {
			screenshotPath = BaseWeb.getScreenshot(methodName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.get().fail("Test failed");
		test.get().fail(result.getThrowable(),
		MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, methodName).build());

        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        System.out.println(methodName + " skipped!");

        test.get().skip("Test skipped");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());
    }

    public Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
