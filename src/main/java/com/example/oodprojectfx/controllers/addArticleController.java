package com.example.oodprojectfx.controllers;


import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.services.NLPPipeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class addArticleController  {

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

    private String escapeSingleQuotes(String input) {
        return input.replace("'", "''");
    }

    public void onSaveButtonClick(ActionEvent event) {
        String title = escapeSingleQuotes(titleField.getText().trim());
        String content = escapeSingleQuotes(contentField.getText().trim());
        String author = escapeSingleQuotes(authorField.getText().trim());
        String articleText = escapeSingleQuotes(articleTextField.getText().trim());
        String datePublished = escapeSingleQuotes(datePublishedField.getText().trim());

        //check for empty fields
        if(title.isEmpty() || content.isEmpty() || articleText.isEmpty() || author.isEmpty() || datePublished.isEmpty())
        {
            showAlert("Empty fields", "Please fill all the fields");
            return;
        }
        //Categorize the article
        NLPPipeline nlpPipeline = NLPPipeline.getInstance();
        String category = nlpPipeline.categorize(articleText);
        //create query to add fields to database

        String query = "INSERT INTO article (title, content, author, articleText, date_published, category) " +
                "VALUES ('" + title + "', '" + content + "', '" + author + "', '" + articleText + "', '" + datePublished + "', '" + category + "')";

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

    public void onBackButtonClick(ActionEvent event) {
        changeScene(event, "/com/example/oodprojectfx/views/articlePage-view.fxml");
    }
}
