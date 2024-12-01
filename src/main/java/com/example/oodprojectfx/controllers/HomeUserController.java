package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeUserController {

    @FXML
    public void onHomeButtonClick(ActionEvent event) {

    }

    @FXML
    public void onArticleButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/articlePageUserView-view.fxml");
    }

    @FXML
    public void onRecommendationButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/recommendation-view.fxml");
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

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
        changeToNextScene(event, "/com/example/oodprojectfx/views/login-view.fxml");
    }
}
