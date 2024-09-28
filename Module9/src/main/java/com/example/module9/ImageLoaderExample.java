package com.example.module9;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class ImageLoaderExample extends Application{
    private Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/databasedb";
        String user = "root";
        String password = "Hankbob2017!";
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try (InputStream inputStream = new FileInputStream(file)) {
                insertImageIntoDatabase(file.getName(), inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void insertImageIntoDatabase(String fileName, InputStream inputStream) throws Exception {
        String sql = "INSERT INTO images (name, image) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fileName);
            pstmt.setBlob(2, inputStream);
            pstmt.executeUpdate();
            System.out.println("Image inserted into the database");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
