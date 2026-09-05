package com.mycompany.customermanagement.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Util = kumpulan method bantu yang dipakai di banyak tempat.
// Class ini khusus urusan keamanan password.
public class PasswordUtil {

    // Mengubah password polos (misal "admin") jadi kode acak (hash)
    // yang aman disimpan ke database. Proses ini SATU ARAH -- tidak bisa
    // dibalik dari hash ke password asli, makanya aman kalau database bocor.
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

            // Ubah dari bentuk byte (angka) jadi teks heksadesimal yang gampang disimpan
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 sudah pasti tersedia di semua JVM, jadi error ini
            // secara praktik tidak akan pernah kejadian
            throw new RuntimeException("Algoritma SHA-256 tidak ditemukan", e);
        }
    }

    // Mengecek apakah password yang diketik user (plain text) cocok dengan
    // hash yang tersimpan di database. Caranya: hash ulang password yang
    // diketik, lalu bandingkan hasilnya dengan hash yang tersimpan.
    public static boolean verify(String plainPassword, String storedHash) {
        String hashOfInput = hash(plainPassword);
        return hashOfInput.equals(storedHash);
    }
}