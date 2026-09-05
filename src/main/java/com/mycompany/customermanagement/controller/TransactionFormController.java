/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Customer;
import com.mycompany.customermanagement.model.Transaction;
import com.mycompany.customermanagement.service.CustomerService;
import com.mycompany.customermanagement.service.TransactionService;
import com.mycompany.customermanagement.util.AlertUtil;
 
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TransactionFormController implements Initializable{
    @FXML private ComboBox<Customer> customerField;
    @FXML private DatePicker dateField;
    @FXML private TextField totalField;
    @FXML private ComboBox<String> paymentStatusField;
    @FXML private TextField paymentProofField;
    @FXML private TextArea notesField;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    @FXML private Label customerError;
    @FXML private Label dateError;
    @FXML private Label totalError;
    @FXML private Label paymentStatusError;
 
    private final CustomerService customerService = new CustomerService();
    private final TransactionService transactionService = new TransactionService();
 
    // Format tanggal dipakai konsisten: DatePicker (objek LocalDate) <-> database (String)
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
    private Transaction editingTransaction = null;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Isi dropdown Pelanggan dari data yang sudah ada
        List<Customer> customers = customerService.getAll();
        customerField.setItems(FXCollections.observableArrayList(customers));
 
        // PENTING: tanpa ini, ComboBox<Customer> akan nampilin teks aneh
        // (kode memori objek) bukan nama pelanggan. Converter ini yang
        // "ngajarin" ComboBox cara nampilin Customer sebagai teks nama-nya.
        customerField.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer == null ? "" : customer.getName();
            }
 
            @Override
            public Customer fromString(String string) {
                return null; // tidak dipakai, karena user cuma pilih dari daftar, tidak ngetik manual
            }
        });
 
        paymentStatusField.getItems().addAll("Lunas", "Belum Lunas", "Sebagian");
 
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
    }
 
    // Dipanggil dari TransactionController kalau mode Edit
    public void setTransaction(Transaction transaction) {
        this.editingTransaction = transaction;
 
        // Cari Customer yang id-nya cocok, supaya ComboBox auto-select pelanggan yang benar
        for (Customer c : customerField.getItems()) {
            if (c.getId() == transaction.getCustomerId()) {
                customerField.setValue(c);
                break;
            }
        }
 
        dateField.setValue(LocalDate.parse(transaction.getTransactionDate(), DATE_FORMAT));
        totalField.setText(String.valueOf(transaction.getTotalAmount()));
        paymentStatusField.setValue(transaction.getPaymentStatus());
        paymentProofField.setText(transaction.getPaymentProof());
        notesField.setText(transaction.getNotes());
    }
 
    private void handleSave(ActionEvent event) {
 
        clearError(customerField, customerError);
        clearError(dateField, dateError);
        clearError(totalField, totalError);
        clearError(paymentStatusField, paymentStatusError);
 
        boolean valid = true;
 
        if (customerField.getValue() == null) {
            showError(customerField, customerError, "Pilih pelanggan.");
            valid = false;
        }
 
        if (dateField.getValue() == null) {
            showError(dateField, dateError, "Tanggal wajib diisi.");
            valid = false;
        }
 
        double total = 0;
        if (totalField.getText() == null || totalField.getText().isBlank()) {
            showError(totalField, totalError, "Total wajib diisi.");
            valid = false;
        } else {
            try {
                total = Double.parseDouble(totalField.getText());
                if (total <= 0) {
                    showError(totalField, totalError, "Total harus lebih besar dari 0.");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                showError(totalField, totalError, "Total harus berupa angka.");
                valid = false;
            }
        }
 
        if (paymentStatusField.getValue() == null) {
            showError(paymentStatusField, paymentStatusError, "Pilih status pembayaran.");
            valid = false;
        }
 
        if (!valid) {
            return;
        }
 
        int customerId = customerField.getValue().getId();
        String dateStr = dateField.getValue().format(DATE_FORMAT);
 
        if (editingTransaction == null) {
            Transaction newTransaction = new Transaction(
                    0,
                    customerId,
                    dateStr,
                    total,
                    paymentStatusField.getValue(),
                    paymentProofField.getText(),
                    notesField.getText()
            );
            transactionService.save(newTransaction);
            AlertUtil.showInfo("Transaksi berhasil ditambahkan.");
 
        } else {
            editingTransaction.setCustomerId(customerId);
            editingTransaction.setTransactionDate(dateStr);
            editingTransaction.setTotalAmount(total);
            editingTransaction.setPaymentStatus(paymentStatusField.getValue());
            editingTransaction.setPaymentProof(paymentProofField.getText());
            editingTransaction.setNotes(notesField.getText());
 
            transactionService.update(editingTransaction);
            AlertUtil.showInfo("Transaksi berhasil diperbarui.");
        }
 
        backToTransactionList(event);
    }
 
    private void showError(Control field, Label errorLabel, String message) {
        if (!field.getStyleClass().contains("input-error")) {
            field.getStyleClass().add("input-error");
        }
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
 
    private void clearError(Control field, Label errorLabel) {
        field.getStyleClass().remove("input-error");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
 
    private void handleCancel(ActionEvent event) {
        backToTransactionList(event);
    }
 
    private void backToTransactionList(ActionEvent event) {
        try {
            Parent listRoot = FXMLLoader.load(getClass().getResource("/fxml/transaction-list.fxml"));
            Scene listScene = new Scene(listRoot, 800, 500);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(listScene);
            stage.setTitle("Transaksi - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
