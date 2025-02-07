package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Date;

public class FastCash extends Application {

    private String pin;

    public FastCash(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        // Background Image
        ImageView background = new ImageView(new Image("file:C:/Preksha/icons/atm.jpg"));
        background.setFitWidth(960);
        background.setFitHeight(1080);

        // Label
        Label instructionLabel = new Label("SELECT WITHDRAWAL AMOUNT");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Buttons
        Button[] amountButtons = {
                new Button("Rs 100"),
                new Button("Rs 500"),
                new Button("Rs 1000"),
                new Button("Rs 2000"),
                new Button("Rs 5000"),
                new Button("Rs 10000"),
                new Button("BACK")
        };

        for (Button button : amountButtons) {
            button.setOnAction(e -> handleAction(e.getSource()));
        }

        // Layout
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: black;");
        layout.add(instructionLabel, 0, 0, 2, 1);
        
        for (int i = 0; i < amountButtons.length; i++) {
            layout.add(amountButtons[i], i % 2, (i / 2) + 1);
        }

        // Combine everything
        Scene scene = new Scene(layout, 960, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fast Cash");
        primaryStage.getIcons().add(new Image("file:ASimulatorSystem/icons/atm_icon.png"));
        primaryStage.show();
    }

    private void handleAction(Object source) {
        try {
            String buttonText = ((Button) source).getText();
            if ("BACK".equals(buttonText)) {
            // Handle the BACK button logic
                new Transactions(pin).start(new Stage());
                ((Stage) ((Button) source).getScene().getWindow()).close();
                return;
            }

            // Extract the numeric part from the button text (e.g., "Rs 100" -> "100")
            String amountText = buttonText.substring(3); // Assuming the format is "Rs <amount>"
            int amount = Integer.parseInt(amountText);  // Convert to integer

            Conn c = new Conn();
            // Update the balance query to reflect the table structure
            String balanceQuery = "SELECT SUM(CASE WHEN transaction_type = 'Deposit' THEN amount ELSE -amount END) AS balance FROM deposit WHERE cardno = ?";
            PreparedStatement balanceStmt = c.c.prepareStatement(balanceQuery);
            balanceStmt.setString(1, pin);  // Using 'pin' as card number (assuming it corresponds to 'cardno')
            ResultSet rs = balanceStmt.executeQuery();

            int balance = 0;
            if (rs.next()) {
                balance = rs.getInt("balance");
            }

            // Check if the withdrawal amount is greater than balance
            if (amount > balance) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "You have insufficient balance.");
                return;
            }

            // Perform the withdrawal transaction
            Date date = new Date();
            String query = "INSERT INTO deposit (cardno, amount, deposit_date, transaction_type) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = c.c.prepareStatement(query);
            stmt.setString(1, pin);  // Using 'pin' as card number
            stmt.setInt(2, -amount); // Negative value for withdrawal
            stmt.setTimestamp(3, new Timestamp(date.getTime()));
            stmt.setString(4, "Withdrawal");
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Rs. " + amount + " Withdrawn Successfully");
            new Transactions(pin).start(new Stage());
            ((Stage) ((Button) source).getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount", "The selected amount is not valid.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
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