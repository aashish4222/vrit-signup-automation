
package com.vrit.tests;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.vrit.base.BaseTest;
import com.vrit.utils.MailosaurHelper;

import org.testng.annotations.Listeners;
import com.vrit.listeners.ExtentReportListener;


public class SignupTest extends BaseTest {

    @Test
    public void completeSignupTest() throws Exception {

        System.out.println("===== STARTING SIGNUP TEST =====");

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(20)
        );

        // ==========================================
        // STEP 1: LANDING PAGE
        // ==========================================

        System.out.println("Landing page opened successfully.");

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.primary-btn")
            )
        ).click();

        wait.until(ExpectedConditions.urlContains("/register"));

        System.out.println("Registration page opened successfully.");

        // ==========================================
        // STEP 2: ACCEPT AGREEMENT
        // ==========================================

        WebElement agreement = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("remember")
            )
        );

        if (!"true".equals(agreement.getAttribute("aria-checked"))) {
            agreement.click();
        }

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Continue']")
            )
        ).click();

        System.out.println("Agreement accepted.");

        // ==========================================
        // STEP 3: INITIALIZE MAILOSAUR
        // ==========================================

        MailosaurHelper mailosaurHelper = new MailosaurHelper();

        String email = mailosaurHelper.generateEmailAddress();

        System.out.println("Generated email: " + email);

        // ==========================================
        // STEP 4: GENERATE ACCOUNT DETAILS
        // ==========================================

        String[] firstNames = {
            "Aashish", "Rohan", "Suman", "Kiran"
        };

        String[] lastNames = {
            "Sharma", "Thapa", "Gurung", "Pandey"
        };

        int nameIndex = ThreadLocalRandom.current()
                                         .nextInt(firstNames.length);

        String firstName = firstNames[nameIndex];
        String lastName = lastNames[nameIndex];

        String phone = "98" +
            ThreadLocalRandom.current().nextInt(
                10000000, 100000000
            );

        String password = "Test@123456";

        // ==========================================
        // STEP 5: FILL ACCOUNT DETAILS
        // ==========================================

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("firstName")
            )
        ).sendKeys(firstName);

        driver.findElement(By.name("lastName"))
              .sendKeys(lastName);

        driver.findElement(By.name("phoneNumber"))
              .sendKeys(phone);

        driver.findElement(By.name("email"))
              .sendKeys(email);

        driver.findElement(By.name("password"))
              .sendKeys(password);

        driver.findElement(By.name("confirmPassword"))
              .sendKeys(password);

        System.out.println("Account details filled successfully.");

        // ==========================================
        // STEP 6: SUBMIT ACCOUNT DETAILS
        // ==========================================

        WebElement accountNextButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@type='submit' and normalize-space()='Next']"
                )
            )
        );

        System.out.println("Clicking account details Next button...");

        accountNextButton.click();

        Thread.sleep(3000);

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());

        List<WebElement> otpFields = driver.findElements(
            By.cssSelector("input[autocomplete='one-time-code']")
        );

        System.out.println("OTP fields found: " + otpFields.size());

        // Check common validation messages

        List<WebElement> errorMessages = driver.findElements(
            By.cssSelector(
                "[role='alert'], .text-destructive, " +
                ".text-red-500, .text-red-600"
            )
        );

        for (WebElement error : errorMessages) {

            if (error.isDisplayed() &&
                !error.getText().trim().isEmpty()) {

                System.out.println(
                    "FORM ERROR: " + error.getText()
                );
            }
        }

        System.out.println("========== VISIBLE PAGE TEXT ==========");

        System.out.println(
            driver.findElement(By.tagName("body")).getText()
        );

        System.out.println("========================================");

        Assert.assertTrue(
            !otpFields.isEmpty(),
            "OTP field did not appear after submitting account details."
        );

        System.out.println("Account details submitted successfully.");

        // ==========================================
        // STEP 7: WAIT 15 SECONDS FOR OTP
        // ==========================================

        System.out.println(
            "Waiting 15 seconds for verification email..."
        );

        Thread.sleep(15000);

        // ==========================================
        // STEP 8: RETRIEVE OTP
        // ==========================================

        String otp = mailosaurHelper.getOTP(email);

        // ==========================================
        // STEP 9: ENTER OTP
        // ==========================================

        WebElement otpField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(
                    "input[autocomplete='one-time-code']"
                )
            )
        );

        otpField.sendKeys(otp);

        Assert.assertEquals(
            otpField.getAttribute("value"),
            otp,
            "OTP was not entered correctly."
        );

        System.out.println("OTP entered successfully.");

        // ==========================================
        // STEP 10: VERIFY OTP
        // ==========================================

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@type='submit' and normalize-space()='Verify Code']"
                )
            )
        ).click();

        wait.until(
            ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(
                    "input[autocomplete='one-time-code']"
                )
            )
        );

        System.out.println(
            "OTP verification completed successfully."
        );

        // ==========================================
        // STEP 11: GENERATE AGENCY DETAILS
        // ==========================================

        int randomNumber = ThreadLocalRandom.current()
                                             .nextInt(1000, 9999);

        String agencyName = "Test Agency " + randomNumber;
        String agencyRole = "Education Consultant";

        String agencyEmail =
            "agency" + randomNumber + "@example.com";

        String agencyWebsite =
            "agency" + randomNumber + ".example.com";

        String agencyAddress =
            "123 Test Street, Sydney, Australia";

        // ==========================================
        // STEP 12: FILL AGENCY DETAILS
        // ==========================================

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("agency_name")
            )
        ).sendKeys(agencyName);

        driver.findElement(By.name("role_in_agency"))
              .sendKeys(agencyRole);

        driver.findElement(By.name("agency_email"))
              .sendKeys(agencyEmail);

        driver.findElement(By.name("agency_website"))
              .sendKeys(agencyWebsite);

        driver.findElement(By.name("agency_address"))
              .sendKeys(agencyAddress);

        System.out.println("Agency details filled successfully.");

        // ==========================================
        // STEP 13: SELECT REGION OF OPERATION
        // ==========================================

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//*[normalize-space()='Select Your Region of Operation']"
                )
            )
        ).click();

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//div[contains(@class,'cursor-pointer')]" +
                    "[.//span[normalize-space()='Australia']]"
                )
            )
        ).click();

        System.out.println("Australia selected successfully.");

        // ==========================================
        // STEP 14: SUBMIT AGENCY DETAILS
        // ==========================================

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@type='submit' and normalize-space()='Next']"
                )
            )
        ).click();

        System.out.println("Agency details submitted.");

        // ==========================================
        // STEP 15: SELECT EXPERIENCE LEVEL
        // ==========================================

        WebElement experienceDropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@role='combobox' and " +
                    ".//span[contains(text(),'Select Your Experience Level')]]"
                )
            )
        );

        experienceDropdown.click();

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//span[normalize-space()='2 years']")
            )
        ).click();

        System.out.println("Experience level selected: 2 years.");

        // ==========================================
        // STEP 16: ENTER FOCUS AREA
        // ==========================================

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("focus_area")
            )
        ).sendKeys(
            "Undergraduate admissions to Canada"
        );

        // ==========================================
        // STEP 17: ANNUAL STUDENTS
        // ==========================================

        driver.findElement(
            By.name("number_of_students_recruited_annually")
        ).sendKeys("50");

        // ==========================================
        // STEP 18: SUCCESS METRICS
        // ==========================================

        driver.findElement(
            By.name("success_metrics")
        ).sendKeys("90");

        System.out.println(
            "Experience and agency information filled successfully."
        );

        // ==========================================
        // STEP 19: SELECT FOUR CHECKBOXES
        // ==========================================

        List<WebElement> checkboxes = wait.until(
            ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("button[role='checkbox']"),
                4
            )
        );

        for (int i = 0; i < checkboxes.size(); i++) {

            WebElement checkbox = checkboxes.get(i);

            if ("false".equals(
                    checkbox.getAttribute("aria-checked"))) {

                wait.until(
                    ExpectedConditions.elementToBeClickable(checkbox)
                ).click();
            }

            System.out.println(
                "Checkbox " + (i + 1) + " selected."
            );
        }

        // ==========================================
        // STEP 20: SUBMIT EXPERIENCE DETAILS
        // ==========================================

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@type='submit' and normalize-space()='Next']"
                )
            )
        ).click();

        System.out.println("Experience details submitted.");

        // ==========================================
        // STEP 21: BUSINESS REGISTRATION NUMBER
        // ==========================================

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("business_registration_number")
            )
        ).sendKeys("TEST-REG-2026-001");

        System.out.println(
            "Business registration number entered."
        );

        // ==========================================
        // STEP 22: PREFERRED COUNTRIES
        // ==========================================

        WebElement preferredCountriesDropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//*[normalize-space()='Select Your Preferred Countries']"
                )
            )
        );

        preferredCountriesDropdown.click();

        // Select Australia

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//span[normalize-space()='Australia']")
            )
        ).click();

        System.out.println("Preferred country Australia selected.");

        // Select Nepal

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//span[normalize-space()='Nepal']")
            )
        ).click();

        System.out.println("Preferred country Nepal selected.");

        // Close dropdown by clicking outside

        driver.findElement(By.tagName("body")).click();

        // ==========================================
        // STEP 23: SELECT CERTIFICATION CHECKBOX
        // ==========================================

        WebElement certificationCheckbox = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("_r_9o_-form-item")
            )
        );

        if ("false".equals(
                certificationCheckbox.getAttribute("aria-checked"))) {

            certificationCheckbox.click();
        }

        System.out.println(
            "Certification checkbox selected."
        );

        // ==========================================
        // STEP 24: CERTIFICATION DETAILS
        // ==========================================

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("certification_details")
            )
        ).sendKeys(
            "ICEF Certified Education Agent"
        );

        System.out.println("Certification details entered.");

        // ==========================================
        // STEP 25: UPLOAD DOCUMENT
        // ==========================================

        // CHANGE THIS PATH TO AN ACTUAL FILE ON YOUR PC

        String filePath =
            "C:\\Users\\acer\\OneDrive\\downloads\\Pictures\\Screenshots 1\\dice 2.png";

        WebElement uploadInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='file']")
            )
        );

        uploadInput.sendKeys(filePath);

        System.out.println("Test document uploaded.");

        // ==========================================
        // STEP 26: CLICK SUBMIT
        // ==========================================

        WebElement submitButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@type='submit' and normalize-space()='Submit']"
                )
            )
        );

        submitButton.click();

        System.out.println("Submit button clicked.");

        // ==========================================
        // STEP 27: FINAL RESULT
        // ==========================================

        System.out.println(
            "===== FINAL SIGNUP STEP COMPLETED ====="
        );
    }
}
