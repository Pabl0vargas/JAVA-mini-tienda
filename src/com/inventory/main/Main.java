package com.inventory.main;

import com.inventory.ui.InventoryMenu;


  //The entry point of the application.
  //Its only responsibility is to start the program.

public class Main {
    public static void main(String[] args) {
        // Create an instance of the menu and display it.
        InventoryMenu menu = new InventoryMenu();
        menu.showMenu();
    }
}