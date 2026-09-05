/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.Customer;
import com.mycompany.customermanagement.service.CustomerService;
import com.mycompany.customermanagement.util.AlertUtil;
import com.mycompany.customermanagement.util.ExportUtil;
 
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


// "implements Initializable" -- ini bikin method initialize() di bawah otomatis
// dipanggil sendiri oleh JavaFX begitu customer-list.fxml selesai dimuat.
// Jadi kita tidak perlu manual panggil "load data" dari luar.
public class CustomerController implements Initializable {
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private ComboBox<String> statusFilter;
 
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colCategory;
    @FXML private TableColumn<Customer, String> colStatus;
    @FXML private TableColumn<Customer, String> colNotes;
 
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnExport;
    @FXML private Button btnBack;
 
    private final CustomerService customerService = new CustomerService();
 
    // Data yang sedang ditampilkan di tabel. ObservableList itu List "spesial"
    // JavaFX -- kalau isinya berubah, tabel otomatis ikut ter-update tanpa
    // kita perlu refresh manual.
    private final ObservableList<Customer> customerData = FXCollections.observableArrayList();
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Kolom No. -- tampilkan nomor urut baris (1,2,3,...), BUKAN id asli dari database.
        // id asli tetap dipakai di balik layar untuk Edit/Hapus, cuma tidak ditampilkan di kolom ini.
        colId.setCellFactory(col -> new javafx.scene.control.TableCell<Customer, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        
        // Menghubungkan tiap kolom tabel ke property/getter yang sesuai di Customer.java
        // "name" di sini artinya JavaFX akan otomatis panggil getName(), dst.
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
 
        customerTable.setItems(customerData);
        // fix kolom tiak konsisten
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
 
        // isi pilihan dropdown filter status
        statusFilter.getItems().addAll("Semua Status", "active", "inactive");
        statusFilter.setValue("Semua Status");
        statusFilter.valueProperty().addListener((obs, oldValue, newValue) -> handleFilterStatus(newValue));
        
        // Tombol-tombol dihubungkan ke method masing-masing
        searchButton.setOnAction(this::handleSearch);
        btnDelete.setOnAction(this::handleDelete);
        btnExport.setOnAction(this::handleExport);
        btnBack.setOnAction(this::handleBack);
 
        // pindah ke customer-form.fxml
        btnAdd.setOnAction(this::handleAdd);
        btnEdit.setOnAction(this::handleEdit);
 
        loadData();
    }
 
    // Ambil semua data dari database, isi ke tabel
    private void loadData() {
        customerData.setAll(customerService.getAll());
    }
 
    // Cari berdasarkan kata kunci di searchField
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText();
        
        // reset filter status kembali ke "Semua Status" waktu user search manual,
        // supaya tidak bingung ("kenapa hasil search cuma dikit?" -- ternyata gara-gara
        // filter status masih aktif dari sebelumnya)
        statusFilter.setValue("Semua Status");

 
        if (keyword == null || keyword.isBlank()) {
            loadData(); // kalau kolom search kosong, tampilkan semua data lagi
        } else {
            customerData.setAll(customerService.search(keyword));
        }
    }
    
    // dipanggil otomatis tiap kali pilihan statusFilter berubah
    private void handleFilterStatus(String selectedStatus) {
 
        if (selectedStatus == null || selectedStatus.equals("Semua Status")) {
            loadData();
        } else {
            customerData.setAll(customerService.filterByStatus(selectedStatus));
        }
    }
    
    // export data yang sedang tampil di tabel (menghormati search/filter yang aktif) ke CSV
    private void handleExport(ActionEvent event) {
 
        if (customerData.isEmpty()) {
            AlertUtil.showError("Tidak ada data untuk di-export.");
            return;
        }
 
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Data Pelanggan");
        fileChooser.setInitialFileName("data_pelanggan.csv");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV File", "*.csv")
        );
 
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
 
        if (file != null) {
            boolean success = ExportUtil.exportCustomersToCsv(customerData, file);
 
            if (success) {
                AlertUtil.showInfo("Data berhasil di-export ke:\n" + file.getAbsolutePath());
            } else {
                AlertUtil.showError("Gagal export data.");
            }
        }
    }
 
    // Hapus data yang sedang dipilih di tabel
    private void handleDelete(ActionEvent event) {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            AlertUtil.showError("Pilih dulu data yang mau dihapus.");
            return;
        }
 
        boolean confirmed = AlertUtil.showConfirm(
                "Yakin mau hapus pelanggan '" + selected.getName() + "'?");
 
        if (confirmed) {
            customerService.delete(selected.getId());
            loadData(); // refresh tabel setelah data terhapus
        }
    }
    
    // pindah ke customer-form.fxml dalam mode "Tambah" (form kosong)
    private void handleAdd(ActionEvent event) {
        navigateToForm(event, null);
    }
 
    // pindah ke customer-form.fxml dalam mode "Edit" (form terisi data terpilih)
    private void handleEdit(ActionEvent event) {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            AlertUtil.showError("Pilih dulu data yang mau diedit.");
            return;
        }
 
        navigateToForm(event, selected);
    }
 
    // Method bersama untuk handleAdd() dan handleEdit() -- bedanya cuma
    // parameter "customer": null untuk Tambah, terisi untuk Edit
    private void navigateToForm(ActionEvent event, Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer-form.fxml"));
            Parent formRoot = loader.load();
 
            // Ambil instance CustomerFormController yang otomatis dibuat
            // oleh FXMLLoader, supaya kita bisa panggil method setCustomer()-nya
            CustomerFormController formController = loader.getController();
 
            if (customer != null) {
                formController.setCustomer(customer);
            }
 
            Scene formScene = new Scene(formRoot, 400, 600);
 
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(formScene);
            stage.setTitle("Form Data Pelanggan - Customer Management System");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // Kembali ke Dashboard
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
