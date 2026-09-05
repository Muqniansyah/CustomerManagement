/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Transaction;
import com.mycompany.customermanagement.service.TransactionService;
import com.mycompany.customermanagement.util.AlertUtil;
 
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
 
        setupPaymentProofColumn();
        
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
    
    // 1. Method untuk memasang tombol "Lihat" di dalam kolom Bukti Pembayaran
    private void setupPaymentProofColumn() {
        colPaymentProof.setCellFactory(column -> new TableCell<Transaction, String>() {
            private final Button btnView = new Button("Lihat");

            {
                btnView.getStyleClass().add("secondary-button");
                btnView.setStyle("-fx-font-size: 11px; -fx-padding: 2 8;");
            }

            @Override
            protected void updateItem(String fileName, boolean empty) {
                super.updateItem(fileName, empty);

                if (empty || fileName == null || fileName.trim().isEmpty()) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btnView.setOnAction(e -> showImagePreview(fileName));

                    HBox container = new HBox(8, new Label(fileName), btnView);
                    container.setStyle("-fx-alignment: CENTER-LEFT;");
                    setGraphic(container);
                    setText(null);
                }
            }
        });
    }

    // 2. Method untuk menampilkan Pop-up Modal Gambar
    private void showImagePreview(String fileName) {
        File file = new File("data/bukti_pembayaran/" + fileName);

        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Tidak Ditemukan");
            alert.setHeaderText(null);
            alert.setContentText("Gambar tidak ditemukan di lokasi: " + file.getAbsolutePath());
            alert.showAndWait();
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Bukti Pembayaran - " + fileName);
        dialog.setResizable(false); // Mencegah window ditarik-tarik

        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(550);  // Disesuaikan ukurannya
        imageView.setFitHeight(380);

        Label titleLabel = new Label("Bukti Bayar: " + fileName);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;");

        Button btnClose = new Button("Tutup");
        // Styling langsung untuk tombol Tutup agar rapi dan kontras
        btnClose.setStyle(
            "-fx-background-color: #8B5A2B; " +  // Warna cokelat disesuaikan dengan tema tabel kamu
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8 25; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        btnClose.setOnAction(e -> dialog.close());

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");
        layout.getChildren().addAll(titleLabel, imageView, btnClose);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
 
    private void navigateToForm(ActionEvent event, Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction-form.fxml"));
            Parent formRoot = loader.load();
 
            TransactionFormController formController = loader.getController();
 
            if (transaction != null) {
                formController.setTransaction(transaction);
            }
 
            Scene formScene = new Scene(formRoot, 900, 600);
 
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
