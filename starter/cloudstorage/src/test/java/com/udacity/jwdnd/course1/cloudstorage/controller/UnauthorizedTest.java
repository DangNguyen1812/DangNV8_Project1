package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.CommonTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnauthorizedTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static CommonTest commonTest;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        commonTest = new CommonTest();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /**
     * Test that verifies that an unauthorized user
     * can only access the login pages.
     */
    @Test
    public void accessLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * Test that verifies that an unauthorized user
     * can only access the signup pages.
     */
    @Test
    public void accessSignupPage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    /**
     * Write a test that signs up a new user, logs in,
     * verifies that the home page is accessible, logs out,
     * and verifies that the home page is no longer accessible.
     */
    @Test
    public void logoutAction() {
        String username = commonTest.generateUserName();

        commonTest.doMockSignUp(driver,port,"ABC","CDE",username,"User1");
        commonTest.doLogIn(driver,port,username,"User1");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-logout")));
        WebElement loginUserName = driver.findElement(By.id("btn-logout"));
        loginUserName.click();

        Assertions.assertFalse(driver.getTitle().equals("Home"));
    }

}
