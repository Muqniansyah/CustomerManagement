/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class Main extends Application{
    
    @Override
    public void start(Stage primaryStage){
        Label label = new Label("Halo, JavaFX berhasil jalan!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Customer Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
     public static void main(String[] args) {
        launch(args);
    }
}
