package com.inventory.model;


  //A subclass representing a specific type of Product: Food.

public class Food extends Product {

    public Food(String name, double price) {

        super(name, price);
    }


     // Provides the specific description for Food items.

    @Override
    public String getDescription() {
        return "Edible product, best consumed before the expiration date.";
    }
}