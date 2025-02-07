package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Transactions extends Application {

    private String pin;

    public Transactions(String pin) {
        this.pin = pin;
    }

    @Override
    public void start(Stage primaryStage) {
        // Load the ATM background image
        ImageView background = new ImageView(new Image("file:C:/Preksha/icons/atm.jpg"));
        background.setFitWidth(960);
        background.setFitHeight(997);

        // Transaction selection label
        Label label = new Label("Please Select Your Transaction");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        // Buttons for transaction options
        Button depositButton = new Button("DEPOSIT");
        Button withdrawButton = new Button("CASH WITHDRAWAL");
        Button fastCashButton = new Button("FAST CASH");
        Button miniStatementButton = new Button("MINI STATEMENT");
        Button pinChangeButton = new Button("PIN CHANGE");
        Button balanceEnquiryButton = new Button("BALANCE ENQUIRY");
        Button exitButton = new Button("EXIT");

        // Set button styles and actions
        Button[] buttons = {depositButton, withdrawButton, fastCashButton, miniStatementButton, pinChangeButton, balanceEnquiryButton, exitButton};
        for (Button button : buttons) {
            button.setMinWidth(190);
            button.setOnAction(e -> handleButtonAction(e.getSource()));
        }

        // Layout for buttons in a GridPane
        GridPane buttonLayout = new GridPane();
        buttonLayout.setVgap(10);
        buttonLayout.setAlignment(Pos.CENTER_LEFT);  // Align buttons to the left within the GridPane
        buttonLayout.add(depositButton, 0, 0);
        buttonLayout.add(withdrawButton, 1, 0);
        buttonLayout.add(fastCashButton, 0, 1);
        buttonLayout.add(miniStatementButton, 1, 1);
        buttonLayout.add(pinChangeButton, 0, 2);
        buttonLayout.add(balanceEnquiryButton, 1, 2);
        buttonLayout.add(exitButton, 1, 3);

        // VBox for the label and button layout with adjusted padding
        VBox layout = new VBox(20, label, buttonLayout);
        layout.setAlignment(Pos.CENTER_LEFT);  
        layout.setPadding(new Insets(100, 0, 0, 170));  // Adjust these values as needed for fine-tuning

        // HBox to hold the VBox with adjusted alignment to slightly center-right
        HBox hbox = new HBox(layout);
        hbox.setAlignment(Pos.TOP_LEFT);

        // StackPane to layer the background image and button layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, hbox); // Add the HBox on top of the background

        // Scene setup
        Scene scene = new Scene(stackPane, 960, 997);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Transactions");
        primaryStage.show();
    }

    private void handleButtonAction(Object source) {
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            String buttonText = clickedButton.getText();

            switch (buttonText) {
                case "DEPOSIT":
                    new Deposit(pin).start(new Stage());
                    break;
                case "CASH WITHDRAWAL":
                    new Withdrawal(pin).start(new Stage());
                    break;
                case "FAST CASH":
                    new FastCash(pin).start(new Stage());
                    break;
                case "MINI STATEMENT":
                    new MiniStatement(pin).start(new Stage());
                    break;
                case "PIN CHANGE":
                    new Pin(pin).start(new Stage());
                    break;
                case "BALANCE ENQUIRY":
                    new BalanceEnquiry(pin).start(new Stage());
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}