// dao = komunikasi langsung dengan SQLite.
package com.mycompany.customermanagement.dao;

import com.mycompany.customermanagement.database.DatabaseConnection;
import com.mycompany.customermanagement.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//  DAO = menangani komunikasi data User dengan SQLite -> bertanggung jawab pada data users
public class UserDAO {
     // Mencari user berdasarkan username dan password
    public User login(String username, String password) {
         String sql = "SELECT id, username, password_hash, role "
                + "FROM users "
                + "WHERE username = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

             // Jika user ditemukan, ubah hasil database menjadi object User
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Jika login gagal
        return null;
    }
}
