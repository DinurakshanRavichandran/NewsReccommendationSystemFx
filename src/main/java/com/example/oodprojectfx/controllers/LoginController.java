package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;




    @FXML
    void onLoginButtonClick(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = PasswordField.getText().trim();
        // check if the email and password are empty
        if(email.isEmpty() || password.isEmpty())
        {
            showAlert("Empty fields", "Please fill in the required fields");
            return;
        }

        try{
            // queery for getting the password for the given email from the database
            String query = "SELECT password FROM user WHERE email = '" + email + "';";

            ResultSet resultSet = DatabaseHandler.search(query);

            if (resultSet.next())
            {
                String storedPassword = resultSet.getString("password");
                if(storedPassword.equals(password))
                {
                    //credentials are correct, proceed to next scene
                    changeToNextScene(event, "");
                }else{
                    // password incorrect
                    showAlert("Incorrect password", "The password you entered is incorrect");
                }
            }
            else{
                //email not found
                showAlert("Email not found", "The email you entered has not been recoreded in the system, enter correct email or register");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            showAlert("Database error", "There was an error in accessing the database");
        }




    }

    @FXML
    void onSignUpButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/register-view.fxml");
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
