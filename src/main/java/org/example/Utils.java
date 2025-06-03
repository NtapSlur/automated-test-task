package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Utils {
    public static void sleep(int milisecond) {
        try {
            Thread.sleep(milisecond);
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void getURL(WebDriver driver, String url) {
        boolean isServerError = true;
        while (isServerError) {
            driver.get(url);
            List<WebElement> errorMesssage = driver.findElements(By.xpath("//*[contains(text(),'Error')]"));

            if (!errorMesssage.isEmpty()) {
                System.out.println("HTTP Error! Trying to refresh!");
            }
            else {
                isServerError = false;
            }
        }
    }

    public static String formatProductCardName(String productCardName) {
        if (productCardName.length() > 50) {
            return productCardName.substring(0, 50) + "...";
        }

        return productCardName;
    }

    public static long formatPrice(String price) {
        String[] priceSplit = price.split(" ");
        String priceNumber = priceSplit[1].replace(",", "");

        return Long.parseLong(priceNumber);
    }
}
