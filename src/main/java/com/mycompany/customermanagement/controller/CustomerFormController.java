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
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
 
    private final CustomerService customerService = new CustomerService();
 
    // null = mode "Tambah" (data baru). Kalau tidak null = mode "Edit"
    // (sedang mengubah data yang sudah ada, disimpan lewat setCustomer()
    // yang dipanggil dari CustomerController sebelum halaman ini ditampilkan)
    private Customer editingCustomer = null;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Isi pilihan dropdown Kategori & Status -- sesuaikan nanti kalau
        // kamu mau tambah/ubah daftar pilihannya
        categoryField.getItems().addAll("Reguler", "VIP", "Corporate");
        statusField.getItems().addAll("active", "inactive");
 
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
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
 
        // Validasi paling dasar -- Nama wajib diisi
        if (nameField.getText() == null || nameField.getText().isBlank()) {
            AlertUtil.showError("Nama pelanggan wajib diisi.");
            return;
        }
 
        if (editingCustomer == null) {
            // MODE TAMBAH: bikin object Customer baru.
            // id diisi 0 karena nanti otomatis di-generate oleh database (AUTOINCREMENT)
            Customer newCustomer = new Customer(
                    0,
                    nameField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    categoryField.getValue(),
                    statusField.getValue(),
                    notesField.getText()
            );
            newCustomer.setEmail(emailField.getText());
 
            customerService.save(newCustomer);
            AlertUtil.showInfo("Data pelanggan berhasil ditambahkan.");
 
        } else {
            // MODE EDIT: pakai object yang sudah ada (editingCustomer),
            // tinggal update field-nya dengan nilai terbaru dari form
            editingCustomer.setName(nameField.getText());
            editingCustomer.setPhone(phoneField.getText());
            editingCustomer.setEmail(emailField.getText());
            editingCustomer.setAddress(addressField.getText());
            editingCustomer.setCategory(categoryField.getValue());
            editingCustomer.setStatus(statusField.getValue());
            editingCustomer.setNotes(notesField.getText());
 
            customerService.update(editingCustomer);
            AlertUtil.showInfo("Data pelanggan berhasil diperbarui.");
        }
 
        backToCustomerList(event);
    }
 
    private void handleCancel(ActionEvent event) {
        backToCustomerList(event);
    }
 
    // Sama seperti pola handleBack di CustomerController -- balik ke
    // customer-list.fxml, kali ini pakai FXMLLoader (bukan bikin objek
    // Java langsung) karena customer-list.fxml memang berbasis FXML
    private void backToCustomerList(ActionEvent event) {
        try {
            Parent listRoot = FXMLLoader.load(getClass().getResource("/fxml/customer-list.fxml"));
            Scene listScene = new Scene(listRoot, 800, 500);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(listScene);
            stage.setTitle("Data Pelanggan - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
