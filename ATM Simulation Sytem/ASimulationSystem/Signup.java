package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.*;

public class Signup extends Application {
    private static int formCounter = 1004;
    private int formNo;
    private TextField nameField, fatherNameField, emailField, addressField, cityField, pinField, stateField;
    private DatePicker dobPicker;
    private ToggleGroup genderGroup, maritalStatusGroup;

    @Override
    public void start(Stage stage) {
        // Logo
        ImageView logo = new ImageView(new Image("file:C:/Preksha/icons/logo.jpg"));
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);

        // Labels and Input Fields
        Label formLabel = new Label("APPLICATION FORM NO." + formCounter);
        formLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        nameField = new TextField();
        fatherNameField = new TextField();
        dobPicker = new DatePicker();
        emailField = new TextField();
        addressField = new TextField();
        cityField = new TextField();
        pinField = new TextField();
        stateField = new TextField();

        // Gender Selection
        genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);

        // Marital Status
        maritalStatusGroup = new ToggleGroup();
        RadioButton marriedRadio = new RadioButton("Married");
        marriedRadio.setToggleGroup(maritalStatusGroup);
        RadioButton unmarriedRadio = new RadioButton("Unmarried");
        unmarriedRadio.setToggleGroup(maritalStatusGroup);
        RadioButton otherRadio = new RadioButton("Other");
        otherRadio.setToggleGroup(maritalStatusGroup);

        // Next Button
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> handleNextAction(stage));

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Father's Name:"), 0, 1);
        grid.add(fatherNameField, 1, 1);
        grid.add(new Label("Date of Birth:"), 0, 2);
        grid.add(dobPicker, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(new HBox(10, maleRadio, femaleRadio), 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Marital Status:"), 0, 5);
        grid.add(new HBox(10, marriedRadio, unmarriedRadio, otherRadio), 1, 5);
        grid.add(new Label("Address:"), 0, 6);
        grid.add(addressField, 1, 6);
        grid.add(new Label("City:"), 0, 7);
        grid.add(cityField, 1, 7);
        grid.add(new Label("Pin Code:"), 0, 8);
        grid.add(pinField, 1, 8);
        grid.add(new Label("State:"), 0, 9);
        grid.add(stateField, 1, 9);

        VBox layout = new VBox(10, logo, formLabel, grid, nextButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        
        Scene scene = new Scene(layout, 500, 700);
        stage.setScene(scene);
        stage.setTitle("Signup - Step 1");
        stage.show();
    }

    private void handleNextAction(Stage stage) {
        String cname = nameField.getText();
        String fatherName = fatherNameField.getText();
        LocalDate dob = dobPicker.getValue();
        String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
        String email = emailField.getText();
        String maritalStatus = ((RadioButton) maritalStatusGroup.getSelectedToggle()).getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String pin = pinField.getText();
        String state = stateField.getText();

        formNo = formCounter++;

        try {
            Conn conn = new Conn();

            String query = "INSERT INTO signup (formno, cname, father_name, dob, gender, email, marital_status, address, city, pin, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, formNo);
            pstmt.setString(2, cname);
            pstmt.setString(3, fatherName);
            pstmt.setDate(4, java.sql.Date.valueOf(dob));
            pstmt.setString(5, gender);
            pstmt.setString(6, email);
            pstmt.setString(7, maritalStatus);
            pstmt.setString(8, address);
            pstmt.setString(9, city);
            pstmt.setString(10, pin);
            pstmt.setString(11, state);
            pstmt.executeUpdate();
            pstmt.close();
            conn.c.close();
            System.out.println("Data saved, moving to Signup2.");
            Signup2 signup2 = new Signup2();
            signup2.setFormno(String.valueOf(formNo));
            signup2.start(new Stage());
            stage.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
