package com.inventory.model;


 //A subclass representing a specific type of Product: Appliance.

public class Appliance extends Product {

    public Appliance(String name, double price) {

        super(name, price);
    }


    @Override
    public String getDescription() {
        return "Electronic product, requires special care and electrical connection.";
    }
}