package com.example.shoppingapi;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {
    public static void load() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
    }
}
