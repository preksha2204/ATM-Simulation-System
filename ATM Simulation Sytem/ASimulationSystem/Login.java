package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;


public class Login extends Application {
    private TextField cardField;
    private PasswordField pinField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AUTOMATED TELLER MACHINE");

        // Logo
        Image logo = new Image("file:C:/Preksha/icons/logo.jpg", 100, 100, true, true);
        ImageView logoView = new ImageView(logo);

        // Labels
        Label title = new Label("WELCOME TO ATM");
        title.setStyle("-fx-font-size: 38px; -fx-font-weight: bold;");

        Label cardLabel = new Label("Card No:");
        cardLabel.setStyle("-fx-font-size: 28px;");

        Label pinLabel = new Label("PIN:");
        pinLabel.setStyle("-fx-font-size: 28px;");

        // Text Fields
        cardField = new TextField();
        cardField.setPromptText("Enter Card No");
        cardField.setStyle("-fx-font-size: 14px;");
        
        pinField = new PasswordField();
        pinField.setPromptText("Enter PIN");
        pinField.setStyle("-fx-font-size: 14px;");

        // Buttons
        Button signInButton = new Button("SIGN IN");
        Button clearButton = new Button("CLEAR");
        Button signUpButton = new Button("SIGN UP");

        signInButton.setOnAction(e -> handleSignIn());
        clearButton.setOnAction(e -> clearFields());
        signUpButton.setOnAction(e -> {
            Signup signup = new Signup();
            signup.start(primaryStage);  
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(logoView, 0, 0, 2, 1);
        grid.add(title, 2, 0, 2, 1);
        grid.add(cardLabel, 1, 1);
        grid.add(cardField, 2, 1);
        grid.add(pinLabel, 1, 2);
        grid.add(pinField, 2, 2);

        HBox buttonBox = new HBox(15, signInButton, clearButton);
        VBox layout = new VBox(10, grid, buttonBox, signUpButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSignIn() {
        String cardNo = cardField.getText();
        String pin = pinField.getText();

        try {
            Conn c1 = new Conn();
            String query = "SELECT * FROM login WHERE cardno = ? AND pin = ?";
            PreparedStatement stmt = c1.c.prepareStatement(query);
            stmt.setString(1, cardNo);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Successful!");
                alert.showAndWait();

                Transactions transactions = new Transactions(pin); 
                transactions.start(new Stage());
            
                ((Stage) cardField.getScene().getWindow()).close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect Card Number or PIN");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred. Please try again.");
            alert.showAndWait();
        }
    }


    private void clearFields() {
        cardField.clear();
        pinField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

