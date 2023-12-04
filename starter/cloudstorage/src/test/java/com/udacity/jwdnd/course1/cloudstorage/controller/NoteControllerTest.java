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
public class NoteControllerTest {

    private WebDriver driver;
    private static CommonTest commonTest = new CommonTest();
    private static String userName = commonTest.generateUserName();

    @LocalServerPort
    private int port;

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
     * Test that creates a note, and verifies it is displayed.
     */

    @Test
    @Order(1)
    public void createNote() {
        commonTest.doMockSignUp(driver,port,"NoteUser","Test",userName,"123");

        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        // Data test
        String title = this.generateTitle();
        String description = this.generateDescription();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        // Click button add note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-note")));
        WebElement btnAddNote = driver.findElement(By.id("btn-add-note"));
        btnAddNote.click();

        createUpdateNote(title,description);

        // output
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-row")));
        List<WebElement> noteTitleElements = driver.findElements(By.id("note-title-row"));
        List<String> noteTitleValues = noteTitleElements.stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertTrue(noteTitleValues.contains(title));
    }

    /**
     * Test that edits an existing note and verifies that the changes are displayed.
     */
    @Test
    @Order(2)
    public void updateNote() {
        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        // Data test
        String title = this.generateTitle();
        String description = this.generateDescription();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        // Click button add note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-note")));
        WebElement btnAddNote = driver.findElement(By.id("btn-add-note"));
        btnAddNote.click();

        // Create note
        createUpdateNote(title,description);

        // Use the title text to determine the position of the edit button.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[.//th[text()='" + title + "']]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//th[text()='" + title + "']]"));
        WebElement editButton = row.findElement(By.tagName("button"));
        editButton.click();

        // Update Data test
        String titleUpdate = title + " Updated";
        String descriptionUpdate = description + " Updated";

        // Update note
        createUpdateNote(titleUpdate,descriptionUpdate);

        // Output
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-row")));
        List<WebElement> noteTitleElements = driver.findElements(By.id("note-title-row"));
        List<String> noteTitleValues = noteTitleElements.stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertTrue(noteTitleValues.contains(titleUpdate));
    }

    /**
     * Test that deletes a note and verifies that the note is no longer displayed.
     */
    @Test
    @Order(3)
    public void deleteNote(){
        // Authorized
        commonTest.doLogIn(driver, port, userName, "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Data test
        String title = this.generateTitle();
        String description = this.generateDescription();

        // Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        // Click button add note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-note")));
        WebElement btnAddNote = driver.findElement(By.id("btn-add-note"));
        btnAddNote.click();

        // Create note
        createUpdateNote(title,description);

        // Note data list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-row")));
        List<WebElement> noteTitleElements = driver.findElements(By.id("note-title-row"));
        List<String> noteTitleValues = noteTitleElements.stream().map(WebElement::getText).collect(Collectors.toList());
        String noteTitle = noteTitleValues.get(noteTitleValues.indexOf(title));

        // Use the title text to determine the position of the delete button.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[.//th[text()='" + noteTitle + "']]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//th[text()='" + noteTitle + "']]"));
        WebElement deleteBtn = row.findElement(By.id("btn-deleteNote"));
        deleteBtn.click();

        // Output
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-row")));
        List<WebElement> noteTitleEl = driver.findElements(By.id("note-title-row"));
        List<String> noteTitleVal = noteTitleEl.stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertFalse(noteTitleVal.contains(noteTitle));
    }

    /**
     * Create note
     * @param title
     * @param description
     */
    private void createUpdateNote(String title, String description) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputTitle = driver.findElement(By.id("note-title"));
        inputTitle.click();
        inputTitle.clear();
        inputTitle.sendKeys(title);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputDes = driver.findElement(By.id("note-description"));
        inputDes.click();
        inputDes.clear();
        inputDes.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-saveNote")));
        WebElement btnSaveNote = driver.findElement(By.id("btn-saveNote"));
        btnSaveNote.click();
    }

    /**
     * generate data test for field NoteTitle
     * @return
     */
    private String generateTitle(){
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return "Title "+randomNumber;
    }

    /**
     * generate data test for field NoteDescription
     * @return
     */
    private String generateDescription(){
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return "Description " + randomNumber;
    }
}
