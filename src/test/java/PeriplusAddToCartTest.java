import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.cdimascio.dotenv.Dotenv;

import org.example.Utils;

import java.util.ArrayList;
import java.util.List;

public class PeriplusAddToCartTest {
    WebDriver driver;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        Dotenv dotenv = Dotenv.load();
        String emailCred = dotenv.get("email");
        String passwordCred = dotenv.get("pass");

        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        actions = new Actions(driver);

        Utils.getURL(driver, "https://www.periplus.com/account/Login");

        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.id("ps"));
        emailField.sendKeys(emailCred);
        passwordField.sendKeys(passwordCred);

        WebElement loginButton = driver.findElement(By.id("button-login"));
        loginButton.click();
    }

    @Test
    public void positiveAddToEmptyCart() {
        emptyCart();
        Utils.sleep(5000);

        Utils.getURL(driver, "https://www.periplus.com/c/1/books");

        Utils.sleep(5000);

        // tag single-product tidak SRP, nama single-product tapi kepake di kategori
        List<WebElement> productCards = driver.findElements(By.className("single-product"));
        String productName = "";

        /**
         * Get First Available Product in The Product List And Add The Product To Cart
         */
        for (WebElement productCard : productCards) {
            try {
                actions.moveToElement(productCard).perform();

                List<WebElement> availableButton = productCard.findElements(By.tagName("a"));
                if (availableButton.size() == 1) {
                    continue;
                }

                productName = availableButton.get(1).getText();

                WebElement addToCartButton = productCard.findElement(By.className("addtocart"));
                addToCartButton.click();

                Utils.sleep(3000);

                break;
            }

            catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occured!");
            }
        }

        /**
         * Check the added product in the cart
         */

        Utils.getURL(driver, "https://www.periplus.com/checkout/cart");
        Utils.sleep(10000);

        List<WebElement> cartProducts = driver.findElements(By.className("row-cart-product"));
        Assert.assertEquals(cartProducts.size(), 1);

        WebElement productNameContainer = cartProducts.get(0).findElement(By.className("product-name"));

        WebElement productNameHTML = productNameContainer.findElement(By.tagName("a"));
        Assert.assertEquals(productName, Utils.formatProductCardName(productNameHTML.getText()));

        Utils.sleep(10000);
    }

    @Test
    public void positiveAddToExistingCart() {
        Utils.getURL(driver, "https://www.periplus.com/c/1/books");

        Utils.sleep(5000);

        List<WebElement> productCards = driver.findElements(By.className("single-product"));
        List<String> productName = new ArrayList<String>();

        /**
         * Get Three First Available Products in The Product List And Add The Products To Cart
         */
        int takenProduct = 0;

        for (WebElement productCard : productCards) {
            try {
                actions.moveToElement(productCard).perform();

                List<WebElement> availableButton = productCard.findElements(By.tagName("a"));
                if (availableButton.size() == 1) {
                    continue;
                }

                productName.add(availableButton.get(1).getText());
                takenProduct++;

                WebElement addToCartButton = productCard.findElement(By.className("addtocart"));
                addToCartButton.click();

                Utils.sleep(3000);

                if (takenProduct == 3) {
                    break;
                }
            }

            catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occured!");
            }
        }

        /**
         * Check the added product in the cart
         */

        Utils.getURL(driver, "https://www.periplus.com/checkout/cart");
        Utils.sleep(10000);

        List<WebElement> cartProducts = driver.findElements(By.className("row-cart-product"));

        for (WebElement cartProduct : cartProducts) {

            WebElement productNameContainer = cartProduct.findElement(By.className("product-name"));
            WebElement productNameHTML = productNameContainer.findElement(By.tagName("a"));

            Assert.assertTrue(productName.contains(Utils.formatProductCardName(productNameHTML.getText())));
        }
    }

    @Test
    public void negativeAddToCart() {
        Utils.getURL(driver, "https://www.periplus.com/c/1/books");

        Utils.sleep(5000);

        boolean isUnavailableFound = false;

        while (!isUnavailableFound) {
            List<WebElement> productCards = driver.findElements(By.className("single-product"));
            Utils.sleep(5000);
            for (WebElement productCard : productCards) {
                try {
                    driver.findElement(By.className("currently-unavailable"));

                    try {
                        productCard.findElement(By.className("currently-unavailable"));
                        List<WebElement> availableButtons = productCard.findElements(By.tagName("a"));
                        for (WebElement availableButton : availableButtons) {
                            System.out.println(availableButton.getText());
                        }
                        isUnavailableFound = true;
                        break;
                    }

                    catch (org.openqa.selenium.NoSuchElementException e) {
                        continue;
                    }
                }

                catch (org.openqa.selenium.NoSuchElementException e) {
                    break;
                }
            }

            List<WebElement> nextButtons = driver.findElements(By.className("border-lr"));
            WebElement nextButton;
            if (nextButtons.size() > 1) {
                nextButton = nextButtons.get(1);
            }

            else {
                nextButton = nextButtons.get(0);
            }
            actions.moveToElement(nextButton).perform();
            Utils.getURL(driver, nextButton.getAttribute("href"));
        }

        Assert.assertTrue(isUnavailableFound);
    }

    @AfterMethod
    public void tearDown() {
        emptyCart();
        if (driver != null) {
            driver.quit();
        }
    }

    private void emptyCart() {
        Utils.getURL(driver, "https://www.periplus.com/checkout/cart");

        while (!driver.findElements(By.className("btn-cart-remove")).isEmpty()) {
            WebElement removeButton = driver.findElement(By.className("btn-cart-remove"));
            actions.moveToElement(removeButton).perform();
            Utils.sleep(3000);
            removeButton.click();
            Utils.sleep(5000);
        }
    }
}
