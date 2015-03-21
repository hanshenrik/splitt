package com.hanshenrik.gronsleth_billdivider;

import java.io.Serializable;


public class Item implements Serializable {
    public final String name;
    public final double price;
    public final String[] buyers;

    public Item(String name, double price, String[] buyers) {
        this.name = name;
        this.price = price;
        this.buyers = buyers;
    }

    @Override
    public String toString() {
        return name + " // £" + price;
    }
}
