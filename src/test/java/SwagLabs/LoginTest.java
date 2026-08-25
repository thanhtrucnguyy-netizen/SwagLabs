package SwagLabs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseTest {

    // ==================== 🟢 GROUP 1: FUNCTIONAL TESTS ====================

    @Test
    @DisplayName("TC_01: Login successfully with standard_user")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        Assertions.assertTrue(loginPage.isProductPageDisplayed(), "Đăng nhập thất bại, không thấy trang Products!");
    }

    @Test
    @DisplayName("TC_02: Login with wrong username")
    public void testLoginWrongUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standarduser", "secret_sauce");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    @DisplayName("TC_03: Login with wrong password")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "wrong_password");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    @DisplayName("TC_04: Leave username empty")
    public void testLoginEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "secret_sauce");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username is required";
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    @DisplayName("TC_05: Leave password empty")
    public void testLoginEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Password is required";
        Assertions.assertEquals(expectedError, actualError);
    }


    // ==================== 🟡 GROUP 2: SAUCEDEMO SPECIFIC USERS ====================

    @Test
    @DisplayName("TC_07: Login with locked_out_user")
    public void testLoginLockedUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("locked_out_user", "secret_sauce");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    @DisplayName("TC_08: Login with problem_user")
    public void testLoginProblemUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("problem_user", "secret_sauce");

        // Vẫn vào được trang sản phẩm
        Assertions.assertTrue(loginPage.isProductPageDisplayed(), "Không vào được trang Products!");
    }

    @Test
    @DisplayName("TC_09: Login with performance_glitch_user (Explicit Wait)")
    public void testLoginPerformanceGlitchUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("performance_glitch_user", "secret_sauce");

        // Nhờ có Explicit Wait trong BasePage, code sẽ tự kiên nhẫn chờ trang load xong
        Assertions.assertTrue(loginPage.isProductPageDisplayed(), "Đăng nhập thất bại do nạp trang quá chậm!");
    }


    // ==================== 🔴 GROUP 3: SECURITY TESTS ====================

    @Test
    @DisplayName("TC_14: Verify SQL Injection attack prevention")
    public void testSQLInjection() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("' OR '1'='1", "123456");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    @DisplayName("TC_18: Direct URL access to inner page without login")
    public void testDirectUrlAccess() {
        // Cố tình truy cập thẳng vào trang trong khi CHƯA đăng nhập
        driver.get("https://www.saucedemo.com/inventory.html");

        LoginPage loginPage = new LoginPage(driver);
        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: You can only access '/inventory.html' when you are logged in.";
        Assertions.assertEquals(expectedError, actualError);
    }
}