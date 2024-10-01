package com.collection.GroceryShop;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventary inventory = new Inventary();
        boolean loggedIn = false;
        boolean isAdmin = false;
        while (true) {
            if (!loggedIn) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                isAdmin = Login.isAdmin(username, password);

                if (isAdmin) {
                    System.out.println("Admin access granted.");
                } else {
                    System.out.println("User access granted.");
                }
                loggedIn = true;
            }
            while (loggedIn) {
                try {
                    System.out.println("\nNaren's Grocery Shop");
                    if (isAdmin) {
                        System.out.println("1. Add Item");
                        System.out.println("2. Update Item");
                        System.out.println("3. Delete Item");
                        System.out.println("4. Show Items");
                        System.out.println("5. Search Item");
                        System.out.println("6. Logout");
                        System.out.println("7. Exit");
                    } else {
                        System.out.println("1. Show Items");
                        System.out.println("2. Search Item");
                        System.out.println("3. Logout (or) 7. Exit");
                    }

                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                        
                            if (isAdmin) {
                                inventory.addItem();
                            } else {
                                inventory.displayItems();
                            }
                            break;
                        case 2:
                            if (isAdmin) {
                                inventory.updateItem();
                            } else {
                                inventory.searchItem();
                            }
                            break;
                        case 3:
                            if (isAdmin) {
                                inventory.deleteItem();
                            } else {
                                System.out.println("Logging out...");
                                loggedIn = false;
                            }
                            break;
                        case 4:
                            if (isAdmin) {
                                inventory.displayItems();
                            } else {
                                System.out.println("Invalid option. Please try again.");
                            }
                            break;
                        case 5:
                            if (isAdmin) {
                                inventory.searchItem();
                            } else {
                                System.out.println("Invalid option. Please try again.");
                            }
                            break;
                        case 6:
                            if (isAdmin) {
                                System.out.println("Logging out...");
                                loggedIn = false;
                            } else {
                                System.out.println("Invalid option. Please try again.");
                            }
                            break;
                        case 7:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter the correct data type.");
                    scanner.nextLine(); // Clear the buffer
                } catch (ItemNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred.");
                }
            }
        }
    }
}
