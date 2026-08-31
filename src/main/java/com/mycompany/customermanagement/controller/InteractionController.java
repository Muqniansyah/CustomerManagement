/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Interaction;
import com.mycompany.customermanagement.service.InteractionService;
import com.mycompany.customermanagement.util.AlertUtil;
 
import java.net.URL;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InteractionController implements Initializable {
    @FXML private TableView<Interaction> interactionTable;
    @FXML private TableColumn<Interaction, String> colDate;
    @FXML private TableColumn<Interaction, String> colType;
    @FXML private TableColumn<Interaction, String> colDescription;
    @FXML private TableColumn<Interaction, String> colNotes;
 
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;
 
    private final InteractionService interactionService = new InteractionService();
    private final ObservableList<Interaction> interactionData = FXCollections.observableArrayList();
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Nama di dalam PropertyValueFactory harus sama dengan nama getter di Interaction.java
        // (tanpa kata "get" dan huruf pertama kecil) -- misal "interactionDate" -> getInteractionDate()
        colDate.setCellValueFactory(new PropertyValueFactory<>("interactionDate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
 
        interactionTable.setItems(interactionData);
 
        btnDelete.setOnAction(this::handleDelete);
        btnBack.setOnAction(this::handleBack);
 
        btnAdd.setOnAction(e -> AlertUtil.showInfo("Form tambah interaksi belum dibuat."));
        btnEdit.setOnAction(e -> AlertUtil.showInfo("Form edit interaksi belum dibuat."));
 
        loadData();
    }
 
    private void loadData() {
        interactionData.setAll(interactionService.getAll());
    }
 
    private void handleDelete(ActionEvent event) {
        Interaction selected = interactionTable.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            AlertUtil.showError("Pilih dulu data yang mau dihapus.");
            return;
        }
 
        boolean confirmed = AlertUtil.showConfirm("Yakin mau hapus interaksi ini?");
 
        if (confirmed) {
            interactionService.delete(selected.getId());
            loadData();
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
