/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Transaction;
import com.mycompany.customermanagement.service.TransactionService;
import com.mycompany.customermanagement.util.AlertUtil;
 
import java.net.URL;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TransactionController implements Initializable {
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colCustomer;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, Double> colTotal;
    @FXML private TableColumn<Transaction, String> colPaymentStatus;
    @FXML private TableColumn<Transaction, String> colPaymentProof;
    @FXML private TableColumn<Transaction, String> colNotes;
 
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;
 
    private final TransactionService transactionService = new TransactionService();
    private final ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Sama seperti InteractionController -- nama di sini harus cocok
        // dengan getter di Transaction.java
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colPaymentProof.setCellValueFactory(new PropertyValueFactory<>("paymentProof"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
 
        transactionTable.setItems(transactionData);
        // fix kolom tiak konsisten
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
 
        btnDelete.setOnAction(this::handleDelete);
        btnBack.setOnAction(this::handleBack);
 
        // pindah ke transaction-form.fxml
        btnAdd.setOnAction(this::handleAdd);
        btnEdit.setOnAction(this::handleEdit);
 
        loadData();
    }
 
    private void loadData() {
        transactionData.setAll(transactionService.getAll());
    }
 
    private void handleDelete(ActionEvent event) {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            AlertUtil.showError("Pilih dulu data yang mau dihapus.");
            return;
        }
 
        boolean confirmed = AlertUtil.showConfirm("Yakin mau hapus transaksi ini?");
 
        if (confirmed) {
            transactionService.delete(selected.getId());
            loadData();
        }
    }
    
    private void handleAdd(ActionEvent event) {
        navigateToForm(event, null);
    }
 
    private void handleEdit(ActionEvent event) {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            AlertUtil.showError("Pilih dulu data yang mau diedit.");
            return;
        }
 
        navigateToForm(event, selected);
    }
 
    private void navigateToForm(ActionEvent event, Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction-form.fxml"));
            Parent formRoot = loader.load();
 
            TransactionFormController formController = loader.getController();
 
            if (transaction != null) {
                formController.setTransaction(transaction);
            }
 
            Scene formScene = new Scene(formRoot, 400, 600);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(formScene);
            stage.setTitle("Form Transaksi - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private void handleBack(ActionEvent event) {
        try {
            DashboardController dashboardController = new DashboardController();
            Scene dashboardScene = new Scene(dashboardController.createDashboard(), 800, 500);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
