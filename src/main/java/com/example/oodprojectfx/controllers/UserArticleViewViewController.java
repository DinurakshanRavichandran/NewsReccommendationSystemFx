package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;
import com.example.oodprojectfx.models.User;
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
       if(articleList == null | articleList.isEmpty())
       {
           System.out.println("No articles available to skip");
           return;
       }

       //update the skipped article in the database
        try {
            UserSession userSession = UserSession.getInstance();
            String userEmail = userSession.getCurrentUser().getEmail();
            String articleId = articleList.get(currentIndex).getArticleId(); // This is a string in the database -- potential error

            //Check whether a row exist for the article
            String queryCheck = "SELECT * FROM user_article_alloc WHERE email = '" + userEmail + "' AND article_id = " + articleId;
            ResultSet resultSet = DatabaseHandler.search(queryCheck);
            if(!resultSet.next()) {
                //Insert new row if it doesn't exist
                String queryInsert = "INSERT INTO user_article_alloc (email, article_id) VALUES ('" + userEmail + "', " + articleId + ")";
                DatabaseHandler.iud(queryInsert);
                System.out.println("New record inserted into the article");
            }



            //Update skipped_article to true in the user_article_alloc table
            String querySkipArticle = "UPDATE user_article_alloc SET skipped_article = true " +
                    "WHERE email = '" + userEmail + "' AND article_id = " + articleId;

            DatabaseHandler.iud(querySkipArticle);
            System.out.println("Article marked as skipped in the database");
        }catch (Exception e)
        {
            System.out.println("Error updating skipped article in the database");
            e.printStackTrace();

        }
        if (currentIndex < articleList.size() - 1)
        {
            currentIndex ++;
        }else{
            currentIndex = 0;
        }
        //update current article to the selected article
        setArticle(articleList.get(currentIndex));//..................
    }

    @FXML
    public void onReadButtonClick(ActionEvent event) throws SQLException {

        UserSession userSession = UserSession.getInstance();
        String userEmail = userSession.getCurrentUser().getEmail();
        String articleId = selectedArticle.getArticleId();

        //Check if the record exists in the user article alloc
        String queryCheck = "SELECT * FROM user_article_alloc WHERE email = '" + userEmail + "' AND article_id = " + articleId;
        ResultSet resultSet = DatabaseHandler.search(queryCheck);

        if(!resultSet.next())
        {
            //Insert a new record if it doesn't exist
            String queryInsert = "INSERT INTO user_article_alloc (email, article_id) VALUES ('" + userEmail + "', " + articleId + ")";
            DatabaseHandler.iud(queryInsert);
            System.out.println("New record inserted into user_article_alloc for reading.");
        }


        //Update read article to true in the database
        String queryReadArticle = "UPDATE user_article_alloc SET read_article = true " +
                "WHERE email = '" + userEmail + "' AND article_id = " + articleId;

        //Increment the number of read count
        String queryIncrementRead = "UPDATE article SET no_of_read = no_of_read + 1 WHERE article_id = " + articleId;
        DatabaseHandler.iud(queryIncrementRead);
        System.out.println("Article read count incremented ");

        DatabaseHandler.iud(queryReadArticle);
        try {
            //Load the next scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodprojectfx/views/userArticle-view.fxml"));
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
