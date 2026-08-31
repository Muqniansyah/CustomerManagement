package com.mycompany.customermanagement.controller;

import com.mycompany.customermanagement.model.User;
import com.mycompany.customermanagement.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    
     // Nama variabel harus SAMA PERSIS dengan fx:id di login.fxml
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.login(username, password);

        if (user != null) {
            // Login berhasil -> pindah ke Dashboard
            goToDashboard(event);
        } else {
            // Login gagal -> tampilkan pesan di errorLabel
            errorLabel.setText("Username atau password salah");
        }
    }

    private void goToDashboard(ActionEvent event) {
        DashboardController dashboardController = new DashboardController();
        Scene dashboardScene = new Scene(dashboardController.createDashboard(), 800, 500);

        // Ambil jendela (Stage) yang sedang aktif dari tombol yang diklik
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(dashboardScene);
        stage.setTitle("Dashboard - Customer Management System");
    }
}
