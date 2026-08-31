/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.dao;

import com.mycompany.customermanagement.database.DatabaseConnection;
import com.mycompany.customermanagement.model.Interaction;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// DAO = menangani komunikasi data Interaction dengan SQLite -> bertanggung jawab pada data interactions
public class InteractionDAO {
    // Mengambil semua interaksi
    public List<Interaction> getAll() {
 
        List<Interaction> interactions = new ArrayList<>();
 
        String sql = "SELECT * FROM interactions ORDER BY interaction_date DESC";
 
        // PERBAIKAN: getConnection() -> connect()
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                interactions.add(mapInteraction(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return interactions;
    }
 
    // Mengambil interaksi milik customer tertentu
    public List<Interaction> getByCustomerId(int customerId) {
 
        List<Interaction> interactions = new ArrayList<>();
 
        // PERBAIKAN: text block -> string sambung (Java 11)
        String sql = "SELECT * FROM interactions "
                + "WHERE customer_id = ? "
                + "ORDER BY interaction_date DESC";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, customerId);
 
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                interactions.add(mapInteraction(rs));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return interactions;
    }
 
    // Menambahkan interaksi baru
    public void save(Interaction interaction) {
 
        // PERBAIKAN: tambah kolom "notes" -- kemarin kita tambahkan field notes
        // di Interaction.java, jadi di sini juga perlu ikut disimpan, kalau tidak
        // catatan yang diketik user tidak akan pernah tersimpan ke database
        String sql = "INSERT INTO interactions "
                + "(customer_id, interaction_date, type, description, notes) "
                + "VALUES (?, ?, ?, ?, ?)";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, interaction.getCustomerId());
            stmt.setString(2, interaction.getInteractionDate());
            stmt.setString(3, interaction.getType());
            stmt.setString(4, interaction.getDescription());
            stmt.setString(5, interaction.getNotes());
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah interaksi
    public void update(Interaction interaction) {
 
        // PERBAIKAN: sama seperti save(), tambah kolom notes
        String sql = "UPDATE interactions "
                + "SET customer_id = ?, interaction_date = ?, "
                + "type = ?, description = ?, notes = ? "
                + "WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, interaction.getCustomerId());
            stmt.setString(2, interaction.getInteractionDate());
            stmt.setString(3, interaction.getType());
            stmt.setString(4, interaction.getDescription());
            stmt.setString(5, interaction.getNotes());
            stmt.setInt(6, interaction.getId());
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Menghapus interaksi
    public void delete(int id) {
 
        String sql = "DELETE FROM interactions WHERE id = ?";
 
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
 
            stmt.executeUpdate();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Mengubah hasil database menjadi object Interaction
    private Interaction mapInteraction(ResultSet rs) throws Exception {
 
        Interaction interaction = new Interaction(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getString("interaction_date"),
            rs.getString("type"),
            rs.getString("description")
        );
 
        // PERBAIKAN: sama seperti mapCustomer() di CustomerDAO -- constructor
        // Interaction belum menerima notes, jadi di-set manual pakai setter di sini
        interaction.setNotes(rs.getString("notes"));
 
        return interaction;
    }
}
