package com.inventory.ui;

import com.inventory.model.Inventory;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;


  //Handles all user interaction via JOptionPane dialogs.
  //It contains the main menu loop and the logic for each option.

public class InventoryMenu {

    private Inventory inventory;
    private double sessionTotalPurchase = 0.0;

    public InventoryMenu() {
        inventory = new Inventory();
    }

    // --- Main Menu Display ---


    public void showMenu() {
        String menu = "==== INVENTORY MANAGER ====\n" +
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
                        JOptionPane.showMessageDialog(null, "Invalid option. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- Final receipt on exit ---
        String receipt = String.format("Thank you for using the system.\nTotal purchases this session: $%.2f", sessionTotalPurchase);
        JOptionPane.showMessageDialog(null, receipt, "Purchase Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- UI Methods for Each Menu Option ---

    private void uiAddProduct() {
        try {
            String name = JOptionPane.showInputDialog(null, "Enter product name:", "Add Product", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.trim().isEmpty()) return;

            double price = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter price:", "Add Product", JOptionPane.QUESTION_MESSAGE));
            int stock = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter initial stock:", "Add Product", JOptionPane.QUESTION_MESSAGE));

            if (inventory.addProduct(name, price, stock)) {
                JOptionPane.showMessageDialog(null, "Product added successfully.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Product '" + name + "' already exists.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price or stock. Please enter numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            // Catches cancellation on price/stock dialogs
            JOptionPane.showMessageDialog(null, "Operation cancelled.", "Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void uiListInventory() {
        StringBuilder listBuilder = new StringBuilder("==== CURRENT INVENTORY ====\n");
        listBuilder.append(String.format("%-20s %-10s %-10s\n", "PRODUCT", "PRICE", "STOCK"));
        listBuilder.append("----------------------------------------\n");

        ArrayList<String> names = inventory.getProductNames();
        double[] prices = inventory.getPrices();
        HashMap<String, Integer> stockMap = inventory.getStock();

        if (names.isEmpty()) {
            listBuilder.append("The inventory is empty.");
        } else {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                double price = prices[i];
                int stock = stockMap.get(name);
                listBuilder.append(String.format("%-20s $%-9.2f %-10d\n", name, price, stock));
            }
        }

        JOptionPane.showMessageDialog(null, listBuilder.toString(), "Product List", JOptionPane.PLAIN_MESSAGE);
    }

    private void uiPurchaseProduct() {
        try {
            String name = JOptionPane.showInputDialog(null, "Enter the name of the product to purchase:", "Purchase Product", JOptionPane.QUESTION_MESSAGE);
            if (name == null) return;

            int productIndex = inventory.findProductIndex(name);
            if (productIndex == -1) {
                JOptionPane.showMessageDialog(null, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter quantity to purchase:", "Purchase Product", JOptionPane.QUESTION_MESSAGE));
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int currentStock = inventory.getStock().get(name);
            if (quantity > currentStock) {
                JOptionPane.showMessageDialog(null, "Not enough stock. Available: " + currentStock, "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            inventory.getStock().put(name, currentStock - quantity);
            double unitPrice = inventory.getPrices()[productIndex];
            double totalCost = unitPrice * quantity;
            sessionTotalPurchase += totalCost;

            String confirmation = String.format("Purchase successful!\n%d x %s\nTotal: $%.2f", quantity, name, totalCost);
            JOptionPane.showMessageDialog(null, confirmation, "Purchase Confirmation", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uiShowStatistics() {
        double[] prices = inventory.getPrices();
        if (prices.length == 0) {
            JOptionPane.showMessageDialog(null, "No products to show statistics.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double mostExpensive = prices[0];
        double cheapest = prices[0];
        int expensiveIndex = 0;
        int cheapIndex = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > mostExpensive) {
                mostExpensive = prices[i];
                expensiveIndex = i;
            }
            if (prices[i] < cheapest) {
                cheapest = prices[i];
                cheapIndex = i;
            }
        }

        String expensiveName = inventory.getProductNames().get(expensiveIndex);
        String cheapName = inventory.getProductNames().get(cheapIndex);

        String stats = String.format(
                "==== PRICE STATISTICS ====\n" +
                        "Most Expensive: %s ($%.2f)\n" +
                        "Cheapest: %s ($%.2f)",
                expensiveName, mostExpensive, cheapName, cheapest
        );

        JOptionPane.showMessageDialog(null, stats, "Price Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void uiSearchProduct() {
        String searchTerm = JOptionPane.showInputDialog(null, "Enter name (or part of it) to search:", "Search", JOptionPane.QUESTION_MESSAGE);
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        StringBuilder results = new StringBuilder("Search results for '"+searchTerm+"':\n");
        boolean found = false;

        ArrayList<String> names = inventory.getProductNames();
        for (int i=0; i < names.size(); i++) {
            String currentName = names.get(i);
            if (currentName.toLowerCase().contains(searchTerm.toLowerCase())) {
                double price = inventory.getPrices()[i];
                int stock = inventory.getStock().get(currentName);
                results.append(String.format("- %s | Price: $%.2f | Stock: %d\n", currentName, price, stock));
                found = true;
            }
        }

        if (!found) {
            results.append("No products found matching the criteria.");
        }

        JOptionPane.showMessageDialog(null, results.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
}