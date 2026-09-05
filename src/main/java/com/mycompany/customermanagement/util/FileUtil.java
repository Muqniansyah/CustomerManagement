package com.mycompany.customermanagement.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// Util untuk menyalin file yang di-upload user (misal bukti pembayaran)
// ke folder khusus di dalam project, supaya file aslinya boleh dipindah/
// dihapus dari lokasi semula tanpa bikin data di aplikasi ikut hilang.
public class FileUtil {

    // Menyalin "source" ke dalam folder tujuan, dengan nama baru yang
    // ditambahi angka waktu saat ini -- supaya tidak ada 2 file numpuk
    // dengan nama sama (misal 2 orang upload "bukti.jpg" di waktu beda).
    // Return: nama file baru (String) yang disimpan, untuk dicatat ke database.
    public static String copyToAppFolder(File source, String targetFolder) {
        try {
            File folder = new File(targetFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String newFileName = System.currentTimeMillis() + "_" + source.getName();

            Files.copy(
                source.toPath(),
                Paths.get(targetFolder, newFileName),
                StandardCopyOption.REPLACE_EXISTING
            );

            return newFileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}