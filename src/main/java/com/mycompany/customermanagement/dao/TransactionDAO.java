/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.dao;

import com.mycompany.customermanagement.database.DatabaseConnection;
import com.mycompany.customermanagement.model.Transaction;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// DAO = menangani komunikasi data Transaction dengan SQLite -> bertanggung jawab pada data transactions
public class TransactionDAO {
    // Mengambil semua transaksi
    public List<Transaction> getAll() {
 
        List<Transaction> transactions = new ArrayList<>();
 
        // di-JOIN ke tabel customers supaya bisa ambil nama pelanggannya juga.
        // "t." dan "c." itu alias -- cara singkat nyebut "transactions" dan "customers"
        String sql = "SELECT t.*, c.name AS customer_name "
                + "FROM transactions t "
                + "JOIN customers c ON t.customer_id = c.id "
                + "ORDER BY t.transaction_date DESC";
 
        // PERBAIKAN: getConnection() -> connect(), sama seperti DAO sebelumnya
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                transactions.add(mapTransaction(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return transactions;
    }
 
    // Mengambil transaksi milik customer tertentu (juga di-JOIN, untuk konsistensi)
    public List<Transaction> getByCustomerId(int customerId) {
 
        List<Transaction> transactions = new ArrayList<>();
 
        String sql = "SELECT t.*, c.name AS customer_name "
                + "FROM transactions t "
                + "JOIN customers c ON t.customer_id = c.id "
                + "WHERE t.customer_id = ? "
                + "ORDER BY t.transaction_date DESC";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, customerId);
 
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                transactions.add(mapTransaction(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return transactions;
    }
 
    // Menambahkan transaksi baru
    public void save(Transaction transaction) {
 
        // PERBAIKAN PENTING: kolom "type", "amount", "description" diganti jadi
        // "total_amount", "payment_status", "payment_proof", "notes" -- ini nama
        // kolom yang benar-benar ada di tabel transactions (lihat DatabaseConnection.java)
        String sql = "INSERT INTO transactions "
                + "(customer_id, transaction_date, total_amount, payment_status, payment_proof, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, transaction.getCustomerId());
            stmt.setString(2, transaction.getTransactionDate());
            // PERBAIKAN: getAmount() tidak ada di Transaction.java, yang benar getTotalAmount()
            stmt.setDouble(3, transaction.getTotalAmount());
            // PERBAIKAN: getType() diganti getPaymentStatus() (field yang memang ada di model)
            stmt.setString(4, transaction.getPaymentStatus());
            stmt.setString(5, transaction.getPaymentProof());
            // PERBAIKAN: getDescription() tidak ada, diganti getNotes()
            stmt.setString(6, transaction.getNotes());
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah transaksi
    public void update(Transaction transaction) {
 
        // PERBAIKAN: sama seperti save(), kolom disesuaikan ke struktur tabel yang benar
        String sql = "UPDATE transactions "
                + "SET customer_id = ?, transaction_date = ?, "
                + "total_amount = ?, payment_status = ?, payment_proof = ?, notes = ? "
                + "WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, transaction.getCustomerId());
            stmt.setString(2, transaction.getTransactionDate());
            stmt.setDouble(3, transaction.getTotalAmount());
            stmt.setString(4, transaction.getPaymentStatus());
            stmt.setString(5, transaction.getPaymentProof());
            stmt.setString(6, transaction.getNotes());
            stmt.setInt(7, transaction.getId());
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Menghapus transaksi
    public void delete(int id) {
 
        String sql = "DELETE FROM transactions WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah hasil database menjadi object Transaction
    private Transaction mapTransaction(ResultSet rs) throws Exception {
        Transaction transaction = new Transaction(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getString("transaction_date"),
            rs.getDouble("total_amount"),
            rs.getString("payment_status"),
            rs.getString("payment_proof"),
            rs.getString("notes")
        );
 
        // PERBAIKAN: ambil "customer_name" hasil JOIN, taruh ke field tambahan
        transaction.setCustomerName(rs.getString("customer_name"));
 
        return transaction;
    }
}
