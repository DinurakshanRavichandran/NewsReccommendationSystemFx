package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UserArticleViewViewController {

    @FXML
    public Button backButton;
    @FXML
    public TextField titleField;
    @FXML
    public TextField authorField;
    @FXML
    public TextArea contentField;
    @FXML
    public TextField datePublishedField;
    @FXML
    public TextField categoryField;

    @FXML
    public void setArticle(Article article)
    {

    }

    @FXML
    public void onBackButtonClick(ActionEvent event) {
    }

    @FXML
    public void onSkipButtonClick(ActionEvent event) {
    }

    @FXML
    public void onReadButtonClick(ActionEvent event) {
    }
}
