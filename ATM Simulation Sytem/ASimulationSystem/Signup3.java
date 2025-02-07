package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Random;

public class Signup3 extends Application {

    private String formno;
    private Stage primaryStage; // Add this line at the class level

    public Signup3() {
    }

    public void setFormno(String formno) {
        this.formno = formno;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("New Account Application Form - Page 3");

        // Logo
        ImageView logo = new ImageView(new Image("file:C:/Preksha/icons/logo.jpg"));
        logo.setFitHeight(100);
        logo.setFitWidth(100);

        Label title = new Label("Page 3: Account Details");
        Label formLabel = new Label("Form No: " + formno);
        Label accountTypeLabel = new Label("Account Type:");
        Label cardNumberLabel = new Label("Card Number: XXXX-XXXX-XXXX-4184");
        Label pinLabel = new Label("PIN: XXXX");

        // Radio Buttons for Account Type
        RadioButton savingAccount = new RadioButton("Saving Account");
        RadioButton fixedDepositAccount = new RadioButton("Fixed Deposit Account");
        RadioButton currentAccount = new RadioButton("Current Account");
        RadioButton recurringDepositAccount = new RadioButton("Recurring Deposit Account");

        ToggleGroup accountTypeGroup = new ToggleGroup();
        savingAccount.setToggleGroup(accountTypeGroup);
        fixedDepositAccount.setToggleGroup(accountTypeGroup);
        currentAccount.setToggleGroup(accountTypeGroup);
        recurringDepositAccount.setToggleGroup(accountTypeGroup);

        // Checkboxes for Facilities
        CheckBox atmCard = new CheckBox("ATM CARD");
        CheckBox internetBanking = new CheckBox("Internet Banking");
        CheckBox mobileBanking = new CheckBox("Mobile Banking");
        CheckBox emailAlerts = new CheckBox("EMAIL Alerts");
        CheckBox chequeBook = new CheckBox("Cheque Book");
        CheckBox eStatement = new CheckBox("E-Statement");

        // Buttons
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(logo, 0, 0, 2, 1);
        grid.add(title, 1, 1);
        grid.add(formLabel, 2, 1);
        grid.add(accountTypeLabel, 0, 2);
        grid.add(savingAccount, 0, 3);
        grid.add(fixedDepositAccount, 1, 3);
        grid.add(currentAccount, 0, 4);
        grid.add(recurringDepositAccount, 1, 4);
        grid.add(cardNumberLabel, 0, 5);
        grid.add(pinLabel, 0, 6);

        VBox checkBoxVBox = new VBox(10, atmCard, internetBanking, mobileBanking, emailAlerts, chequeBook, eStatement);
        grid.add(checkBoxVBox, 0, 7);

        VBox buttonBox = new VBox(10, submitButton, cancelButton);
        grid.add(buttonBox, 1, 8);

        // Button actions
        submitButton.setOnAction(e -> handleSubmission(accountTypeGroup, atmCard, internetBanking, mobileBanking, emailAlerts, chequeBook, eStatement));
        cancelButton.setOnAction(e -> primaryStage.close());

        // Scene setup
        Scene scene = new Scene(grid, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmission(ToggleGroup accountTypeGroup, CheckBox... services) {
        String accountType = ((RadioButton) accountTypeGroup.getSelectedToggle()).getText();
        Random random = new Random();
        String cardNumber = "XXXX-XXXX-XXXX-" + (1000 + random.nextInt(9000));
        String pin = String.format("%04d", random.nextInt(10000));

        StringBuilder facility = new StringBuilder();
        for (CheckBox service : services) {
            if (service.isSelected()) {
                facility.append(service.getText()).append(", ");
            }
        }
        if (facility.length() > 0) {
            facility.setLength(facility.length() - 2); // Remove last comma and space
        }

        // Database save
        try {
            Conn conn = new Conn();
            String accountQuery = "INSERT INTO signup3 (form_no, account_type, card_number, pin, facilities) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement accountStmt = conn.c.prepareStatement(accountQuery);
            accountStmt.setInt(1, Integer.parseInt(formno));
            accountStmt.setString(2, accountType);
            accountStmt.setString(3, cardNumber.replaceAll("-", "")); // Remove dashes for storage
            accountStmt.setString(4, pin);
            accountStmt.setString(5, facility.toString());
            accountStmt.executeUpdate();

            String loginQuery = "INSERT INTO login (cardno, pin) VALUES (?, ?)";
            PreparedStatement loginStmt = conn.c.prepareStatement(loginQuery);
            loginStmt.setString(1, cardNumber.replaceAll("-", ""));
            loginStmt.setString(2, pin);
            loginStmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Details");
            alert.setHeaderText("Account Created Successfully");
            alert.setContentText("Card Number: " + cardNumber + "\nPIN: " + pin + "\nFacilities: " + facility);
            alert.showAndWait();

            new Transactions(pin).start(new Stage());
            primaryStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}



