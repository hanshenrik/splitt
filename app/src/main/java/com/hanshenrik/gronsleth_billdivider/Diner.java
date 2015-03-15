package com.hanshenrik.gronsleth_billdivider;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by hanshenrik on 12/02/15.
 */
public class Diner implements Serializable {
    private final String name;
    private HashMap<String, double[]> items;
    private double total = 0;

    public Diner(String name) {
        this.name = name;
        this.items = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public HashMap<String, double[]> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public void addItem(String name, double price, double splitBetween) {
        items.put(name, new double[] { price, splitBetween });

        // update total
        this.total += price*(1.0/splitBetween);
    }

    public String toString() {
        return getName() + " // £" + getTotal();
    }
}
