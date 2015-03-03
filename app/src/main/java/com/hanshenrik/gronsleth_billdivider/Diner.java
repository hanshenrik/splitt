package com.hanshenrik.gronsleth_billdivider;

import java.util.HashMap;

/**
 * Created by hanshenrik on 12/02/15.
 */
public class Diner {
    private final String name;
    private HashMap<String, double[]> items;
    private double total;

    public Diner(String name) {
        this.name = name;
        this.items = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public HashMap getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public void addItem(String name, double price, double fraction) {
        items.put(name, new double[] { price, fraction });

        // update total
        this.total += price*fraction;
    }
}
