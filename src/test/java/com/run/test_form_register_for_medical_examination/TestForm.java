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

import java.time.Duration;

public class TestForm {
    private static final String CHROME_DRIVER_PATH = "D:\\download\\chromedriver-win64\\chromedriver.exe";
    private static final String FORM_URL = "http://127.0.0.1:5500/form.html";
    private static final String DEFAULT_NAME = "Test User";
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
    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(FORM_URL);
    }
    @BeforeMethod
    public void setUpMethod() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='reset' and text()='Làm Mới']")));
        button.click();
    }
    @AfterMethod
    public void afterMethod() {
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @DataProvider(name = "fieldValidationData")
    public Object[][] testData() {
        return new Object[][]{
                // Name field test cases
                {"name", "Nguyễn Văn A", true},        // Valid name
                {"name", "", false},                   // Empty name
                {"name", "Nguyễn@123", false},        // Name with special characters
                {"name", "     ", false},             // Name with only spaces
                {"name", "Nguyễn Văn A B C D E F G H I J K L M N O P Q R S T U V W X Y Z", true}, // Very long name
                {"name", "أحمد", true},                // Non-Latin characters

                // Address field test cases
                {"address", "123 Đường ABC, Quận 1, TP.HCM", true}, // Valid address
                {"address", "1234", false},                      // Too short
                {"address", "", false},                           // Required

                // Phone field test cases
                {"phone", "0123456789", true},                      // Valid phone
                {"phone", "012345678901234", true},                 // Valid (15 digits)
                {"phone", "012345678", false},                      // Too short
                {"phone", "01234567890123456", false},              // Too long
                {"phone", "01234abcde", false},                     // Non-digit
                {"phone", "", false},                                // Required

                // CCCD field test cases
                {"cccd", "123456789012", true},                      // Valid CCCD
                {"cccd", "12345678901", false},                      // Too short
                {"cccd", "1234567890123", false},                    // Too long
                {"cccd", "12345abc9012", false},                     // Non-digit
                {"cccd", "", false},                                  // Required

                // Gender field test cases
                {"gender", "Nam", true},                             // Valid
                {"gender", "Nữ", true},                              // Valid
                {"gender", "Trẻ em", true},                         // Valid
                {"gender", "Người cao tuổi", true},                  // Valid
                {"gender", null, false},                              // No selection

                // Age field test cases
                {"age", "25", true},       // Valid
                {"age", "0", true},        // Valid
                {"age", "120", true},      // Valid
                {"age", "-1", false},      // Less than 0
                {"age", "121", false},     // Greater than 120
                {"age", "25.5", false},    // Non-integer
                {"age", "", false},         // Required

                // Doctor field test cases
                {"doctor", "Bác Sĩ 1", true},  // Valid
                {"doctor", "Bác Sĩ 2", true},  // Valid
                {"doctor", "Bác Sĩ 3", true},  // Valid
                {"doctor", null, false},         // No selection

                // Appointment field test cases
                {"appointment", "2024-12-01T10:00:PM", true},      // Valid
                {"appointment", "invalid-date", false},          // Invalid format
                {"appointment", "2020-01-01T10:00", false},      // Past Date
                {"appointment", "", false},                        // Required
                // Trường hợp kiểm tra cho các nút
                {"buttons", "charge",}, // Nhấn nút submit thành công
                {"buttons", "refresh", true}, // Nhấn nút không hợp lệ
                {"buttons", "register", true},
                {"buttons", "cancel", true},
        };
    }


    @Test(dataProvider = "fieldValidationData", description = "Test Field Validations", threadPoolSize = 5, invocationCount = 10)
    public void testFieldValidation(Object... data) {
        RegistrationData regData = new RegistrationData().getDefaultInstance();
        String fieldType = (String) data[0];
        Object inputValue = data[1];
        Object expectedOutcome = data[2];

        // Thiết lập dữ liệu dựa trên loại trường
        switch (fieldType) {
            case "name":
                regData.setName((String) inputValue);
                break;
            case "address":
                regData.setAddress((String) inputValue);
                break;
            case "phone":
                regData.setPhone((String) inputValue);
                break;
            case "cccd":
                regData.setCccd((String) inputValue);
                break;
            case "gender":
                regData.setGender((String) inputValue);
                break;
            case "age":
                regData.setAge((String) inputValue);
                if ("0".equals(inputValue)) {
                    regData.setGender("Trẻ em");
                }
                break;
            case "doctor":
                regData.setDoctor((String) inputValue);
                break;
            case "appointment":
                regData.setAppointment((String) inputValue);
                break;
            case "buttons":
                // So sánh thông báo alert với kết quả mong đợi
                Assert.assertEquals(handleButtonClick(inputValue), expectedOutcome);
                return; // Thoát khỏi phương thức sau khi xử lý nút
            default:
                throw new IllegalArgumentException("Loại trường không hợp lệ: " + fieldType);
        }

        // So sánh kết quả mong đợi với kết quả trả về từ phương thức đăng ký
        Assert.assertEquals(registerForMedicalExamination(regData), expectedOutcome);
    }

    private boolean handleButtonClick(Object inputValue) {
        String buttonType = (String) inputValue;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String message = ""; // Khởi tạo message
        WebElement button;

        // Gán giá trị message và nút tương ứng
        switch (buttonType) {
            case "charge" -> {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and text()='Tính Tiền']")));
                message = "Giá tiền ước tính: % VND";
            }
            case "refresh" -> {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='reset' and text()='Làm Mới']")));
                message = "invalid";
            }
            case "register" -> {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Đăng Ký']")));
                message = "Đăng ký thành công!";
            }
            case "exit" -> {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and text()='Thoát']")));
                message = "Bạn có chắc chắn muốn thoát không?";
            }
            default -> throw new IllegalArgumentException("Nút không hợp lệ: " + buttonType);
        }

        // Nhấn nút
        button.click();

        // Lấy alert và so sánh với message đã gán
        String alertMessage = wait.until(ExpectedConditions.alertIsPresent()).getText();
        driver.switchTo().alert().accept(); // Đóng alert
        return alertMessage.equals(message);
    }



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
            String gender = data.getGender();

            if (gender.equalsIgnoreCase("Nữ")) {
                gender = "female";
            } else if (gender.equalsIgnoreCase("Nam")) {
                gender = "male";
            } else if (gender.equalsIgnoreCase("Trẻ em")) {
                gender = "child";
            } else if (gender.equalsIgnoreCase("Người cao tuổi")) {
                gender = "elder";
            }

            WebElement genderRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id(gender)));
            genderRadio.click();
        }

        // Fill in Age
        WebElement ageField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("age")));
        ageField.clear();
        ageField.sendKeys(data.getAge().toString());

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
    }
    private boolean registerForMedicalExamination(RegistrationData data) {
        try {
            //reset click
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='reset' and text()='Làm Mới']")));
            button.click();

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
