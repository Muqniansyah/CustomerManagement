/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Customer;
import com.mycompany.customermanagement.model.Interaction;
import com.mycompany.customermanagement.service.CustomerService;
import com.mycompany.customermanagement.service.InteractionService;
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
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class InteractionFormController implements Initializable{
    @FXML private ComboBox<Customer> customerField;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> typeField;
    @FXML private TextArea descriptionField;
    @FXML private TextArea notesField;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    @FXML private Label customerError;
    @FXML private Label dateError;
    @FXML private Label typeError;
    @FXML private Label descriptionError;
 
    private final CustomerService customerService = new CustomerService();
    private final InteractionService interactionService = new InteractionService();
 
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
    private Interaction editingInteraction = null;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        List<Customer> customers = customerService.getAll();
        customerField.setItems(FXCollections.observableArrayList(customers));
 
        customerField.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer == null ? "" : customer.getName();
            }
 
            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
 
        typeField.getItems().addAll("Telepon", "Chat", "Kunjungan", "Email");
 
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
    }
 
    public void setInteraction(Interaction interaction) {
        this.editingInteraction = interaction;
 
        for (Customer c : customerField.getItems()) {
            if (c.getId() == interaction.getCustomerId()) {
                customerField.setValue(c);
                break;
            }
        }
 
        dateField.setValue(LocalDate.parse(interaction.getInteractionDate(), DATE_FORMAT));
        typeField.setValue(interaction.getType());
        descriptionField.setText(interaction.getDescription());
        notesField.setText(interaction.getNotes());
    }
 
    private void handleSave(ActionEvent event) {
 
        clearError(customerField, customerError);
        clearError(dateField, dateError);
        clearError(typeField, typeError);
        clearError(descriptionField, descriptionError);
 
        boolean valid = true;
 
        if (customerField.getValue() == null) {
            showError(customerField, customerError, "Pilih pelanggan.");
            valid = false;
        }
 
        if (dateField.getValue() == null) {
            showError(dateField, dateError, "Tanggal wajib diisi.");
            valid = false;
        }
 
        if (typeField.getValue() == null) {
            showError(typeField, typeError, "Pilih tipe interaksi.");
            valid = false;
        }
 
        if (descriptionField.getText() == null || descriptionField.getText().isBlank()) {
            showError(descriptionField, descriptionError, "Deskripsi wajib diisi.");
            valid = false;
        }
 
        if (!valid) {
            return;
        }
 
        int customerId = customerField.getValue().getId();
        String dateStr = dateField.getValue().format(DATE_FORMAT);
 
        if (editingInteraction == null) {
            Interaction newInteraction = new Interaction(
                    0,
                    customerId,
                    dateStr,
                    typeField.getValue(),
                    descriptionField.getText()
            );
            newInteraction.setNotes(notesField.getText());
 
            interactionService.save(newInteraction);
            AlertUtil.showInfo("Interaksi berhasil ditambahkan.");
 
        } else {
            editingInteraction.setCustomerId(customerId);
            editingInteraction.setInteractionDate(dateStr);
            editingInteraction.setType(typeField.getValue());
            editingInteraction.setDescription(descriptionField.getText());
            editingInteraction.setNotes(notesField.getText());
 
            interactionService.update(editingInteraction);
            AlertUtil.showInfo("Interaksi berhasil diperbarui.");
        }
 
        backToInteractionList(event);
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
        backToInteractionList(event);
    }
 
    private void backToInteractionList(ActionEvent event) {
        try {
            Parent listRoot = FXMLLoader.load(getClass().getResource("/fxml/interaction-list.fxml"));
            Scene listScene = new Scene(listRoot, 800, 500);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(listScene);
            stage.setTitle("Riwayat Interaksi - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
