package com.mycompany.customermanagement.util;

import com.mycompany.customermanagement.model.Customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Util = kumpulan method bantu. Class ini khusus urusan export data ke CSV.
// CSV = Comma Separated Values, format teks sederhana yang bisa dibuka
// langsung di Excel/Google Sheets.
public class ExportUtil {

    public static boolean exportCustomersToCsv(List<Customer> customers, File file) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            // Menjadikan Excel otomatis membaca delimiter titik koma secara konsisten
            writer.write("sep=;");
            writer.newLine();
            
            // Baris pertama = judul kolom & Gunakan titik koma sebagai pemisah header
            writer.write("ID;Nama;Telepon;Email;Alamat;Kategori;Status;Catatan");
            writer.newLine();

            // Satu baris per pelanggan & Gunakan titik koma sebagai pemisah baris data
            for (Customer c : customers) {
                writer.write(String.join(";",
                    String.valueOf(c.getId()),
                    escapeCsv(c.getName()),
                    formatPhoneNumber(c.getPhone()), // Memaksa Excel membaca telepon sebagai teks murni
                    escapeCsv(c.getEmail()),
                    escapeCsv(c.getAddress()),
                    escapeCsv(c.getCategory()),
                    escapeCsv(c.getStatus()),
                    escapeCsv(c.getNotes())
                ));
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Format khusus agar nomor telepon dianggap teks oleh Excel (mencegah format 8,69E+12)
    private static String formatPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return "";
        }
        return "=\"" + phone + "\"";
    }

    // Melindungi data yang kebetulan mengandung koma/petik/baris baru,
    // supaya tidak merusak struktur CSV (misal Catatan berisi "beli, lalu bayar")
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(";") || value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}