/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Customer;
import com.mycompany.customermanagement.service.CustomerService;
import com.mycompany.customermanagement.util.AlertUtil;
 
import java.net.URL;
import java.util.ResourceBundle;
 
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerFormController implements Initializable{
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> categoryField;
    @FXML private ComboBox<String> statusField;
    @FXML private TextArea notesField;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    // Label kecil di bawah tiap field, dipakai buat pesan error validasi.
    // "managed=false" di FXML artinya defaultnya tidak makan tempat sama
    // sekali (bukan cuma disembunyikan) -- baru muncul dan makan tempat
    // waktu ada error yang perlu ditampilkan.
    @FXML private Label nameError;
    @FXML private Label phoneError;
    @FXML private Label emailError;
    @FXML private Label categoryError;
    @FXML private Label statusError;
 
    private final CustomerService customerService = new CustomerService();
 
    // null = mode "Tambah" (data baru). Kalau tidak null = mode "Edit"
    // (sedang mengubah data yang sudah ada, disimpan lewat setCustomer()
    // yang dipanggil dari CustomerController sebelum halaman ini ditampilkan)
    private Customer editingCustomer = null;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Isi pilihan dropdown Kategori & Status -- sesuaikan nanti kalau
        // kamu mau tambah/ubah daftar pilihannya
        categoryField.getItems().addAll("VIP", "Korporasi", "Langganan", "Baru", "Jarang");
        statusField.getItems().addAll("active", "inactive");
 
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
        
        // Paksa window jadi FULLSCREEN khusus di halaman ini dengan Set ExitHint terlebih dahulu sebelum fullscreen aktif
        Platform.runLater(() -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            if (stage != null) {
                stage.setFullScreenExitHint(""); // Sembunyikan pesan ESC
                stage.setFullScreen(true);
            }
        });
    }
 
    // Dipanggil dari CustomerController SEBELUM halaman form ini ditampilkan,
    // kalau user klik "Edit" (bukan "Tambah"). Ini yang bikin form otomatis
    // keisi data yang mau diedit.
    public void setCustomer(Customer customer) {
        this.editingCustomer = customer;
 
        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        addressField.setText(customer.getAddress());
        categoryField.setValue(customer.getCategory());
        statusField.setValue(customer.getStatus());
        notesField.setText(customer.getNotes());
    }
 
    private void handleSave(ActionEvent event) {
 
        // Bersihkan dulu semua tanda error dari percobaan submit sebelumnya
        clearError(nameField, nameError);
        clearError(phoneField, phoneError);
        clearError(emailField, emailError);
        clearError(categoryField, categoryError);
        clearError(statusField, statusError);
 
        boolean valid = true;
 
        if (nameField.getText() == null || nameField.getText().isBlank()) {
            showError(nameField, nameError, "Nama wajib diisi.");
            valid = false;
        }
 
        if (phoneField.getText() == null || phoneField.getText().isBlank()) {
            showError(phoneField, phoneError, "Telepon wajib diisi.");
            valid = false;
        }
 
        // Email boleh kosong, TAPI kalau diisi harus mengandung format wajar (ada '@' dan '.')
        String email = emailField.getText();
        if (email != null && !email.isBlank()
                && (!email.contains("@") || !email.contains("."))) {
            showError(emailField, emailError, "Format email tidak valid.");
            valid = false;
        }
 
        if (categoryField.getValue() == null) {
            showError(categoryField, categoryError, "Pilih kategori.");
            valid = false;
        }
 
        if (statusField.getValue() == null) {
            showError(statusField, statusError, "Pilih status.");
            valid = false;
        }
 
        if (!valid) {
            return; // berhenti di sini, tidak jadi simpan ke database
        }
 
        if (editingCustomer == null) {
            Customer newCustomer = new Customer(
                    0,
                    nameField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    categoryField.getValue(),
                    statusField.getValue(),
                    notesField.getText()
            );
            newCustomer.setEmail(email);
 
            customerService.save(newCustomer);
            AlertUtil.showInfo("Data pelanggan berhasil ditambahkan.");
 
        } else {
            editingCustomer.setName(nameField.getText());
            editingCustomer.setPhone(phoneField.getText());
            editingCustomer.setEmail(email);
            editingCustomer.setAddress(addressField.getText());
            editingCustomer.setCategory(categoryField.getValue());
            editingCustomer.setStatus(statusField.getValue());
            editingCustomer.setNotes(notesField.getText());
 
            customerService.update(editingCustomer);
            AlertUtil.showInfo("Data pelanggan berhasil diperbarui.");
        }
 
        backToCustomerList(event);
    }
    
    // Menandai 1 field sebagai error: kasih border merah + tampilkan pesan di bawahnya
    private void showError(Control field, Label errorLabel, String message) {
        if (!field.getStyleClass().contains("input-error")) {
            field.getStyleClass().add("input-error");
        }
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
 
    // Kebalikan dari showError() -- balikin field ke tampilan normal
    private void clearError(Control field, Label errorLabel) {
        field.getStyleClass().remove("input-error");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
 
 
    private void handleCancel(ActionEvent event) {
        backToCustomerList(event);
    }
 
    // Sama seperti pola handleBack di CustomerController -- balik ke
    // customer-list.fxml, kali ini pakai FXMLLoader (bukan bikin objek
    // Java langsung) karena customer-list.fxml memang berbasis FXML
    private void backToCustomerList(ActionEvent event) {
        try {
            // 1. Load FXML & buat Scene baru lebih dulu
            Parent listRoot = FXMLLoader.load(getClass().getResource("/fxml/customer-list.fxml"));
            Scene listScene = new Scene(listRoot, 800, 500);

            // 2. Ambil Stage yang sedang aktif
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            if (stage != null) {
                // 3. Ganti scene lebih dulu selagi masih fullscreen
                stage.setScene(listScene);
                stage.setTitle("Data Pelanggan - Customer Management System");

                // 4. Bungkus kembalinya ukuran window di Platform.runLater
                // Ini mencegah efek kedip/glitch saat keluar dari fullscreen
                Platform.runLater(() -> {
                    stage.setFullScreen(false);
                    stage.centerOnScreen();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
