package com.hanshenrik.gronsleth_billdivider;

import java.util.ArrayList;

/**
 * Created by hanshenrik on 12/03/15.
 */
public class Item {
    private final String name;
    private final double price;
    private final ArrayList<String> buyers;

    public Item(String name, double price, ArrayList<String> buyers) {
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

    public ArrayList<String> getBuyers() {
        return buyers;
    }
}
