package org.example.pageObject;

import org.example.base.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.print.Book;

public class HomePage extends TestBase {
    private WebDriver driver;

    private final By cartLocator = By.xpath("//*[@id='show-your-cart']/a");
    private final By categoryLocator = By.xpath("//*[contains(@class, 'navbar-nav')]//a[contains(., 'Categories')]");
    private final By bookLocator = By.xpath("//*[contains(@class, 'navbar-nav')]//a[contains(., 'Books')]");


    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public CartPage navigateToCart() {
        WebElement cartButton = waitUntilElementClickable(cartLocator);
        cartButton.click();
        return new CartPage(this.driver);
    }

    public BookPage navigateToBookList() {
        WebElement category = waitUntilElementClickable(categoryLocator);
        actionMoveToElement(category);
        WebElement book = waitUntilElementClickable(bookLocator);
        book.click();
        return new BookPage(this.driver);
    }
}
