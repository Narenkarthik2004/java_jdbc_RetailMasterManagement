package com.collection.GroceryShop;

public class Login {
    private static final String ADMIN_USERNAME = "NK_07";
    private static final String ADMIN_PASSWORD = "Naren@04";

    public static boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public static boolean isAdmin(String username, String password) {
        return authenticate(username, password);
    }
}