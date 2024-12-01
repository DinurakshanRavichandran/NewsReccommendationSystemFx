package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class adminController
{
    @FXML
    public Button viewLogsButton;
    @FXML
    public Button articleButton;
    @FXML
    public Button dashboard;

    @FXML
    public void onArticleButtonClick(ActionEvent event) {
        changeScene(event, "/com/example/oodprojectfx/views/articlePage-view.fxml");

    }

    @FXML
    public void onDashboardButtonClick(ActionEvent event) {
    }

    @FXML
    public void onViewLogsButtonClick(ActionEvent event) {

    }

    private void changeScene(ActionEvent event, String fxmlFile) {
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

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
        changeScene(event, "/com/example/oodprojectfx/views/login-view.fxml");
    }
}
