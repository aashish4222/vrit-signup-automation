
package com.vrit.listeners;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.vrit.base.BaseTest;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {

        try {
            Files.createDirectories(Paths.get("reports"));
            Files.createDirectories(Paths.get("screenshots"));
        } catch (Exception e) {
            throw new RuntimeException("Could not create report directories", e);
        }

        ExtentSparkReporter spark =
                new ExtentSparkReporter("reports/ExtentReport.html");

        spark.config().setDocumentTitle("Vrit Signup Automation Report");
        spark.config().setReportName("Signup Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Project", "Vrit Signup Automation");
        extent.setSystemInfo("Framework", "Selenium WebDriver");
        extent.setSystemInfo("Test Framework", "TestNG");
        extent.setSystemInfo("Language", "Java");
        extent.setSystemInfo("Browser", "Google Chrome");

        System.out.println("Extent Report initialized.");
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest extentTest =
                extent.createTest(result.getMethod().getMethodName());

        test.set(extentTest);
        test.get().info("Test execution started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        test.get().pass("Test passed successfully.");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        test.get().fail(result.getThrowable());

        try {
            Object instance = result.getInstance();

            if (instance instanceof BaseTest) {

                WebDriver driver = ((BaseTest) instance).getDriver();

                if (driver != null) {

                    String timestamp = LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    );

                    String screenshotName =
                            result.getMethod().getMethodName()
                            + "_" + timestamp + ".png";

                    Path screenshotPath = Paths.get(
                            "screenshots", screenshotName
                    );

                    File screenshot = ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

                    Files.copy(screenshot.toPath(), screenshotPath);

                    test.get().addScreenCaptureFromPath(
                            screenshotPath.toAbsolutePath().toString()
                    );
                }
            }

        } catch (Exception e) {
            test.get().warning(
                    "Screenshot could not be captured: " + e.getMessage()
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        test.get().skip("Test skipped.");

        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();

        System.out.println("Extent Report generated successfully.");
        System.out.println("Report location: reports/ExtentReport.html");
    }
}
