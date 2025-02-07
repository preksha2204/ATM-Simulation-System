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

public class Withdrawal extends Application {

    private String pin;
    private TextField amountField;

    public Withdrawal(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        // Background Image
        ImageView background = new ImageView(new Image("file:C:/Preksha/icons/atm.jpg"));
        background.setFitWidth(600);
        background.setFitHeight(400);

        // Labels
        Label maxWithdrawalLabel = new Label("MAXIMUM WITHDRAWAL IS RS. 10,000");
        maxWithdrawalLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Label enterAmountLabel = new Label("PLEASE ENTER YOUR AMOUNT");
        enterAmountLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        amountField = new TextField();
        amountField.setPromptText("Enter Amount");

        // Buttons
        Button withdrawButton = new Button("WITHDRAW");
        Button backButton = new Button("BACK");

        // Event handlers
        withdrawButton.setOnAction(e -> handleWithdrawal());
        backButton.setOnAction(e -> {
            new Transactions(pin).start(new Stage());
            primaryStage.close();
        });

        // Layout
        VBox layout = new VBox(15, maxWithdrawalLabel, enterAmountLabel, amountField, withdrawButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: black;");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Withdrawal");
        primaryStage.show();
    }

    private void handleWithdrawal() {
    String amount = amountField.getText();
    Date date = new Date();

    if (amount.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Error", "Please enter the Amount you want to withdraw");
    } else {
        try {
            Conn c1 = new Conn();

            // Calculate the current balance by summing deposits and withdrawals
            String balanceQuery = "SELECT SUM(amount) AS total_balance FROM deposit WHERE cardno = ?";
            PreparedStatement balanceStmt = c1.c.prepareStatement(balanceQuery);
            balanceStmt.setString(1, pin);
            ResultSet rs = balanceStmt.executeQuery();

            double currentBalance = 0;
            if (rs.next()) {
                currentBalance = rs.getDouble("total_balance");
            }

            // Check if the user has sufficient balance
            double withdrawAmount = Double.parseDouble(amount);
            if (currentBalance < withdrawAmount) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "You have insufficient balance");
                return;
            }

            // Insert the withdrawal transaction into the 'deposit' table (negative amount for withdrawal)
            String query = "INSERT INTO deposit (cardno, amount, deposit_date, transaction_type) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = c1.c.prepareStatement(query);
            stmt.setString(1, pin);
            stmt.setDouble(2, -withdrawAmount);  // Use negative amount for withdrawal (decreases balance)
            stmt.setTimestamp(3, new Timestamp(date.getTime()));
            stmt.setString(4, "Withdrawal");
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Rs. " + amount + " Withdrawn Successfully");

            // Go back to the transactions screen
            new Transactions(pin).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your withdrawal.");
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

