package com.mycompany.customermanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {

    // =========================================================
    // Membuat tampilan utama Dashboard secara langsung dengan Java
    // tanpa menggunakan Scene Builder / FXML.
    // =========================================================
    public BorderPane createDashboard() {

        BorderPane root = new BorderPane();

        // Warna dasar dashboard
        root.setStyle("-fx-background-color: #f7f4f0;");

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(25, 35, 25, 35));
        header.setStyle("-fx-background-color: #ffffff;");

        VBox titleBox = new VBox(4);

        Label appTitle = new Label("Customer Management System");
        appTitle.setStyle(
                "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #3d3026;"
        );

        Label subtitle = new Label("Kelola pelanggan, transaksi, dan interaksi");
        subtitle.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #8a7a6b;"
        );

        titleBox.getChildren().addAll(appTitle, subtitle);

        // Spacer agar tombol Logout berada di sebelah kanan
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(
                "-fx-background-color: #eee9e3;"
                + "-fx-text-fill: #5f4d3d;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-padding: 9px 18px;"
                + "-fx-cursor: hand;"
        );

        btnLogout.setOnAction(this::handleLogout);

        header.getChildren().addAll(titleBox, spacer, btnLogout);

        // =====================================================
        // KONTEN DASHBOARD
        // =====================================================

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(35));
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcome = new Label("Selamat Datang");
        welcome.setStyle(
                "-fx-font-size: 28px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #3d3026;"
        );

        Label description = new Label(
                "Pilih menu yang ingin kamu kelola."
        );
        description.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #8a7a6b;"
        );

        // =====================================================
        // CARD MENU
        // =====================================================

        GridPane cards = new GridPane();

        cards.setHgap(20);
        cards.setVgap(20);
        cards.setAlignment(Pos.CENTER);

        VBox customerCard = createMenuCard(
                "Data Pelanggan",
                "Kelola informasi dan data pelanggan.",
                "Buka Data Pelanggan",
                this::handleShowCustomerPage
        );

        VBox transactionCard = createMenuCard(
                "Transaksi",
                "Kelola transaksi pelanggan.",
                "Buka Transaksi",
                this::handleShowTransactionPage
        );

        VBox interactionCard = createMenuCard(
                "Riwayat Interaksi",
                "Catat dan lihat riwayat interaksi pelanggan.",
                "Buka Interaksi",
                this::handleShowInteractionPage
        );

        cards.add(customerCard, 0, 0);
        cards.add(transactionCard, 1, 0);
        cards.add(interactionCard, 2, 0);

        mainContent.getChildren().addAll(
                welcome,
                description,
                cards
        );

        // =====================================================
        // SUSUN DASHBOARD
        // =====================================================

        root.setTop(header);
        root.setCenter(mainContent);

        return root;
    }

    // =========================================================
    // Membuat Card Menu Dashboard
    // =========================================================

    private VBox createMenuCard(
            String title,
            String description,
            String buttonText,
            javafx.event.EventHandler<ActionEvent> action
    ) {

        VBox card = new VBox(15);

        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(240);
        card.setPrefHeight(180);

        card.setPadding(new Insets(25));

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10px;"
                + "-fx-border-color: #e2d9d0;"
                + "-fx-border-radius: 10px;"
        );

        Label cardTitle = new Label(title);
        cardTitle.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #4a382a;"
        );

        Label cardDescription = new Label(description);
        cardDescription.setWrapText(true);
        cardDescription.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #8a7a6b;"
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button button = new Button(buttonText);

        button.setMaxWidth(Double.MAX_VALUE);

        button.setStyle(
                "-fx-background-color: #8b6f4e;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-padding: 10px;"
                + "-fx-cursor: hand;"
        );

        button.setOnAction(action);

        card.getChildren().addAll(
                cardTitle,
                cardDescription,
                spacer,
                button
        );

        return card;
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout(ActionEvent event) {

        try {

            Parent loginRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Scene loginScene = new Scene(loginRoot);

            Stage stage = (Stage) ((Button) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(loginScene);
            stage.setTitle("Login - Customer Management System");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // PINDAH KE HALAMAN DATA PELANGGAN
    // =========================================================

    private void handleShowCustomerPage(ActionEvent event) {

        try {

            Parent customerRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/customer-list.fxml")
            );

            Scene customerScene = new Scene(
                    customerRoot,
                    900,
                    600
            );

            Stage stage = (Stage) ((Button) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(customerScene);
            stage.setTitle(
                    "Data Pelanggan - Customer Management System"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // PINDAH KE HALAMAN TRANSAKSI
    // =========================================================

    private void handleShowTransactionPage(ActionEvent event) {

        try {

            Parent transactionRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/transaction-list.fxml")
            );

            Scene transactionScene = new Scene(
                    transactionRoot,
                    900,
                    600
            );

            Stage stage = (Stage) ((Button) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(transactionScene);
            stage.setTitle(
                    "Transaksi - Customer Management System"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // PINDAH KE HALAMAN RIWAYAT INTERAKSI
    // =========================================================

    private void handleShowInteractionPage(ActionEvent event) {

        try {

            Parent interactionRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/interaction-list.fxml")
            );

            Scene interactionScene = new Scene(
                    interactionRoot,
                    900,
                    600
            );

            Stage stage = (Stage) ((Button) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(interactionScene);
            stage.setTitle(
                    "Riwayat Interaksi - Customer Management System"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}