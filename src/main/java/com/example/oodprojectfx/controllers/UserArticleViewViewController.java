package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

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

    private List<Article> articleList; // This is to store the articles passed from the article page user view
    private Article selectedArticle; // Passed for storing the article object

    @FXML
    public void setArticle(Article article)
    {
        this.selectedArticle = article;

        //Populate fields with article details
        titleField.setText(article.getTitle());
        authorField.setText(article.getAuthor());
        contentField.setText(article.getContent());
        datePublishedField.setText(article.getDatePublished());
        categoryField.setText(article.getCategory());

    }

    @FXML
    public void setArticleList(List<Article> articleList )
    {
        this.articleList = articleList;
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
