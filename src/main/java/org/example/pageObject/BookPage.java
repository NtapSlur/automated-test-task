package org.example.pageObject;

import org.example.Utils;
import org.example.base.TestBase;
import org.example.model.Book;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BookPage extends TestBase {
    private WebDriver driver;

    private final By bookCardLocator = By.xpath("//div[contains(@class, 'single-product')]//div[contains(@class, 'product-content')]");
    private final By addToCardButtonLocator = By.className("addtocart");
    private final By nextButtonLocator = By.className("border-lr");
    private final By unavailableBookLocator = By.xpath("//div[contains(@class, 'single-product')]//div[contains(@class, 'currently-unavailable')]");
    private final By cartLocator = By.xpath("//*[@id='show-your-cart']/a");

    private final By productTitleLocator = By.xpath(".//h3//a");
    private final By productAuthorLocator = By.className("product-author");
    private final By productBindingLocator = By.className("product-binding");
    private final By productPriceLocator = By.className("product-price");

    private List<Book> addedBooks;

    public BookPage(WebDriver driver) {
        this.driver = driver;
        addedBooks = new ArrayList<Book>();
    }

    public List<Book> getAddedBooks() {
        return addedBooks;
    }

    public void addBookToCart(int amount) {
        List<WebElement> allBook = waitUntilAllElementVisible(bookCardLocator);
        for (WebElement book : allBook) {
            if (amount == 0) {
                break;
            }
            waitUntilSpinnerDisappear();

            clickAddToCartButton(book);
            addedBooks.add(fetchBookInformation(book));

            amount--;
        }
    }

    private void clickAddToCartButton(WebElement book) {
        waitUntilElementClickable(book);
        actionMoveToElement(book);
        WebElement addToCardButton = book.findElement(addToCardButtonLocator);
        addToCardButton.click();
    }

    private Book fetchBookInformation(WebElement book) {
        Book bookModel = new Book();
        WebElement bookTitle = book.findElement(productTitleLocator);

        String title =  bookTitle.getText();
        bookModel.setTitle(formatBookTitle(title));

        WebElement bookAuthor = book.findElement(productAuthorLocator);
        bookModel.setAuthor(bookAuthor.getText());

        WebElement bookBinding = book.findElement(productBindingLocator);
        bookModel.setBinding(bookBinding.getText());

        WebElement bookPrice = book.findElement(productPriceLocator);
        bookModel.setPrice(Utils.formatPrice(bookPrice.getText()));

        return bookModel;
    }

    private String formatBookTitle(String title) {
        if (title.contains("...")) {
            title = title.substring(0, title.length()-3);
        }
        return title;
    }

    public void navigateToNextPage() {
        List<WebElement> nextButtons = waitUntilAllElementVisible(nextButtonLocator);

        /**
         * This part of code that checks for the size of nextButtons List is a must
         * Since Periplus doesn't distinguish the button for the previous and next
         */
        WebElement nextButton;
        if (nextButtons.size() > 1) {
            nextButton = nextButtons.get(1);
        }

        else {
            nextButton = nextButtons.get(0);
        }
        actionMoveToElement(nextButton);
        nextButton.click();
    }

    public CartPage navigateToCartPage() {
        WebElement cartButton = waitUntilElementClickable(cartLocator);
        actionMoveToElement(cartButton);
        cartButton.click();
        return new CartPage(driver);
    }

    public boolean isAddToCartButtonFound(WebElement book) {
        try {
            book.findElement(addToCardButtonLocator);
            return true;
        }

        catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public WebElement findUnavailableBook() {
        boolean isBookFound = false;
        WebElement selectedBook = null;
        while(!isBookFound) {
            try {
                selectedBook = waitUntilElementVisible(unavailableBookLocator);
                isBookFound =  true;
            }

            catch (org.openqa.selenium.NoSuchElementException e) {
                navigateToNextPage();
            }
        }

        return selectedBook;
    }
}
