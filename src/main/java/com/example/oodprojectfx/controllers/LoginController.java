package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.User;
import com.example.oodprojectfx.models.UserSession;
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
            // query for getting the password and account type for the given email from the database
            String query = "SELECT password, accountType, username FROM user WHERE email = '" + email + "';";

            ResultSet resultSet = DatabaseHandler.search(query);

            if (resultSet.next())
            {
                String storedPassword = resultSet.getString("password");
                String accountType = resultSet.getString("accountType"); // Retrieve accountType
                String username = resultSet.getString("username");
                if(storedPassword.equals(password))

                {
                    User currentUser = new User(email, username);
                    UserSession.getInstance().setCurrentUser(currentUser);
                    System.out.println("Session created");
                    //credentials are correct, proceed to next scene accordingly based on accountType
                    String fxmlPath;
                    if( accountType.equalsIgnoreCase("Admin"))
                    {
                        fxmlPath = "/com/example/oodprojectfx/views/admin-view.fxml";
                    }
                    else{
                        fxmlPath = "/com/example/oodprojectfx/views/homeUser-view.fxml";
                    }

                    //Load the specified FXML file
                    Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
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
        }catch (IOException e)
        {
            e.printStackTrace();
            showAlert("Load error", "An error has occured while loading the view ");
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
