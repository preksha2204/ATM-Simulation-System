package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StartingPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Our Banking Application");

        // Load background image
        Image backgroundImage = new Image("file:C:/Preksha/icons/ATM-Program-in-Java.jpg"); 
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1155); 
        backgroundImageView.setFitHeight(650); 

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            Login loginPage = new Login();
            loginPage.start(new Stage());
            primaryStage.close(); 
        });

        StackPane layout = new StackPane();
        layout.getChildren().addAll(backgroundImageView, loginButton);
        StackPane.setAlignment(loginButton, Pos.TOP_CENTER);
        loginButton.setStyle("-fx-font-size: 24px; -fx-padding: 10;"); 

        Scene scene = new Scene(layout, 1155, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
