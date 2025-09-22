package com.inventory.model;

import java.util.ArrayList;
import java.util.HashMap;


  //Manages the inventory data structures and core logic.
  //It is decoupled from the user interface.

public class Inventory {

    // --- Data Structures ---
    private ArrayList<String> productNames;
    private double[] prices;
    private HashMap<String, Integer> stock;


      //Constructor to initialize the data collections.

    public Inventory() {
        productNames = new ArrayList<>();
        prices = new double[0];
        stock = new HashMap<>();
    }

    // --- Core Logic Methods ---


     // Adds a new product to all data structures.

    public boolean addProduct(String name, double price, int initialStock) {
        if (findProductIndex(name) != -1) {
            return false;
        }

        productNames.add(name);
        stock.put(name, initialStock);
        prices = expandPricesArray(price);

        return true;
    }


      //Finds the index of a product by its name.
     // This is crucial for synchronizing the names list with the prices array.

    public int findProductIndex(String name) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1; // Not found
    }

    // --- Utility Methods ---


      //Expands the prices array to add a new price.

    private double[] expandPricesArray(double newPrice) {
        double[] newPrices = new double[prices.length + 1];
        System.arraycopy(prices, 0, newPrices, 0, prices.length);
        newPrices[newPrices.length - 1] = newPrice;
        return newPrices;
    }

    // --- Getters for UI access ---

    public ArrayList<String> getProductNames() {
        return productNames;
    }

    public double[] getPrices() {
        return prices;
    }

    public HashMap<String, Integer> getStock() {
        return stock;
    }
}