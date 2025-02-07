package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class BalanceEnquiry extends Application {

    private String pin;
    private Label balanceLabel;

    public BalanceEnquiry(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Balance Enquiry");

        // Background Image
        Image backgroundImage = new Image("file:C:/Preksha/icons/atm.jpg");  // Fix the image path
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(960);
        imageView.setFitHeight(1080);

        // Create a StackPane to layer the image and other elements
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);

        // Label for Balance
        balanceLabel = new Label();
        balanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        balanceLabel.setMaxWidth(Double.MAX_VALUE); // Allow label to stretch across the VBox
        balanceLabel.setAlignment(Pos.TOP_LEFT); // Align the label text to the left

        // Button to go back
        Button backButton = new Button("BACK");
        backButton.setOnAction(e -> {
            new Transactions(pin).start(new Stage());
            primaryStage.close();
        });

        // Create a VBox for the balance label and button
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(balanceLabel, backButton);

        // Add VBox to StackPane
        stackPane.getChildren().add(vBox);

        // Update balance
        updateBalance();

        Scene scene = new Scene(stackPane, 960, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBalance() {
        double balance = 0;
        try {
            Conn c1 = new Conn();

            // Query to get the sum of deposits and withdrawals for the user
            String balanceQuery = "SELECT SUM(amount) AS total_balance FROM deposit WHERE cardno = ?";
            PreparedStatement balanceStmt = c1.c.prepareStatement(balanceQuery);
            balanceStmt.setString(1, pin);
            ResultSet rs = balanceStmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("total_balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update the balance label with the current balance
        balanceLabel.setText("Your Current Account Balance is Rs " + balance);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

