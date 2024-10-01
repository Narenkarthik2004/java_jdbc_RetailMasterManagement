package com.collection.GroceryShop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Inventary {
    private Scanner scanner;
    Statement stmt;
    ResultSet rs;
    dbutil db = new dbutil();
    Connection con;

    public Inventary() {
        new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addItem() {
        try {
            System.out.print("Enter item type: ");
            String itemType = scanner.nextLine();

            System.out.print("Enter item name: ");
            String name = scanner.nextLine();

            System.out.print("Enter item quantity: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter item price: ");
            int price = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Item newItem = new Item(generateId(name, quantity), itemType, name, quantity, price);

            Connection con = db.getDBConnection();
            Statement stmt = con.createStatement();
            String qry = "INSERT INTO grocery(itemType, name, quantity, price) VALUES('" +
                    newItem.getItemType() + "', '" +
                    newItem.getName() + "', " +
                    newItem.getQuantity() + ", " +
                    newItem.getPrice() + ")";
            int count = stmt.executeUpdate(qry);
            if (count > 0) {
                System.out.println("Item added successfully.");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private String generateId(String name, int quantity) {
        String idPart = name.length() > 3 ? name.substring(0, 3) : name;
        return idPart.toUpperCase() + quantity;
    }

    public void updateItem() throws ItemNotFoundException {
        try {
            System.out.print("Enter item ID to update: ");
            String id = scanner.nextLine();

            Item item = findItemById(id);  // Fetch from DB
            if (item == null) {
                throw new ItemNotFoundException("Item with ID " + id + " not found.");
            }

            boolean updating = true;

            while (updating) {
                System.out.println("Current details: ");
                System.out.println("Item Type: " + (item.getItemType() != null ? item.getItemType() : "Not set"));
                System.out.println("Item Name: " + (item.getName() != null ? item.getName() : "Not set"));
                System.out.println("Item Quantity: " + item.getQuantity());
                System.out.println("Item Price: " + item.getPrice());

                System.out.println("Which field do you want to update?");
                System.out.println("1. Item Type");
                System.out.println("2. Item Name");
                System.out.println("3. Item Quantity");
                System.out.println("4. Item Price");
                System.out.println("5. Finish updating");

                System.out.print("Enter the number corresponding to the field: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter new item type (or press Enter to keep current): ");
                        String newItemType = scanner.nextLine();
                        if (!newItemType.isEmpty()) {
                            item.setItemType(newItemType);
                        }
                        break;
                    case 2:
                        System.out.print("Enter new item name (or press Enter to keep current): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            item.setName(newName);
                        }
                        break;
                    case 3:
                        System.out.print("Enter new item quantity (or press Enter to keep current): ");
                        String quantityInput = scanner.nextLine();
                        if (!quantityInput.isEmpty()) {
                            item.setQuantity(Integer.parseInt(quantityInput));
                        }
                        break;
                    case 4:
                        System.out.print("Enter new item price (or press Enter to keep current): ");
                        String priceInput = scanner.nextLine();
                        if (!priceInput.isEmpty()) {
                            item.setPrice(Integer.parseInt(priceInput));
                        }
                        break;
                    case 5:
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

            // Update the database with the new values
            try (Connection con = db.getDBConnection();
                 Statement stmt = con.createStatement()) {

                String qry = "UPDATE grocery SET itemType = '" + (item.getItemType() != null ? item.getItemType() : item.getItemType()) + 
                             "', name = '" + (item.getName() != null ? item.getName() : item.getName()) + 
                             "', quantity = " + item.getQuantity() + 
                             ", price = " + item.getPrice() + 
                             " WHERE id = '" + id + "'";
                
                int count = stmt.executeUpdate(qry);
                if (count > 0) {
                    System.out.println("Item updated successfully.");
                } else {
                    System.out.println("Failed to update item.");
                }
            }
        } catch (InputMismatchException e) {
            handleInvalidInputException("Invalid input for quantity or price.");
        } catch (Exception e) {
            handleRuntimeException("An unexpected error occurred while updating item.");
        }
    }

    public void deleteItem() throws ItemNotFoundException {
        try {
            System.out.print("Enter item ID to delete: ");
            String id = scanner.nextLine();

            // Check if the item exists
            Item item = findItemById(id);
            if (item == null) {
                throw new ItemNotFoundException("Item with ID " + id + " not found.");
            }

            try (Connection con = db.getDBConnection();
                 Statement stmt = con.createStatement()) {

                String qry = "DELETE FROM grocery WHERE id = '" + id + "'";
                int count = stmt.executeUpdate(qry);
                if (count > 0) {
                    System.out.println("Item deleted successfully.");
                } else {
                    System.out.println("Failed to delete item.");
                }
            }

        } catch (Exception e) {
            handleRuntimeException("An unexpected error occurred while deleting item.");
        }
    }


    public void searchItem() {
        boolean continueSearching = true;

        while (continueSearching) {
            try {
                // Step 1: Ask for item type
                System.out.println("Select item type to search: ");
                System.out.println("1. Fruits");
                System.out.println("2. Vegetables");
                System.out.println("3. Snacks");
                System.out.println("4. Stationery");
                System.out.println("5. Kitchen Appliances");
                System.out.println("6. Cleaning Supplies");
                System.out.println("7. Personal Care");
                System.out.println("8. Beverages");
                System.out.println("9. Frozen Foods");
                System.out.println("10. Dairy Products");
                System.out.println("11. Search by Name");
                System.out.println("12. Exit");

                System.out.print("Enter the number corresponding to the item type: ");
                int typeChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                String selectedType = "";
                boolean searchByName = false;

                switch (typeChoice) {
                    case 1: selectedType = "Fruits"; break;
                    case 2: selectedType = "Vegetables"; break;
                    case 3: selectedType = "Snacks"; break;
                    case 4: selectedType = "Stationery"; break;
                    case 5: selectedType = "Kitchen Appliances"; break;
                    case 6: selectedType = "Cleaning Supplies"; break;
                    case 7: selectedType = "Personal Care"; break;
                    case 8: selectedType = "Beverages"; break;
                    case 9: selectedType = "Frozen Foods"; break;
                    case 10: selectedType = "Dairy Products"; break;
                    case 11: 
                        searchByName = true; // Search by name
                        break;
                    case 12:
                        System.out.println("Exiting search.");
                        continueSearching = false;
                        continue; // Exit the loop
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue; // Continue the loop for valid input
                }

                // Step 2: If searching by type
                if (!searchByName) {
                    System.out.println("Items of type " + selectedType + ":");
                    boolean typeFound = false;

                    try (Connection con = db.getDBConnection();
                         Statement stmt = con.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT * FROM grocery WHERE itemType = '" + selectedType + "'")) {

                        while (rs.next()) {
                            System.out.println("ID: " + rs.getString(1) + ", Type: " + rs.getString(2) +
                                               ", Name: " + rs.getString(3) + ", Quantity: " + rs.getInt(4) +
                                               ", Price: " + rs.getInt(5));
                            typeFound = true;
                        }
                    }

                    if (!typeFound) {
                        System.out.println("No items found of type: " + selectedType);
                    }
                } else {
                    // Step 3: If searching by name
                    System.out.print("Enter item name to search: ");
                    String itemName = scanner.nextLine().trim();
                    boolean nameFound = false;

                    try (Connection con = db.getDBConnection();
                         Statement stmt = con.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT * FROM grocery WHERE name LIKE '%" + itemName + "%'")) {

                        while (rs.next()) {
                            System.out.println("ID: " + rs.getString(1) + ", Type: " + rs.getString(2) +
                                               ", Name: " + rs.getString(3) + ", Quantity: " + rs.getInt(4) +
                                               ", Price: " + rs.getInt(5));
                            nameFound = true;
                        }
                    }

                    if (!nameFound) {
                        System.out.println("No items found with the name: " + itemName);
                    }
                }

            } catch (Exception e) {
                handleRuntimeException("An unexpected error occurred while searching for items: " + e.getMessage());
            }
        }
    }


    public void displayItems() {
        try (Connection con = db.getDBConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM grocery")) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getString(1) + ", Type: " + rs.getString(2) +
                                   ", Name: " + rs.getString(3) + ", Quantity: " + rs.getInt(4) +
                                   ", Price: " + rs.getInt(5));
            }

        } catch (Exception ex) {
            System.out.println("Error while displaying items: " + ex.getMessage());
        }
    }
    private Item findItemById(String id) {
        try {
            con = db.getDBConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM grocery WHERE id = '" + id + "'");
            if (rs.next()) {
                return new Item(rs.getString("id"), rs.getString("itemType"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("price"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void handleInvalidInputException(String message) {
        try {
            throw new InvalidInputException(message);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRuntimeException(String message) {
        System.out.println(message);
    }
}
