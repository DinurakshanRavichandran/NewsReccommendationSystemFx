package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
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
    private int currentIndex;

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
        this.currentIndex = 0; // start at the first index

    }

    private void loadArticleAtIndex(int index)
    {
        if(index >= 0  && index < articleList.size())
        {
            Article article = articleList.get(index);
            setArticle(article);
        }else{
            System.out.println("No more articles to display ");
        }
    }

    @FXML
    public void onBackButtonClick(ActionEvent event) {
    }

    @FXML
    public void onSkipButtonClick(ActionEvent event) {
        if(currentIndex < articleList.size() - 1)
        {
            currentIndex ++;
        }else {
            currentIndex = 0;
        }
        // Update selectedArticle to the current article
        setArticle(articleList.get(currentIndex));
    }

    @FXML
    public void onReadButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodprojectfx/views/articlePage-view.fxml"));
            Parent root = loader.load();

            // Pass the article details to the ArticleDetailsController
            ArticleController controller = loader.getController();
            controller.setArticle(selectedArticle);// sending the article from this class to the next


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
