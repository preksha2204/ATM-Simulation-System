package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class MiniStatement extends Application {

    private String pin;

    public MiniStatement(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mini Statement");

        // Labels
        Label titleLabel = new Label("Indian Bank");
        Label cardLabel = new Label();
        Label transactionsLabel = new Label();
        Label balanceLabel = new Label();

        // Set up layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, cardLabel, transactionsLabel, balanceLabel);

        // Fetch card number and transactions
        fetchCardDetails(cardLabel, transactionsLabel, balanceLabel);

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());
        layout.getChildren().add(exitButton);

        // Create Scene
        Scene scene = new Scene(layout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchCardDetails(Label cardLabel, Label transactionsLabel, Label balanceLabel) {
        try {
            Conn c = new Conn();
            // Query login table using cardno, as pin is stored in login table
            ResultSet rs = c.s.executeQuery("SELECT * FROM login WHERE cardno = '" + pin + "'");
            if (rs.next()) {
                cardLabel.setText("Card Number: " + rs.getString("cardno").substring(0, 4) + "XXXXXXXX" + rs.getString("cardno").substring(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int balance = 0;
            Conn c1 = new Conn();
            // Query deposit table using cardno (for the pin in the login table)
            ResultSet rs = c1.s.executeQuery("SELECT * FROM deposit WHERE cardno = '" + pin + "'");
            StringBuilder transactions = new StringBuilder();
    
            while (rs.next()) {
                // Fetch transaction details
                transactions.append(rs.getString("deposit_date")).append("     ")
                        .append(rs.getString("transaction_type")).append("     ")
                        .append(rs.getInt("amount")).append("\n");

                // Update balance based on transaction type (Deposit or Withdrawal)
                if ("Deposit".equals(rs.getString("transaction_type"))) {
                    balance += rs.getInt("amount");
                } else if ("Withdrawal".equals(rs.getString("transaction_type"))) {
                    balance -= rs.getInt("amount");
                }
            }

            transactionsLabel.setText(transactions.toString());
            balanceLabel.setText("Your total Balance is Rs " + balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
