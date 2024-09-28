package com.example.module9;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

    public class DatabaseGUI extends Application {
        private TextField idField, nameField, ageField;
        private Label statusLabel;
        private Connection connection;

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Database GUI");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(5);
            grid.setHgap(5);

            Label idLabel = new Label("ID:");
            grid.add(idLabel, 0, 0);

            idField = new TextField();
            grid.add(idField, 1, 0);

            Label nameLabel = new Label("Name:");
            grid.add(nameLabel, 0, 1);

            nameField = new TextField();
            grid.add(nameField, 1, 1);

            Label ageLabel = new Label("Age:");
            grid.add(ageLabel, 0, 2);

            ageField = new TextField();
            grid.add(ageField, 1, 2);

            Button displayButton = new Button("Display");
            grid.add(displayButton, 0, 3);

            Button updateButton = new Button("Update");
            grid.add(updateButton, 1, 3);

            statusLabel = new Label("Status: Ready");
            grid.add(statusLabel, 0, 4, 2, 1);

            displayButton.setOnAction(e -> displayRecord());
            updateButton.setOnAction(e -> updateRecord());

            Scene scene = new Scene(grid, 300, 200);
            primaryStage.setScene(scene);

            connectToDatabase();

            primaryStage.show();
        }

        private void connectToDatabase() {
//            String url = "jdbc:mysql://localhost:3306/databasedb";
//            String user = "student1";
//            String password = "pass";

            String url = "jdbc:mysql://localhost:3306/databasedb";
            String user = "root";
            String password = "Hankbob2017!";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                statusLabel.setText("Status: Connected to database");
            } catch (ClassNotFoundException | SQLException e) {
                statusLabel.setText("Status: Error connecting to database");
                e.printStackTrace();
            }
        }

        private void displayRecord() {
            String id = idField.getText();
            try {
                String query = "SELECT * FROM students WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    ageField.setText(String.valueOf(rs.getInt("age")));
                    statusLabel.setText("Status: Record displayed");
                } else {
                    statusLabel.setText("Status: Record not found");
                }

                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                statusLabel.setText("Status: Error displaying record");
                e.printStackTrace();
            }
        }
        private void updateRecord() {
            String id = idField.getText();
            String name = nameField.getText();
            String age = ageField.getText();

            if (id.isEmpty() || name.isEmpty() || age.isEmpty()) {
                statusLabel.setText("Status: Please fill in all fields");
                return;
            }

            try {
                Integer.parseInt(age); // Check if age is a valid number
            } catch (NumberFormatException e) {
                statusLabel.setText("Status: Age must be a number");
                return;
            }

            try {
                String query = "UPDATE students SET name = ?, age = ? WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setInt(2, Integer.parseInt(age));
                pstmt.setString(3, id);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    statusLabel.setText("Status: Record updated successfully");
                } else {
                    statusLabel.setText("Status: Record not found");
                }

                pstmt.close();
            } catch (SQLException e) {
                statusLabel.setText("Status: Error updating record");
                e.printStackTrace();
            }
        }




        @Override
        public void stop() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

