package ASimulationSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;

public class Signup2 extends Application {
    
    private String formno;

    // No-argument constructor for JavaFX
    public Signup2() {
    }

    // Constructor to set form number
    public Signup2(String formno) {
        this.formno = formno;
    }
    
    // Setter method to set form number after instantiation
    public void setFormno(String formno) {
        this.formno = formno;
    }
    
    @Override
    public void start(Stage stage) {
        ImageView logo = new ImageView(new Image("file:C:/Preksha/icons/logo.jpg"));
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);
        stage.setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 2");

        // Labels
        Label titleLabel = new Label("Page 2: Additional Details");
        titleLabel.setFont(new Font("Raleway", 22));
        
        Label formLabel = new Label("Form No:");
        Label formNumberLabel = new Label(formno);

        // ComboBox for dropdowns
        ComboBox<String> religionCombo = new ComboBox<>();
        religionCombo.getItems().addAll("Hindu", "Muslim", "Sikh", "Christian", "Other");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("General", "OBC", "SC", "ST", "Other");

        ComboBox<String> incomeCombo = new ComboBox<>();
        incomeCombo.getItems().addAll("Null", "<1,50,000", "<2,50,000", "<5,00,000", "Upto 10,00,000", "Above 10,00,000");

        ComboBox<String> educationCombo = new ComboBox<>();
        educationCombo.getItems().addAll("Non-Graduate", "Graduate", "Post-Graduate", "Doctorate", "Others");

        ComboBox<String> occupationCombo = new ComboBox<>();
        occupationCombo.getItems().addAll("Salaried", "Self-Employed", "Business", "Student", "Retired", "Others");

        // TextFields
        TextField panField = new TextField();
        TextField aadharField = new TextField();

        // RadioButtons
        RadioButton seniorYes = new RadioButton("Yes");
        RadioButton seniorNo = new RadioButton("No");
        ToggleGroup seniorGroup = new ToggleGroup();
        seniorYes.setToggleGroup(seniorGroup);
        seniorNo.setToggleGroup(seniorGroup);

        RadioButton accountYes = new RadioButton("Yes");
        RadioButton accountNo = new RadioButton("No");
        ToggleGroup accountGroup = new ToggleGroup();
        accountYes.setToggleGroup(accountGroup);
        accountNo.setToggleGroup(accountGroup);

        // Button
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            // Handle action: save data to database and transition to the next page
            String religion = religionCombo.getValue();
            String category = categoryCombo.getValue();
            String income = incomeCombo.getValue();
            String education = educationCombo.getValue();
            String occupation = occupationCombo.getValue();
            String pan = panField.getText();
            String aadhar = aadharField.getText();
            String scitizen = seniorYes.isSelected() ? "Yes" : "No";
            String eaccount = accountYes.isSelected() ? "Yes" : "No";

            try {
                Conn conn = new Conn();
                String query = "INSERT INTO signup2 (formno, religion, category, income, education, occupation, pan, aadhar, senior_citizen, existing_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.c.prepareStatement(query);
                pstmt.setString(1, formno);
                pstmt.setString(2, religion);
                pstmt.setString(3, category);
                pstmt.setString(4, income);
                pstmt.setString(5, education);
                pstmt.setString(6, occupation);
                pstmt.setString(7, pan);
                pstmt.setString(8, aadhar);
                pstmt.setString(9, scitizen);
                pstmt.setString(10, eaccount);
                pstmt.executeUpdate();
                pstmt.close();
                conn.c.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Open Signup3 and pass form number
            Signup3 signup3 = new Signup3();
            signup3.setFormno(formno);
            signup3.start(new Stage());
            stage.close(); // Close Signup2
        });

        // Layout
        VBox layout = new VBox(10, titleLabel, new Label("Religion:"), religionCombo, 
                               new Label("Category:"), categoryCombo, new Label("Income:"), incomeCombo, 
                               new Label("Educational Qualification:"), educationCombo, 
                               new Label("Occupation:"), occupationCombo, new Label("PAN Number:"), panField, 
                               new Label("Aadhar Number:"), aadharField, new Label("Senior Citizen:"), 
                               new HBox(10, seniorYes, seniorNo), new Label("Existing Account:"), 
                               new HBox(10, accountYes, accountNo), nextButton);
        layout.setPadding(new Insets(20));

        // Set scene and show
        Scene scene = new Scene(layout, 600, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
