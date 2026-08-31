package com.mycompany.customermanagement;

import com.mycompany.customermanagement.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application{
    
    @Override
    public void start(Stage primaryStage)throws Exception {
        // import manual tanpa pemanggilan import
        // com.mycompany.customermanagement.database.DatabaseConnection.initializeDatabase();

        // pemanggilan database
        DatabaseConnection.initializeDatabase();
        
        // pemanggilan login       
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/login.fxml")
        );
        
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Customer Management System");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.show();
    }
    
     public static void main(String[] args) {
        launch(args);
    }
}
