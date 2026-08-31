package com.mycompany.customermanagement.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//Menyiapkan dan mengatur koneksi ke SQLite
public class DatabaseConnection {

    private static final String DB_FOLDER = "data";
    private static final String DB_NAME = "customer.db";
    private static final String URL = "jdbc:sqlite:" + DB_FOLDER + File.separator + DB_NAME;

    public static Connection connect() throws SQLException {
        ensureDataFolderExists();
        return DriverManager.getConnection(URL);
    }

    private static void ensureDataFolderExists() {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void initializeDatabase() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password_hash TEXT NOT NULL,"
                + "role TEXT NOT NULL DEFAULT 'admin',"
                + "created_at TEXT DEFAULT CURRENT_TIMESTAMP"
                + ");";

        String createCustomers = "CREATE TABLE IF NOT EXISTS customers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "phone TEXT,"
                + "email TEXT,"
                + "address TEXT,"
                + "category TEXT,"
                + "status TEXT NOT NULL DEFAULT 'active',"
                + "notes TEXT,"
                + "created_at TEXT DEFAULT CURRENT_TIMESTAMP,"
                + "updated_at TEXT DEFAULT CURRENT_TIMESTAMP"
                + ");";

        String createTransactions = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "customer_id INTEGER NOT NULL,"
                + "transaction_date TEXT NOT NULL,"
                + "total_amount REAL,"
                + "payment_status TEXT,"
                + "payment_proof TEXT,"
                + "notes TEXT,"
                + "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE"
                + ");";

        String createInteractions = "CREATE TABLE IF NOT EXISTS interactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "customer_id INTEGER NOT NULL,"
                + "interaction_date TEXT NOT NULL,"
                + "type TEXT,"
                + "description TEXT,"
                + "notes TEXT,"
                + "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(createUsers);
            stmt.execute(createCustomers);
            stmt.execute(createTransactions);
            stmt.execute(createInteractions);
            
            // Membuat user percobaan (admin/admin) kalau belum ada, supaya bisa dites login
            stmt.execute("INSERT OR IGNORE INTO users (username, password_hash, role) "
                    + "VALUES ('admin', 'admin', 'admin')");
            System.out.println("Database berhasil diinisialisasi.");

        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
        }
    }
}