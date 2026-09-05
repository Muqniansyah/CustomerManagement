// dao = komunikasi langsung dengan SQLite.
package com.mycompany.customermanagement.dao;

import com.mycompany.customermanagement.database.DatabaseConnection;
import com.mycompany.customermanagement.model.User;
import com.mycompany.customermanagement.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//  DAO = menangani komunikasi data User dengan SQLite -> bertanggung jawab pada data users
public class UserDAO {

    // Mencari user berdasarkan username dan password
    public User login(String username, String password) {

        // PERBAIKAN: query sekarang HANYA cari berdasarkan username.
        // Password TIDAK dibandingkan di SQL lagi, karena yang tersimpan
        // di database adalah hash, sedangkan yang diketik user adalah
        // password polos -- dua hal itu tidak bisa dibandingkan langsung
        // pakai "=" di SQL.
        String sql = "SELECT id, username, password_hash, role "
                + "FROM users "
                + "WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                // PERBAIKAN: verifikasi password dilakukan di sini, di Java,
                // pakai PasswordUtil -- bukan lagi dibandingkan di query SQL
                if (PasswordUtil.verify(password, storedHash)) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        storedHash,
                        rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Kalau username tidak ketemu, ATAU username ketemu tapi password salah
        return null;
    }
}