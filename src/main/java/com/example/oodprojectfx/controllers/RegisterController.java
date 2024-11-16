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
    @FXML
    public void onLoginButtonClick(ActionEvent event) {
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
    }

    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
