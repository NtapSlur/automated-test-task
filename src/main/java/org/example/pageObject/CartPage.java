package org.example.pageObject;

import org.example.Utils;
import org.example.base.TestBase;
import org.example.model.Cart;
import org.example.model.CartProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends TestBase {
    private WebDriver driver;

    private final By homePageLocator = By.xpath("//a[@href='https://www.periplus.com/']");

    private final By productCardLocator = By.className("row-cart-product");
    private final By productNameLocator = By.xpath(".//p[contains(@class, 'product-name')]//a");
    private final By productPriceLocator = By.xpath(".//div[@class='row' and contains(text(), 'Rp')]");

    private final By totalLocator = By.id("sub_total");
    private final By removeButtonLocator = By.className("btn-cart-remove");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void emptyCart() {
        while (true) {
            try {
                waitUntilSpinnerDisappear();
                List<WebElement> removeButtons = waitUntilAllElementVisible(removeButtonLocator);
                waitUntilElementClickable(removeButtons.get(0));
                actionMoveToElement(removeButtons.get(0));
                removeButtons.get(0).click();
            }

            catch (org.openqa.selenium.TimeoutException e) {
                break;
            }

        }
    }

    public HomePage navigateToHomePage() {
        WebElement homePage = waitUntilElementClickable(homePageLocator);
        homePage.click();
        return new HomePage(driver);
    }
    public Cart getCart() {
        Cart cartModel = new Cart();
        List<WebElement> productCards = waitUntilAllElementVisible(productCardLocator);
        for (WebElement productCard : productCards) {
            CartProduct cartProduct = new CartProduct();

            WebElement productName = productCard.findElement(productNameLocator);
            cartProduct.setName(productName.getText());

            WebElement productPrice = productCard.findElement(productPriceLocator);

            cartProduct.setPrice(Utils.formatPrice(productPrice.getText()));
            cartModel.addToCart(cartProduct);
        }

        WebElement total = waitUntilElementVisible(totalLocator);
        cartModel.setTotal(Utils.formatPrice(total.getText()));

        return cartModel;
    }
}
