package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;
import com.example.oodprojectfx.models.UserSession;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleController {

    @FXML
    public TextField titleField;
    @FXML
    public TextArea articleTextField;
    @FXML
    public Button likeButton;
    private Article selectedArticle;
    UserSession userSession;

    @FXML
    public void onBackButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/articlePageUserView-view.fxml");

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

    @FXML
    public void onLikeButtonClick(ActionEvent event) {
        userSession = UserSession.getInstance();
        String userEmail = userSession.getCurrentUser().getEmail();
        String articleId =  selectedArticle.getArticleId(); // in the database the article id is an int

        try{
            //check if the article is already liked
            String queryCheck = "SELECT liked_article FROM user_article_alloc WHERE email = '" + userEmail + "' AND article_id = " + articleId;
            ResultSet resultSet = DatabaseHandler.search(queryCheck);

            boolean isLiked = false;
            if(resultSet.next()) {
                isLiked = resultSet.getBoolean("liked_article");

            }if (!isLiked)
            {
                //If not liked like the article
                String queryLikeArticle = "UPDATE article SET no_of_likes = no_of_likes + 1 WHERE article_id = " + articleId;
                DatabaseHandler.iud(queryLikeArticle);

                String queryLikeAlloc = "UPDATE user_article_alloc SET liked_article = true WHERE email = '" + userEmail + "' AND article_id = " + articleId;
                DatabaseHandler.iud(queryLikeAlloc);

                likeButton.setText("Liked");


            }else{

                //If the article is already liked unlike the article
                String queryUnlikeArticle = "UPDATE article SET no_of_likes = no_of_likes - 1 WHERE article_id = " + articleId;
                DatabaseHandler.iud(queryUnlikeArticle);

                String queryUnlikeAlloc = "UPDATE user_article_alloc SET liked_article = false WHERE email = '" + userEmail + "' AND article_id = " + articleId;
                DatabaseHandler.iud(queryUnlikeAlloc);

                likeButton.setText("like");
            }
        }catch (SQLException e)
        {
            System.out.println("Error handling like unlike action");
            e.printStackTrace();

        }

    }

    @FXML
    public void setArticle(Article article)
    {
        //Populate the fields with the article's details
        titleField.setText(article.getTitle());
        articleTextField.setText(article.getArticleText());
        this.selectedArticle = article;
    }

}
