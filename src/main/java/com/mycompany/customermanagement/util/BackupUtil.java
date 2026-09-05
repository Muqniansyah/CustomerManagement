package com.mycompany.customermanagement.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Util = kumpulan method bantu. Class ini khusus urusan backup database.
public class BackupUtil {

    private static final String DB_PATH = "data/customer.db";
    private static final String BACKUP_FOLDER = "backup";

    // Menyalin file customer.db ke folder backup/, dengan nama yang
    // menyertakan tanggal+jam saat itu, supaya tidak menimpa backup lama.
    // Return: true kalau berhasil, false kalau gagal.
    public static boolean backupDatabase() {
        try {
            File backupDir = new File(BACKUP_FOLDER);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String backupFileName = "customer_backup_" + timestamp + ".db";

            Files.copy(
                Paths.get(DB_PATH),
                Paths.get(BACKUP_FOLDER, backupFileName),
                StandardCopyOption.REPLACE_EXISTING
            );

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}