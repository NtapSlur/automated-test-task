package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
    private long total = 0;

    public void addToCart(CartProduct product) {
        cartProducts.add(product);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return this.total;
    }

    public List<CartProduct> getCartProducts() {
        return this.cartProducts;
    }

}
