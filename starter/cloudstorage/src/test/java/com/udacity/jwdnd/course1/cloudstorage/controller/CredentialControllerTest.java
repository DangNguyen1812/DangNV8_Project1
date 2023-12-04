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

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialControllerTest {

    private static WebDriver driver;
    private static CommonTest commonTest = new CommonTest();
    private static String userName = commonTest.generateUserName();

    @LocalServerPort
    private  int port;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
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
     * test that creates a set of credentials, verifies that they are displayed,
     * and verifies that the displayed password is encrypted.
     */
    @Test
    @Order(1)
    public void createCredential() throws InterruptedException {
        commonTest.doMockSignUp(driver, port, "CredentialUser", "Test", userName, "123");

        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        // Data test
        String url = this.generateUrl();
        String userName = this.generateUserName();
        String password = this.generatePass();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
        noteTab.click();

        // Click button add credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-credential")));
        WebElement btnAddCredential = driver.findElement(By.id("btn-add-credential"));
        btnAddCredential.click();

        createUpdateCredential(url, userName, password);

        // verifies that they are displayed
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-url-row")));
        List<WebElement> creTitleElements = driver.findElements(By.id("cre-url-row"));
        List<String> creUrlValues = creTitleElements.stream().map(WebElement::getText).collect(Collectors.toList());

        // verifies that they are displayed
        Assertions.assertTrue(creUrlValues.contains(url));

        // verifies that the displayed password is encrypted.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-pw-row")));
        List<WebElement> crePassElements = driver.findElements(By.id("cre-pw-row"));
        List<String> crePassValues = crePassElements.stream().map(WebElement::getText).collect(Collectors.toList());

        // verifies that the displayed password is encrypted.
        Assertions.assertFalse(crePassValues.contains(password));
    }

    @Test
    @Order(2)
    public void updateCredential() throws InterruptedException {
        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        // Data test
        String url = this.generateUrl();
        String userName = this.generateUserName();
        String password = this.generatePass();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
        noteTab.click();

        // Click button add credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-credential")));
        WebElement btAddCredential = driver.findElement(By.id("btn-add-credential"));
        btAddCredential.click();

        // Create credential
        createUpdateCredential(url, userName, password);

        // Use the url to determine the position of the edit button.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[.//th[text()='" + url + "']]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//th[text()='" + url + "']]"));
        WebElement editButton = row.findElement(By.tagName("button"));
        editButton.click();

        // Update Data test
        String urlUpdate = this.generateUrl();
        String userNameUpdate = this.generateUserName();
        String passwordUpdate = this.generatePass();

        // Update credential
        createUpdateCredential(urlUpdate, userNameUpdate, passwordUpdate);

        // data on screen
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-url-row")));
        List<WebElement> creTitleElements = driver.findElements(By.id("cre-url-row"));
        List<String> creUrlValues = creTitleElements.stream().map(WebElement::getText).collect(Collectors.toList());

        // verifies that they are displayed
        Assertions.assertTrue(creUrlValues.contains(urlUpdate));

        // verifies that the displayed password is encrypted.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-pw-row")));
        List<WebElement> crePassElements = driver.findElements(By.id("cre-pw-row"));
        List<String> crePassValues = crePassElements.stream().map(WebElement::getText).collect(Collectors.toList());

        // verifies that the displayed password is encrypted.
        Assertions.assertFalse(crePassValues.contains(passwordUpdate));
    }

    @Test
    @Order(3)
    public void deleteCredential() throws InterruptedException {
        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Data test
        String url = this.generateUrl();
        String userName = this.generateUserName();
        String password = this.generatePass();

        // Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
        noteTab.click();

        // Click button add credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-credential")));
        WebElement btnAddCredential = driver.findElement(By.id("btn-add-credential"));
        btnAddCredential.click();

        // Create credential
        createUpdateCredential(url, userName, password);

        // Data on screen
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-url-row")));
        List<WebElement> creTitleElementList = driver.findElements(By.id("cre-url-row"));
        List<String> creUrlList = creTitleElementList.stream().map(WebElement::getText).collect(Collectors.toList());
        String noteTitle = creUrlList.get(creUrlList.indexOf(url));

        // Use the url to determine the position of the delete button.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[.//th[text()='" + url + "']]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//th[text()='" + url + "']]"));
        WebElement editButton = row.findElement(By.id("btn-credential-delete"));
        editButton.click();

        // Output
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cre-url-row")));
        List<WebElement> creTitleElementList1 = driver.findElements(By.id("cre-url-row"));
        List<String> creUrlList1 = creTitleElementList1.stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertFalse(creUrlList1.contains(noteTitle));
    }

    /**
     * @param url
     * @param username
     * @param pass
     */
    private void createUpdateCredential(String url, String username, String pass) throws InterruptedException {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        inputUrl.click();
        inputUrl.clear();
        inputUrl.sendKeys(url);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUserName = driver.findElement(By.id("credential-username"));
        inputUserName.click();
        inputUserName.clear();
        inputUserName.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.clear();
        inputPassword.sendKeys(pass);

        Thread.sleep(1000);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-update-credential")));
        WebElement btnSaveCredential = driver.findElement(By.id("btn-update-credential"));
        btnSaveCredential.click();
    }

    /**
     * generate data test for field Url
     *
     * @return
     */
    private String generateUrl() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return "http://localhost:8080/" + randomNumber + ".com";
    }

    /**
     * generate data test for field Password
     *
     * @return
     */
    private String generatePass() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return "Pass " + randomNumber;
    }

    /**
     * generate data test for field UserName
     *
     * @return
     */
    private String generateUserName() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return "UserName " + randomNumber;
    }


}
