package com.hanshenrik.gronsleth_billdivider;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;


public class Diner implements Serializable {
    public final String name;
    public HashMap<String, double[]> items;
    private double total = 0;
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public Diner(String name) {
        this.name = name;
        this.items = new HashMap<>();
    }

    public void addItem(String name, double price, double splitBetween) {
        items.put(name, new double[] { price, splitBetween });

        // update total
        this.total += price*(1.0/splitBetween);
    }

    public String toString() {
        return name + " // £" + DF.format(total);
    }
}
