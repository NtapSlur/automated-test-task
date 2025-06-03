import org.example.base.TestBase;
import org.example.model.Book;
import org.example.model.Cart;
import org.example.model.CartProduct;
import org.example.pageObject.CartPage;
import org.example.pageObject.HomePage;
import org.example.pageObject.LoginPage;
import org.example.pageObject.BookPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.example.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PeriplusAddToCartTest extends TestBase {
    private WebDriver driver;
    private Actions actions;

    private LoginPage login;
    private HomePage home;
    private BookPage book;
    private CartPage cart;


    @BeforeMethod
    public void setUp() {
        init();

        driver = getDriver();
        actions = new Actions(driver);

        login = new LoginPage(driver);
        login.typeEmail(email);
        login.typePassword(password);
        home = login.submitLogin();
        cart = home.navigateToCart();
        cart.emptyCart();
        home = cart.navigateToHomePage();
    }

    @Test
    public void positiveAddToEmptyCart() {
        book = home.navigateToBookList();
        book.addBookToCart(1);
        cart = book.navigateToCartPage();
        Cart cartModel = cart.getCart();
        List<Book> addedBooks = book.getAddedBooks();
        long bookTotal = 0;

        for (Book addedBook : addedBooks) {
            boolean hasFound = false;
            for (CartProduct cartProduct : cartModel.getCartProducts()) {
                if (cartProduct.getName().contains(addedBook.getTitle())) {

                    Assert.assertEquals(cartProduct.getPrice(), addedBook.getPrice());

                    bookTotal += addedBook.getPrice();
                    hasFound = true;
                    break;
                }
            }
            if (!hasFound) {
                throw new NoSuchElementException("There is a book that is not found in the cart!");
            }
        }

        long cartTotal = cartModel.getTotal();

        Assert.assertEquals(cartTotal, bookTotal);
    }

    @Test
    public void positiveAddToExistingCart() {
        book = home.navigateToBookList();
        book.addBookToCart(3);
        cart = book.navigateToCartPage();
        Cart cartModel = cart.getCart();
        List<Book> addedBooks = book.getAddedBooks();
        long bookTotal = 0;

        for (Book addedBook : addedBooks) {
            boolean hasFound = false;
            for (CartProduct cartProduct : cartModel.getCartProducts()) {
                if (cartProduct.getName().contains(addedBook.getTitle())) {
                    Assert.assertEquals(cartProduct.getPrice(), addedBook.getPrice());
                    bookTotal += addedBook.getPrice();
                    hasFound = true;
                    break;
                }
            }

            if (!hasFound) {
                throw new NoSuchElementException("There is a book that is not found in the cart!");
            }
        }

        long cartTotal = cartModel.getTotal();

        Assert.assertEquals(cartTotal, bookTotal);
    }

    @Test
    public void negativeAddToCart() {
        book = home.navigateToBookList();
        WebElement unavailableBook = book.findUnavailableBook();
        Assert.assertFalse(book.isAddToCartButtonFound(unavailableBook));
    }

    @AfterMethod
    public void tearDown() {
        if (cart != null) {
            cart.emptyCart();
        }
        quit();
    }
}
