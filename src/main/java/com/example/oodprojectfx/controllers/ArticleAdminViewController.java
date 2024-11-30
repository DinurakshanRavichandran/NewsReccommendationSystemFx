package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;
import com.example.oodprojectfx.services.NLPPipeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ArticleAdminViewController {

    @FXML
    public TextField titleField;
    @FXML
    public TextField contentField;
    @FXML
    public TextField authorField;
    @FXML
    public TextArea articleTextField;
    @FXML
    public TextField datePublishedField;
    @FXML
    public Button saveButton;

    @FXML
    public void setArticle(Article article)
    {
        titleField.setText(article.getTitle());
        contentField.setText(article.getContent());
        authorField.setText(article.getAuthor());
        articleTextField.setText(article.getArticleText());
        datePublishedField.setText(article.getDatePublished());
        titleField.setUserData(article.getArticleId()); // Store articleID

    }

    public void onSaveButtonClick(ActionEvent event) {
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();
        String author = authorField.getText().trim();
        String datePublished = datePublishedField.getText().trim();
        String articleText = articleTextField.getText().trim();

        if(title.isEmpty() || content.isEmpty() || author.isEmpty() || datePublished.isEmpty() || articleText.isEmpty())
        {
            showAlert("Empty fields", "Please fill all the fields");
            return;
        }
        //Get article id to pass onto query
        String articleId = titleField.getUserData().toString();

        //Categorize the article
        NLPPipeline nlpPipeline = NLPPipeline.getInstance();
        String category = nlpPipeline.categorize(articleText);

        //Create query to update fields to the database
        String query = "UPDATE article " +
                "SET title = '" + title + "', " +
                "content = '" + content + "', " +
                "author = '" + author + "', " +
                "date_published = '" + datePublished + "', " +
                "articleText = '" + articleText + "', " +
                "category = '" + category + "' " +
                "WHERE article_id = " + articleId;

        try{
            //call database handler to add
            DatabaseHandler.iud(query);
            showAlert("Registration Successful", " The article has been successfully added");

            try{
                //changeScene
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/oodprojectfx/views/articlePage-view.fxml"));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            showAlert("Adding article failed", "The article wasn't added please try again");
        }




    }
    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onDeleteButtonClick(ActionEvent event) {
        String articleId = titleField.getUserData().toString();

        // Confirm the deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Article");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this article?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) { // Proceed if user confirms
                // Construct the SQL DELETE query
                String query = "DELETE FROM article WHERE article_id = " + articleId;
                try {
                    //Execute the delete query
                    DatabaseHandler.iud(query);

                    // Show a success message
                    showAlert("Deletion Successful", "The article has been successfully deleted.");

                    // Redirect to the article page
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/com/example/oodprojectfx/views/articlePage-view.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Deletion Failed", "Failed to delete the article");
                }
            }
        });
    }

    public void onBackButtonClick(ActionEvent event) {
       changeScene(event, "/com/example/oodprojectfx/views/articlePage-view.fxml");
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
}
