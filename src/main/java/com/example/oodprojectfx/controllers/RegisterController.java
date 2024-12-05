package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    public TextField UsernameField;
    @FXML
    public TextField EmailField;
    @FXML
    public javafx.scene.control.PasswordField PasswordField;
    @FXML
    public javafx.scene.control.PasswordField reEnterPasswordField;
    @FXML
    public Button registerButton;
    @FXML
    public CheckBox adminOptionCheckbox;

    private String username;
    private String email;
    private String password;
    private String reEnterPassword;

    @FXML
    public void onSignUpButtonClick(ActionEvent event) {
        username = UsernameField.getText().trim();
        email = EmailField.getText().trim();
        password = PasswordField.getText().trim();
        reEnterPassword = reEnterPasswordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
            showAlert("Empty fields", "Some of the fields are empty. Kindly fill in all the fields");
            return;
        }

        if(!isValidEmail(email))
        {
            showAlert("Invalid email", "Please enter a valid email");
            return;
        }

        // check if the passwords match
        if (!password.equals(reEnterPassword)) {
            showAlert("Password mismatch", "The passwords you entered do not match.");
            return;
        }

        // determine type based on checkbox
        String accountType = adminOptionCheckbox.isSelected() ? "Admin" : "User";



        // Create query to add all the features to the database
        // Corrected query
        String query = "INSERT INTO user (username, email, password, accountType) VALUES ('" + username + "', '" + email + "', '" + password + "', '" + accountType + "');";


        //Put the query into the iud function
        try{
            // add a query to see if it already exists in the database the email
            String checkQuery = "SELECT COUNT(*) AS count FROM user WHERE email = '" + email + "';";
            ResultSet rs = DatabaseHandler.search(checkQuery);
            if (rs.next() && rs.getInt("count") > 0) {
                showAlert("Email Already Exists", "An account with this email already exists. Please use a different email.");
                return;
            }

            DatabaseHandler.iud(query);
            showAlert("Registration Successful", "You have successfully registered");
            //Should go to the login page from here
            try{
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/oodprojectfx/views/login-view.fxml"));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch(IOException e)
            {
              e.printStackTrace();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            showAlert("Registration Failed", "An error occurred while registering. Please try again.");
        }
    }

    public static boolean isValidEmail(String email) {
        // Regex pattern for a valid email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @FXML
    public void onadminCheckboxClick(ActionEvent event) {
        if(adminOptionCheckbox.isSelected())
        {
            // Create an alert with a password field
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Admin Password");
            alert.setHeaderText("Enter admin password");

            // Create password field with alert
            PasswordField adminPasswordField = new PasswordField();
            adminPasswordField.setPromptText("Admin Password");

            // Set the password field in the alert's content
            alert.getDialogPane().setContent(adminPasswordField);

            // show the alert and wait for the user to submit
            alert.showAndWait();

            // Retrieve the entered password after the dialog is closed
            String adminPassword = adminPasswordField.getText();

            if (adminPassword.equals("expectedAdminPassword"))
            {
                // Replace with actual check
                // Proceed with admin registration

                System.out.println("Admin password accepted");
            }
            else {
                showAlert("Invalid Admin Password", "The admin password you entered is wrong");
                adminOptionCheckbox.setSelected(false);
            }
        }
    }

    @FXML
    public void onSignInButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/login-view.fxml");
    }

    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void changeToNextScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
