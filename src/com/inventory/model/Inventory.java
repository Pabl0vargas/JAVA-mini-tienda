package com.inventory.model;

import java.util.ArrayList;
import java.util.HashMap;


  //Manages the data collections using Product objects.

public class Inventory {

    private ArrayList<Product> products;
    private HashMap<String, Integer> stock;

    public Inventory() {
        products = new ArrayList<>();
        stock = new HashMap<>();
    }


     // Adds a new product and its stock.

    public boolean addProduct(Product product, int quantity) {
        if (findProductByName(product.getName()) != null) {
            return false;
        }
        products.add(product);
        stock.put(product.getName(), quantity);
        return true;
    }


     // Finds a product by its name.

    public Product findProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    // --- Getters for the UI to access the data ---
    public ArrayList<Product> getProducts() {
        return products;
    }

    public HashMap<String, Integer> getStock() {
        return stock;
    }
}