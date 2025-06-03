package org.example.pageObject;
import org.example.base.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends TestBase {
    private WebDriver driver;

    private final By emailLocator = By.name("email");
    private final By passwordLocator = By.id("ps");
    private final By loginButtonLocator =  By.id("button-login");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void typeEmail(String email) {
        WebElement emailInput = waitUntilElementVisible(emailLocator);
        emailInput.sendKeys(email);
    }

    public void typePassword(String password) {
        WebElement passwordInput = waitUntilElementVisible(passwordLocator);
        passwordInput.sendKeys(password);
    }

    public HomePage submitLogin() {
        WebElement loginButton = waitUntilElementVisible(loginButtonLocator);
        loginButton.click();
        return new HomePage(driver);
    }
}
