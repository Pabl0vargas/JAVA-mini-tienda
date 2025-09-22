package com.inventory.model;


  //An abstract class that serves as the base template for all products.
  //It defines common attributes and behaviors.

public abstract class Product {
    // Private attributes for encapsulation
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


      //An abstract method that forces subclasses to provide their own specific description.
     // This is a key part of polymorphism.

    public abstract String getDescription();
}