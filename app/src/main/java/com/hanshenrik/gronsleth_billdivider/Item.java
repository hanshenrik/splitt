package com.hanshenrik.gronsleth_billdivider;

import java.io.Serializable;

/**
 * Created by hanshenrik on 12/03/15.
 */
public class Item implements Serializable {
    private final String name;
    private final double price;
    private final String[] buyers;

    public Item(String name, double price, String[] buyers) {
        this.name = name;
        this.price = price;
        this.buyers = buyers;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String[] getBuyers() {
        return buyers;
    }
}
