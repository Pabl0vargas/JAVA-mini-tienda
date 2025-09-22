package com.inventory.ui;

import com.inventory.model.Food;
import com.inventory.model.Appliance;
import com.inventory.model.Inventory;
import com.inventory.model.Product;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;


  //Handles all user interaction, now using the object-oriented model.

public class InventoryMenu {
    private Inventory inventory;
    private double sessionTotalPurchase = 0.0;

    public InventoryMenu() {
        inventory = new Inventory();
    }

    public void showMenu() {
        String menu = "==== INVENTORY MANAGER (OOP) ====\n" +
                "1. Add Product\n" +
                "2. List Inventory\n" +
                "3. Purchase Product\n" +
                "4. Show Statistics\n" +
                "5. Search Product by Name\n" +
                "6. Exit";

        boolean exit = false;
        while (!exit) {
            String choiceStr = JOptionPane.showInputDialog(null, menu, "Main Menu", JOptionPane.PLAIN_MESSAGE);

            if (choiceStr == null) {
                exit = true;
                continue;
            }

            try {
                int choice = Integer.parseInt(choiceStr);
                switch (choice) {
                    case 1:
                        uiAddProduct();
                        break;
                    case 2:
                        uiListInventory();
                        break;
                    case 3:
                        uiPurchaseProduct();
                        break;
                    case 4:
                        uiShowStatistics();
                        break;
                    case 5:
                        uiSearchProduct();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String receipt = String.format("Thank you for using the system.\nTotal purchases this session: $%.2f", sessionTotalPurchase);
        JOptionPane.showMessageDialog(null, receipt, "Purchase Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void uiAddProduct() {
        String[] types = {"Food", "Appliance"};
        int selectedType = JOptionPane.showOptionDialog(null, "Select the product type",
                "Add Product", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (selectedType == -1) { // User closed the dialog
            return;
        }

        try {
            String name = JOptionPane.showInputDialog(null, "Enter the product name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter the price:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the initial stock:"));

            if (price < 0 || stock < 0) {
                JOptionPane.showMessageDialog(null, "Price and stock cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product newProduct;
            if (selectedType == 0) { // Food
                newProduct = new Food(name, price);
            } else { // Appliance
                newProduct = new Appliance(name, price);
            }

            if (inventory.addProduct(newProduct, stock)) {
                JOptionPane.showMessageDialog(null, "Product added successfully.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Product '" + name + "' already exists.", "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price or stock. Must be numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Operation cancelled.", "Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void uiListInventory() {
        StringBuilder list = new StringBuilder("==== CURRENT INVENTORY ====\n");
        ArrayList<Product> products = inventory.getProducts();
        HashMap<String, Integer> stockMap = inventory.getStock();

        if (products.isEmpty()) {
            list.append("The inventory is empty.");
        } else {
            for (Product product : products) {
                list.append("----------------------------------\n");
                list.append("Name: ").append(product.getName()).append("\n");
                list.append(String.format("Price: $%.2f\n", product.getPrice()));
                list.append("Stock: ").append(stockMap.get(product.getName())).append("\n");
                list.append("Description: ").append(product.getDescription()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(null, list.toString(), "Product List", JOptionPane.PLAIN_MESSAGE);
    }

    private void uiPurchaseProduct() {
        try {
            String name = JOptionPane.showInputDialog(null, "Enter the name of the product to purchase:");
            if (name == null) return;

            Product product = inventory.findProductByName(name);
            if (product == null) {
                JOptionPane.showMessageDialog(null, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the quantity to purchase:"));
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int currentStock = inventory.getStock().get(product.getName());
            if (quantity > currentStock) {
                JOptionPane.showMessageDialog(null, "Not enough stock. Available: " + currentStock, "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            inventory.getStock().put(product.getName(), currentStock - quantity);
            double totalPurchase = product.getPrice() * quantity;
            sessionTotalPurchase += totalPurchase;

            String confirmation = String.format("Purchase successful!\n%d x %s\nTotal: $%.2f", quantity, product.getName(), totalPurchase);
            JOptionPane.showMessageDialog(null, confirmation, "Purchase Confirmation", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uiShowStatistics() {
        ArrayList<Product> products = inventory.getProducts();
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products to show statistics.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Product mostExpensive = products.get(0);
        Product cheapest = products.get(0);

        for (Product product : products) {
            if (product.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = product;
            }
            if (product.getPrice() < cheapest.getPrice()) {
                cheapest = product;
            }
        }

        String statistics = String.format(
                "==== STATISTICS ====\n" +
                        "Most Expensive Product: %s ($%.2f)\n" +
                        "Cheapest Product: %s ($%.2f)",
                mostExpensive.getName(), mostExpensive.getPrice(), cheapest.getName(), cheapest.getPrice()
        );

        JOptionPane.showMessageDialog(null, statistics, "Price Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void uiSearchProduct() {
        String searchTerm = JOptionPane.showInputDialog(null, "Enter name (or part of it) to search for:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        StringBuilder results = new StringBuilder("Search results for '"+searchTerm+"':\n");
        boolean found = false;

        for (Product product : inventory.getProducts()) {
            if (product.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.append("----------------------------------\n");
                results.append("Name: ").append(product.getName()).append("\n");
                results.append(String.format("Price: $%.2f\n", product.getPrice()));
                results.append("Stock: ").append(inventory.getStock().get(product.getName())).append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("No products were found.");
        }

        JOptionPane.showMessageDialog(null, results.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
}