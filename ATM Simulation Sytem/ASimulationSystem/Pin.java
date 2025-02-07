package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class Pin extends Application {

    private String pin;
    private PasswordField newPinField, reEnterPinField;

    public Pin(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Change PIN");

        // Background Image
        Image backgroundImage = new Image("file:C:/Preksha/icons/atm.jpg");
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(960);
        imageView.setFitHeight(1080);


        // Labels
        Label titleLabel = new Label("CHANGE YOUR PIN");
        Label newPinLabel = new Label("New PIN:");
        Label reEnterPinLabel = new Label("Re-Enter New PIN:");

        // Password Fields
        newPinField = new PasswordField();
        reEnterPinField = new PasswordField();

        // Buttons
        Button changeButton = new Button("CHANGE");
        Button backButton = new Button("BACK");

        changeButton.setOnAction(e -> changePin());
        backButton.setOnAction(e -> goBack(primaryStage));

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(titleLabel, 0, 0, 2, 1);
        grid.add(newPinLabel, 0, 1);
        grid.add(newPinField, 1, 1);
        grid.add(reEnterPinLabel, 0, 2);
        grid.add(reEnterPinField, 1, 2);
        grid.add(changeButton, 0, 3);
        grid.add(backButton, 1, 3);
        grid.setStyle("-fx-background-image: url('" + backgroundImage + "');");

        Scene scene = new Scene(grid, 960, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changePin() {
        try {
            String newPin = newPinField.getText();
            String reEnteredPin = reEnterPinField.getText();
    
            // Validate that both PIN fields match
            if (!newPin.equals(reEnteredPin)) {
                showAlert("Entered PIN does not match");
                return;
            }

            // Validate that PIN is not empty
            if (newPin.isEmpty()) {
                showAlert("Enter New PIN");
                return;
            }
            if (reEnteredPin.isEmpty()) {
                 showAlert("Re-Enter new PIN");
                return;
            }

            // Validate PIN format (4-digit PIN)
            if (newPin.length() != 4 || !newPin.matches("\\d+")) {
                showAlert("PIN must be 4 digits");
                return;
            }

            // Database connection and updating the PIN using parameterized queries
            Conn c1 = new Conn();
        
                // Start a transaction to ensure atomicity
            c1.c.setAutoCommit(false);

            try {
                // SQL Queries for updating PIN in different tables
                String query1 = "UPDATE signup SET pin = ? WHERE pin = ?";  // Update PIN in signup3
                String query2 = "UPDATE login SET pin = ? WHERE pin = ?";   // Update PIN in login table
                String query3 = "UPDATE signup2 SET pin = ? WHERE pin = ?"; // Optional: Update PIN in signup2 table

                // PreparedStatements to avoid SQL injection
                PreparedStatement stmt1 = c1.c.prepareStatement(query1);
                PreparedStatement stmt2 = c1.c.prepareStatement(query2);
                PreparedStatement stmt3 = c1.c.prepareStatement(query3);

                // Set the new PIN and old PIN values
                stmt1.setString(1, reEnteredPin);  // New PIN
                stmt1.setString(2, pin);           // Old PIN
                stmt2.setString(1, reEnteredPin);  // New PIN
                stmt2.setString(2, pin);           // Old PIN
                stmt3.setString(1, reEnteredPin);  // New PIN
                stmt3.setString(2, pin);           // Old PIN

                // Execute the update queries
                int rowsUpdated1 = stmt1.executeUpdate();
                int rowsUpdated2 = stmt2.executeUpdate();
                int rowsUpdated3 = stmt3.executeUpdate();  // Execute for signup2 (optional)

                // Check if the rows were updated successfully
                if (rowsUpdated1 > 0 && rowsUpdated2 > 0 && rowsUpdated3 >= 0) {
                    // Commit transaction if all updates were successful
                    c1.c.commit();
    
                    // Success alert and transition to the next screen
                    showAlert("PIN changed successfully");
                    new Transactions(reEnteredPin).start(new Stage());
                } else {
                    // Rollback transaction if any update failed
                    c1.c.rollback();
                    showAlert("Error updating PIN. Please try again.");
                }
            } catch (SQLException e) {
                // Rollback if an error occurs during the transaction
                c1.c.rollback();
                e.printStackTrace();
                showAlert("Error updating PIN. Please try again.");
            } finally {
                // Reset auto-commit to true
                c1.c.setAutoCommit(true);
            }
        } 
        catch (SQLException e) {
             e.printStackTrace();
             showAlert("Database error. Please try again.");
        }
    }

    private void goBack(Stage primaryStage) {
        new Transactions(pin).start(new Stage());
        primaryStage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

