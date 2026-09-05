package com.mycompany.customermanagement.dao;

import com.mycompany.customermanagement.database.DatabaseConnection;
import com.mycompany.customermanagement.model.Customer;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// DAO = menangani komunikasi data Customer dengan SQLite -> bertanggung jawab pada data customers
public class CustomerDAO {
    // Mengambil semua data pelanggan
    public List<Customer> getAll() {
 
        List<Customer> customers = new ArrayList<>();
 
        String sql = "SELECT * FROM customers ORDER BY name";
 
        // PERBAIKAN: getConnection() diganti jadi connect(), sesuai nama method
        // yang sebenarnya ada di DatabaseConnection.java
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return customers;
    }
 
    // Mencari pelanggan berdasarkan nama atau nomor telepon
    public List<Customer> search(String keyword) {
 
        List<Customer> customers = new ArrayList<>();
 
        // PERBAIKAN: text block """...""" diganti jadi string sambung "..." + "..."
        // karena pom.xml masih Java 11 (text block baru ada di Java 15+)
        String sql = "SELECT * FROM customers "
                + "WHERE name LIKE ? OR phone LIKE ? "
                + "ORDER BY name";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            String searchKeyword = "%" + keyword + "%";
 
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
 
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return customers;
    }
    
     // mengambil pelanggan berdasarkan status keaktifan (active/inactive)
    public List<Customer> filterByStatus(String status) {
 
        List<Customer> customers = new ArrayList<>();
 
        String sql = "SELECT * FROM customers WHERE status = ? ORDER BY name";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, status);
 
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return customers;
    }
 
    // Menambahkan pelanggan baru
    public void save(Customer customer) {
 
        // PERBAIKAN: tambah kolom "email" di INSERT, karena Customer.java
        // sekarang sudah punya field email (kemarin kita tambahkan)
        String sql = "INSERT INTO customers "
                + "(name, phone, email, address, category, status, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());     // urutan ? harus sama persis dengan urutan kolom di atas
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getCategory());
            stmt.setString(6, customer.getStatus());
            stmt.setString(7, customer.getNotes());
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah data pelanggan
    public void update(Customer customer) {
 
        // PERBAIKAN: sama seperti save(), tambah kolom email di UPDATE
        String sql = "UPDATE customers "
                + "SET name = ?, phone = ?, email = ?, address = ?, "
                + "category = ?, status = ?, notes = ? "
                + "WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getCategory());
            stmt.setString(6, customer.getStatus());
            stmt.setString(7, customer.getNotes());
            stmt.setInt(8, customer.getId());           // id di paling akhir, sesuai posisi WHERE id = ?
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Menghapus pelanggan berdasarkan ID
    public void delete(int id) {
 
        String sql = "DELETE FROM customers WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah hasil database (1 baris tabel) menjadi object Customer
    private Customer mapCustomer(ResultSet rs) throws Exception {
 
        Customer customer = new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("category"),
            rs.getString("status"),
            rs.getString("notes")
        );
 
        // PERBAIKAN: constructor Customer belum menerima email (constructor lama,
        // sebelum email ditambahkan), jadi email di-set manual pakai setter di sini
        customer.setEmail(rs.getString("email"));
 
        return customer;
    }
}
