package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Date;

public class Deposit extends Application {

    private String pin;
    private TextField amountField;

    public Deposit(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        // Background Image
        ImageView background = new ImageView(new Image("file:C:/Preksha/icons/atm.jpg"));
        background.setFitWidth(500);
        background.setFitHeight(300);

        // Label and Text Field
        Label label = new Label("ENTER AMOUNT YOU WANT TO DEPOSIT");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        amountField = new TextField();
        amountField.setPromptText("Enter Amount");

        // Deposit and Back Buttons
        Button depositButton = new Button("DEPOSIT");
        Button backButton = new Button("BACK");

        // Event handlers
        depositButton.setOnAction(e -> handleDeposit());
        backButton.setOnAction(e -> {
            new Transactions(pin).start(new Stage());
            primaryStage.close();
        });

        // Layout
        VBox layout = new VBox(15, label, amountField, depositButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: black;");

        // Scene setup
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Deposit");
        primaryStage.show();
    }

    private void handleDeposit() {
        String amount = amountField.getText();
        Date date = new Date();

        if (amount.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter the Amount you want to deposit");
        } else {
            try {
                Conn c1 = new Conn();

                // Insert the deposit transaction into the 'deposit' table (positive amount for deposit)
                String query = "INSERT INTO deposit (cardno, amount, deposit_date, transaction_type) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = c1.c.prepareStatement(query);
                stmt.setString(1, pin);
                stmt.setDouble(2, Double.parseDouble(amount));  // Positive amount for deposit
                stmt.setTimestamp(3, new Timestamp(date.getTime()));
                stmt.setString(4, "Deposit");
                stmt.executeUpdate();

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rs. " + amount + " Deposited Successfully");

                // Go back to the transactions screen
                new Transactions(pin).start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your deposit.");
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}