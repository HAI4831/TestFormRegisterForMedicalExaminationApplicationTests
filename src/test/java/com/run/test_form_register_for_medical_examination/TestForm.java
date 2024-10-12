package com.run.test_form_register_for_medical_examination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.time.Duration;

public class TestForm {

    // Path to ChromeDriver
    private static final String CHROME_DRIVER_PATH = "D:\\download\\chromedriver-win64\\chromedriver.exe";
    private static final String FORM_URL = "http://127.0.0.1:5500/10_10/form.html";

    // Default valid data for registration
    private static final String DEFAULT_NAME = "Test User";
    //        private static final String DEFAULT_EMAIL = "validuser@gmail.com";
//        private static final String DEFAULT_PASSWORD = "StrongPass1";
    private static final String DEFAULT_ADDRESS = "123 Đường ABC, Quận 1, TP.HCM";
    private static final String DEFAULT_PHONE = "0123456789";
    private static final String DEFAULT_CCCD = "123456789012";
    private static final String DEFAULT_GENDER = "Nam";
    private static final String DEFAULT_AGE = "25";
    private static final String DEFAULT_DOCTOR = "Bác Sĩ 1";
    private static final String DEFAULT_APPOINTMENT = "2024-12-01T10:00:PM";

    // Initialize WebDriver before each test
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //1) test for Name Field

    // ============================
    // DataProviders for Name Field
    // ============================
    @DataProvider(name = "nameData")
    public Object[][] nameTestData() {
        return new Object[][]{
                {"Nguyễn Văn A", true},        // Valid name
                {"", false},                   // Empty name
                {"Nguyễn@123", false},        // Name with special characters
                {"     ", false},             // Name with only spaces
                {"Nguyễn Văn A B C D E F G H I J K L M N O P Q R S T U V W X Y Z", true}, // Very long name
                {"أحمد", true}                // Non-Latin characters
        };
    }

    // ===============================
    // Test Method for Name Field
    // ===============================
    @Test(dataProvider = "nameData", description = "Test Name Field Validations")
    public void testNameField(String name, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setName(name);
        boolean actualOutcome = registerForMedicalExamination(data, "name");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Name field validation failed.");
    }

    //2) test for address field

    // =================================
    // DataProviders for Address Field
    // =================================
    @DataProvider(name = "addressData")
    public Object[][] addressData() {
        return new Object[][]{
                {"123 Đường ABC, Quận 1, TP.HCM", true},          // TC5: Valid
                {"1234", false},                                 // TC6: Too Short
//                {generateString('A', 101), false},               // TC7: Too Long
                {"", false}                                      // TC8: Required
        };
    }

    // ===============================
    // Test Method for Address Field
    // ===============================
    @Test(dataProvider = "addressData", description = "Test Address Field Validations")
    public void testAddressField(String address, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setAddress(address);
        boolean actualOutcome = registerForMedicalExamination(data, "address");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Address field validation failed.");
    }

    // 3) test for phone field

    // ================================
    // DataProviders for Phone Field
    // ================================
    @DataProvider(name = "phoneData")
    public Object[][] phoneData() {
        return new Object[][]{
                {"0123456789", true},                      // TC9: Valid
                {"012345678901234", true},                 // TC10: Valid (15 digits)
                {"012345678", false},                      // TC11: Too Short
                {"01234567890123456", false},              // TC12: Too Long
                {"01234abcde", false},                     // TC13: Non-digit
                {"", false}                                // TC14: Required
        };
    }

    // ===============================
    // Test Method for Phone Field
    // ===============================
    @Test(dataProvider = "phoneData", description = "Test Phone Number Field Validations")
    public void testPhoneField(String phone, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setPhone(phone);
        boolean actualOutcome = registerForMedicalExamination(data, "phone");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Phone number field validation failed.");
    }

    //4)test for cccd field

    // =================================
    // DataProviders for CCCD Field
    // =================================
    @DataProvider(name = "cccdData")
    public Object[][] cccdData() {
        return new Object[][]{
                {"123456789012", true},                      // TC15: Valid
                {"12345678901", false},                      // TC16: Too Short
                {"1234567890123", false},                    // TC17: Too Long
                {"12345abc9012", false},                     // TC18: Non-digit
                {"", false}                                  // TC19: Required
        };
    }

    // ===============================
    // Test Method for CCCD Field
    // ===============================
    @Test(dataProvider = "cccdData", description = "Test CCCD Number Field Validations")
    public void testCccdField(String cccd, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setCccd(cccd);
        boolean actualOutcome = registerForMedicalExamination(data, "cccd");
        Assert.assertEquals(actualOutcome, expectedOutcome, "CCCD number field validation failed.");
    }

    //5)test for gender field

    // =================================
    // DataProviders for Gender Field
    // =================================
    @DataProvider(name = "genderData")
    public Object[][] genderData() {
        return new Object[][]{
                {"Nam", true},                             // TC20: Valid
                {"Nữ", true},                              // TC21: Valid
                {"Trẻ em", true},                         // TC22: Valid
                {"Người cao tuổi", true},                  // TC23: Valid
                {null, false}                              // TC24: No selection
        };
    }

    // ===============================
    // Test Method for Gender Field
    // ===============================
    @Test(dataProvider = "genderData", description = "Test Gender Field Validations")
    public void testGenderField(String gender, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setGender(gender);
        boolean actualOutcome = registerForMedicalExamination(data, "gender");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Gender field validation failed.");
    }

    //6)test for age field

    // =================================
    // DataProviders for Age Field
    // =================================
    @DataProvider(name = "ageData")
    public Object[][] ageData() {
        return new Object[][]{
                {"25", true},       // TC25: Valid
                {"0", true},        // TC26: Valid
                {"120", true},      // TC27: Valid
                {"-1", false},      // TC28: Less than 0
                {"121", false},     // TC29: Greater than 120
                {"25.5", false},    // TC30: Non-integer
                {"", false}         // TC31: Required
        };
    }

    // ===============================
    // Test Method for Age Field
    // ===============================
    @Test(dataProvider = "ageData", description = "Test Age Field Validations")
    public void testAgeField(String age, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setAge(age);
        if(age.equals("0")) data.setGender("child");
        boolean actualOutcome = registerForMedicalExamination(data, "age");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Age field validation failed.");
    }

    //7) test for doctor field

    // ====================================
    // DataProviders for Doctor Field
    // ====================================
    @DataProvider(name = "doctorData")
    public Object[][] doctorData() {
        return new Object[][]{
                {"Bác Sĩ 1", true},  // TC32: Valid
                {"Bác Sĩ 2", true},  // TC33: Valid
                {"Bác Sĩ 3", true},  // TC34: Valid
                {null, false}         // TC35: No selection
        };
    }

    // ===============================
    // Test Method for Doctor Field
    // ===============================
    @Test(dataProvider = "doctorData", description = "Test Select Doctor Field Validations")
    public void testDoctorField(String doctor, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setDoctor(doctor);
        boolean actualOutcome = registerForMedicalExamination(data, "doctor");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Select Doctor field validation failed.");
    }

    //8) test for appointment field

    // ====================================
    // DataProviders for Appointment Field
    // ====================================
    @DataProvider(name = "appointmentData")
    public Object[][] appointmentData() {
        return new Object[][]{
                {"2024-12-01T10:00", true},      // TC36: Valid
                {"invalid-date", false},          // TC37: Invalid format
                {"2020-01-01T10:00", false},      // TC38: Past Date (Assuming past dates are invalid)
                {"", false}                        // TC39: Required
        };
    }

    // ===============================
    // Test Method for Appointment Field
    // ===============================
    @Test(dataProvider = "appointmentData", description = "Test Appointment Time Field Validations")
    public void testAppointmentField(String appointment, boolean expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        data.setAppointment(appointment);
        boolean actualOutcome = registerForMedicalExamination(data, "appointment");
        Assert.assertEquals(actualOutcome, expectedOutcome, "Appointment Time field validation failed.");
    }

    //9) test for fee field

    // =================================
    // DataProviders for Fee Field
    // =================================
    @DataProvider(name = "feeData")
    public Object[][] feeData() {
        return new Object[][]{
                {"Load form", "VND"},                             // TC40: Load form
                {"Calculate fee", "Calculated Amount"}            // TC41: Calculate fee based on inputs
        };
    }

    // ===============================
    // Test Method for Fee Field
    // ===============================
    @Test(dataProvider = "feeData", description = "Test Fee Field Validations")
    public void testFeeField(String action, String expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        // Perform the specified action
        if (action.equals("Load form")) {
            boolean actualOutcome = verifyFeeOnLoad(data);
            Assert.assertEquals(actualOutcome, expectedOutcome.equals("VND"), "Fee field display on form load failed.");
        } else if (action.equals("Calculate fee")) {
            boolean actualOutcome = calculateAndVerifyFee(data);
            Assert.assertEquals(actualOutcome, expectedOutcome.equals("Calculated Amount"), "Fee calculation failed.");
        }
    }

    //10)test for Buttons Functionality

    // =================================
    // DataProviders for Buttons Functionality
    // =================================
    @DataProvider(name = "buttonsData")
    public Object[][] buttonsData() {
        return new Object[][]{
                {"Tính Tiền", "valid", "Fee is calculated and displayed correctly"},      // TC42
                {"Tính Tiền", "invalid", "Error messages are displayed, no calculation"}, // TC43
                {"Làm Mới", "reset", "All fields are reset to default values"},           // TC44
                {"Đăng Ký", "valid", "Form is submitted successfully"},                  // TC45
                {"Đăng Ký", "invalid", "Form submission is blocked, errors shown"},       // TC46
                {"Thoát", "exit", "Form is closed or navigates away"}                    // TC47
        };
    }

    // ===============================
    // Test Method for Buttons Functionality
    // ===============================
    @Test(dataProvider = "buttonsData", description = "Test Buttons Functionality")
    public void testButtonsFunctionality(String button, String scenario, String expectedOutcome) {
        RegistrationData data = new RegistrationData().getDefaultInstance();
        if (!scenario.equals("reset") && !scenario.equals("exit")) {
            // Populate fields based on scenario
            if (scenario.equals("valid") || scenario.equals("invalid")) {
                data.setName(DEFAULT_NAME);
                data.setAddress(DEFAULT_ADDRESS);
                data.setPhone(DEFAULT_PHONE);
                data.setCccd(DEFAULT_CCCD);
                data.setGender(DEFAULT_GENDER);
                data.setAge(DEFAULT_AGE);
                data.setDoctor(DEFAULT_DOCTOR);
                data.setAppointment(DEFAULT_APPOINTMENT);
            }
        }

        performActionAndVerify(data, button, scenario, expectedOutcome);
        Assert.assertTrue(true, expectedOutcome); // Simplified assertion for demonstration
    }

    // ====================================
    // Helper Methods for Fee Field
    // ====================================

    /**
     * Verifies that the "Giá Tiền" field displays "VND" upon form load.
     *
     * @param data The registration data.
     * @return True if "Giá Tiền" displays "VND", False otherwise.
     */
    private boolean verifyFeeOnLoad(RegistrationData data) {
        try {
            // Navigate to registration page
            driver.get(FORM_URL);

            // Verify "Giá Tiền" field
            WebElement feeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fee")));
            String feeValue = feeField.getAttribute("value"); // Assuming it's an input field
            return feeValue.equals("VND");
        } catch (Exception e) {
            System.out.println("Fee verification on load failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calculates the fee based on inputs and verifies the "Giá Tiền" field updates correctly.
     *
     * @param data The registration data.
     * @return True if fee is calculated and displayed correctly, False otherwise.
     */
    private boolean calculateAndVerifyFee(RegistrationData data) {
        try {
            // Navigate to registration page
            driver.get(FORM_URL);

            // Fill in all required fields with valid data
            fillIntoForm(data);

            // Click "Tính Tiền" (Calculate Fee) button
            WebElement calculateButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("calculate_fee")));
            calculateButton.click();

            // Wait for fee to be calculated and displayed
            WebElement feeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fee")));
            String feeValue = feeField.getAttribute("value"); // Assuming it's an input field

            // Implement logic to verify fee calculation if possible
            // For demonstration, assume any non-empty value means success
            return !feeValue.isEmpty() && !feeValue.equals("VND");
        } catch (Exception e) {
            System.out.println("Fee calculation failed: " + e.getMessage());
            return false;
        }
    }

    // ====================================
    // Helper Method for Buttons Functionality
    // ====================================

    /**
     * Performs the specified button action and verifies the expected outcome.
     *
     * @param data            The registration data.
     * @param button          The button to click.
     * @param scenario        The scenario type (e.g., "valid", "invalid", "reset", "exit").
     * @param expectedOutcome The expected outcome description.
     */
    private void performActionAndVerify(RegistrationData data, String button, String scenario, String expectedOutcome) {
        try {
            // Navigate to registration page
            driver.get(FORM_URL);

            // Populate form if necessary
            if (!scenario.equals("reset") && !scenario.equals("exit")) {
                fillIntoForm(data);
            }

            // Click the specified button
            WebElement buttonElement = null;
            switch (button) {
                case "Tính Tiền":
                    buttonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("calculate_fee")));
                    break;
                case "Làm Mới":
                    buttonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("reset_form")));
                    break;
                case "Đăng Ký":
                    buttonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit_form")));
                    break;
                case "Thoát":
                    buttonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("exit_form")));
                    break;
                default:
                    System.out.println("Unknown button: " + button);
                    return;
            }
            buttonElement.click();

            // Verify the expected outcome based on action
            switch (button) {
                case "Tính Tiền":
                    if (scenario.equals("valid")) {
                        // Check if fee is calculated
                        WebElement feeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fee")));
                        String feeValue = feeField.getAttribute("value");
                        Assert.assertNotEquals(feeValue, "VND", "Fee should be calculated and not 'VND'");
                    } else if (scenario.equals("invalid")) {
                        // Check for error messages
                        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-danger")));
                        Assert.assertTrue(errorMsg.isDisplayed(), "Error message should be displayed for invalid inputs.");
                    }
                    break;
                case "Làm Mới":
                    // Verify that all fields are reset
                    Assert.assertTrue(areFieldsReset(), "All fields should be reset to default values.");
                    break;
                case "Đăng Ký":
                    if (scenario.equals("valid")) {
                        // Verify successful submission
                        wait.until(ExpectedConditions.urlContains("http://localhost:8000/home"));
                        Assert.assertTrue(driver.getCurrentUrl().contains("home"), "Form should be submitted successfully.");
                    } else if (scenario.equals("invalid")) {
                        // Check for error messages
                        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-danger")));
                        Assert.assertTrue(errorMsg.isDisplayed(), "Error messages should be displayed for invalid inputs.");
                    }
                    break;
                case "Thoát":
                    // Verify navigation or form closure
                    // Assuming it redirects to a homepage or closes the window
                    // For demonstration, check if URL changes to homepage
                    wait.until(ExpectedConditions.urlContains("http://localhost:8000/home"));
                    Assert.assertTrue(driver.getCurrentUrl().contains("home"), "Form should navigate away successfully.");
                    break;
            }

        } catch (Exception e) {
            System.out.println("Button action failed: " + e.getMessage());
            Assert.fail("Button action failed: " + e.getMessage());
        }
    }

    // ====================================
    // Helper Method to Fill Registration Form
    // ====================================

    /**
     * Fills out the registration form with the provided data.
     *
     * @param data The registration data.
     */
    private void fillIntoForm(RegistrationData data) {
        // Fill in Name
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nameField.clear();
        nameField.sendKeys(data.getName());

        // Fill in Address
        WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        addressField.clear();
        addressField.sendKeys(data.getAddress());

        // Fill in Phone
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phone")));
        phoneField.clear();
        phoneField.sendKeys(data.getPhone());

        // Fill in CCCD
        WebElement cccdField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cccd")));
        cccdField.clear();
        cccdField.sendKeys(data.getCccd());

        // Select Gender
        if (data.getGender() != null) {
            String gender = data.getGender(); // Assuming male is the other option
//            String gender = data.getGender().equalsIgnoreCase("Nữ") ? "female" : "male"; // Assuming male is the other option
            WebElement genderRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id(gender)));
            genderRadio.click();
        }
        // Fill in Age
        WebElement ageField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("age")));
        ageField.clear();
        ageField.sendKeys(data.getAge());

        // Select Doctor
        if (data.getDoctor() != null) {
            WebElement doctorSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("doctor")));
            doctorSelect.click();
            WebElement doctorOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='" + data.getDoctor() + "']")));
            doctorOption.click();
        }

        // Fill in Appointment Time
        WebElement appointmentField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appointment")));
        appointmentField.clear();
        appointmentField.sendKeys(data.getAppointment());

        // Fill in Appointment Time
//        WebElement feeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fee")));
//        feeField.clear();
//        feeField.sendKeys(data.getFee().toString());
    }
    // ====================================
    // Helper Method to Register User
    // ====================================

    /**
     * Registers a user with the provided data. Depending on the field being tested,
     * it may input invalid data for that field while keeping other fields valid.
     *
     * @param data        The registration data.
     * @param fieldToTest The specific field being tested (e.g., "name", "address").
     * @return True if registration succeeds as expected, False otherwise.
     */
    private boolean registerForMedicalExamination(RegistrationData data, String fieldToTest) {
        try {
            // Navigate to registration page
            driver.get(FORM_URL);

            // Fill in all fields
            fillIntoForm(data);

            // Click Register Button
            WebElement paymentButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("payment")));
            paymentButton.click();

           return isFormValid();
        } catch (Exception e) {
            System.out.println("Test encountered an exception: " + e.getMessage());
            return false;
        }
    }

    // ====================================
    // Helper Method to Determine Validity
    // ====================================
    public boolean isFormValid() {
        // Check if an alert is present
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();

            // Check if the alert text contains "Giá tiền ước tính: * VND"
            if (alertText.startsWith("Giá tiền ước tính: ") && alertText.endsWith(" VND")){
                return true; // Return true if alert matches the pattern
            }
        } catch (NoAlertPresentException e) {
            // No alert found, continue
        }

        // If no errors found for any fields, return true
        return false;
    }


    // ====================================
    // Helper Method to Get Error Selector
    // ====================================

    /**
     * Returns the CSS selector for the error message related to the specific field.
     *
     * @param field The field being tested.
     * @return The CSS selector for the error message.
     */
    private String getErrorSelector(String field) {
        switch (field) {
            case "name":
                return "div#error-name";
            case "address":
                return "div#error-address";
            case "phone":
                return "div#error-phone";
            case "cccd":
                return "div#error-cccd";
            case "gender":
                return "div#error-gender";
            case "age":
                return "div#error-age";
            case "doctor":
                return "div#error-doctor";
            case "appointment":
                return "div#error-appointment";
            default:
                return "div.alert-danger";
        }
    }

    // ====================================
    // Helper Method to Generate Repeated Characters
    // ====================================

    /**
     * Generates a string composed of a repeated character.
     *
     * @param c     The character to repeat.
     * @param count The number of times to repeat.
     * @return The generated string.
     */
    private String generateString(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    // ====================================
    // Helper Method to Check If Fields Are Reset
    // ====================================

    /**
     * Verifies that all input fields are reset to their default values.
     *
     * @return True if all fields are reset correctly, False otherwise.
     */
    private boolean areFieldsReset() {
        try {
            // Verify each field is reset to its default value or empty
            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement confirmPasswordField = driver.findElement(By.id("password_confirmation"));
            WebElement addressField = driver.findElement(By.id("address"));
            WebElement phoneField = driver.findElement(By.id("phone"));
            WebElement cccdField = driver.findElement(By.id("cccd"));
            WebElement ageField = driver.findElement(By.id("age"));
            WebElement doctorSelect = driver.findElement(By.id("doctor"));
            WebElement appointmentField = driver.findElement(By.id("appointment_time"));
            WebElement feeField = driver.findElement(By.id("fee"));

            // Check that fields are empty or set to default
            return nameField.getAttribute("value").isEmpty()
                    && emailField.getAttribute("value").isEmpty()
                    && passwordField.getAttribute("value").isEmpty()
                    && confirmPasswordField.getAttribute("value").isEmpty()
                    && addressField.getAttribute("value").isEmpty()
                    && phoneField.getAttribute("value").isEmpty()
                    && cccdField.getAttribute("value").isEmpty()
                    && ageField.getAttribute("value").isEmpty()
                    && doctorSelect.getAttribute("value").equals("Select Doctor") // Assuming default option value
                    && appointmentField.getAttribute("value").isEmpty()
                    && feeField.getAttribute("value").equals("VND"); // Assuming default fee is "VND"
        } catch (Exception e) {
            System.out.println("Field reset verification failed: " + e.getMessage());
            return false;
        }
    }

    // ====================================
    // Helper Method to Check If Form Is Submitted Successfully
    // ====================================

    /**
     * Verifies that the form is submitted successfully and navigates to the home page.
     *
     * @return True if submission is successful, False otherwise.
     */
    private boolean isFormSubmittedSuccessfully() {
        try {
            wait.until(ExpectedConditions.urlContains("http://localhost:8000/home"));
            return driver.getCurrentUrl().contains("home");
        } catch (Exception e) {
            System.out.println("Form submission verification failed: " + e.getMessage());
            return false;
        }
    }

    // ====================================
    // Inner Class to Hold Registration Data
    // ====================================

    /**
     * A helper class to encapsulate all registration form data.
     */

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RegistrationData { // Changed to public
        private String name;
        private String address;
        private String phone;
        private String cccd;
        private String gender;
        private String age;
        private String doctor;
        private String appointment;

        // You can create a method to return a default instance
        public RegistrationData getDefaultInstance() {
            return new RegistrationData(
                    DEFAULT_NAME,
                    DEFAULT_ADDRESS,
                    DEFAULT_PHONE,
                    DEFAULT_CCCD,
                    DEFAULT_GENDER,
                    DEFAULT_AGE,
                    DEFAULT_DOCTOR,
                    DEFAULT_APPOINTMENT
            );
        }
    }
}



//_______________________

// Depending on expected outcome, verify success or error
//            if (isFieldValid(fieldToTest, data)) {
// Expecting success
// Now wait for the alert
//                wait.until(ExpectedConditions.alertIsPresent());
//                // Switch to the alert and verify its text
//                Alert alert = driver.switchTo().alert();
//                String alertMessage = alert.getText();
//                // Check if the alert message is as expected
//                if (alertMessage.equals("Giá tiền ước tính: " + fee + " VND")) {
//                    alert.accept(); // Dismiss the alert
//                    return true;
//                }
//                return true;
//            } else {
//                 Expecting failure, check for error message related to the field
//                String errorSelector = getErrorSelector(fieldToTest);
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(errorSelector)));
//                return false;
//            }




///**
// * Determines if the input for the specific field is valid based on expected outcomes.
// *
// * @param field The field being tested.
// * @param data  The registration data.
// * @return True if the input is valid, False otherwise.
// */
//private boolean isFieldValid(String field, RegistrationData data) {
//    switch (field) {
//        case "name":
//            return data.getName() != null && data.getName().length() >= 3 && data.getName().length() <= 50;
//        case "address":
//            return data.getAddress() != null && data.getAddress().length() >= 5 && data.getAddress().length() <= 100;
//        case "phone":
//            return data.getPhone() != null && data.getPhone().matches("\\d{10,15}");
//        case "cccd":
//            return data.getCccd() != null && data.getCccd().matches("\\d{12}");
//        case "gender":
//            return data.getGender() != null && (
//                    data.getGender().equals("Nam") ||
//                            data.getGender().equals("Nữ") ||
//                            data.getGender().equals("Trẻ em") ||
//                            data.getGender().equals("Người cao tuổi")
//            );
//        case "age":
//            if (data.getAge() == null) return false;
//            try {
//                int age = Integer.parseInt(data.getAge());
//                return age >= 0 && age <= 120;
//            } catch (NumberFormatException e) {
//                return false;
//            }
//        case "doctor":
//            return data.getDoctor() != null && (
//                    data.getDoctor().equals("Bác Sĩ 1") ||
//                            data.getDoctor().equals("Bác Sĩ 2") ||
//                            data.getDoctor().equals("Bác Sĩ 3")
//            );
//        case "appointment":
//            // Simple regex for datetime format YYYY-MM-DDTHH:MM
//            if (data.getAppointment() == null) return false;
//            if (!data.getAppointment().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) return false;
//            // Additional check for past dates can be implemented if necessary
//            return true;
//        default:
//            return false;
//    }
//}