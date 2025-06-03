package org.example.base;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class TestBase {
    private static WebDriver driver;
    private static Actions actions;
    private static WebDriverWait wait;

    public static String email;
    public static String password;

    private final int waitDuration = 10;

    public TestBase() {
    }

    public void init() {
        ChromeOptions options = new ChromeOptions();
        // Uncomment to check using ChromeDriver
        //driver = new ChromeDriver(options);
        try {
            driver = new RemoteWebDriver(
                    new URL("http://localhost:4444"),
                    options
            );
        }

        catch (Exception e) {
            System.out.println("Setup failed!");
        }
        actions = new Actions(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));

        Dotenv dotenv = Dotenv.load();

        String baseURL = dotenv.get("baseURL");
        email = dotenv.get("email");
        password = dotenv.get("pass");

        driver.get(baseURL);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void actionMoveToElement(WebElement element) {
        actions.moveToElement(element).perform();
    }

    public static WebElement waitUntilElementVisible(By element) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public static List<WebElement> waitUntilAllElementVisible(By element) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
    }

    public static WebElement waitUntilElementClickable(By element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitUntilElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitUntilSpinnerDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
    }
}
