package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addArticleController implements Initializable {
    @FXML
    public Button dashboardButton;
    @FXML
    public Button articleButton;
    @FXML
    public Button viewLogsButton;
    @FXML
    public TextField contentField;
    @FXML
    public TextField authorField;
    @FXML
    public TextArea abstractField;
//    @FXML
//    public TextField fileLinkField;
    @FXML
    public ChoiceBox<String> categoryField;
    @FXML
    public Button addImageButton;
    @FXML
    public ImageView imageField;
    @FXML
    public TextField titleField;
    @FXML
    public Button backButton;
    @FXML
    public TextArea fileLinkField;

    private String imagePath;
    private String[] category = {"Business", "Sports", "Technology", "Celebrity", "Politics"};
    private int article_id = 10000000;



    @FXML
    public void onDashboardButtonClick(ActionEvent event) {
    }

    @FXML
    public void onArticleButtonClick(ActionEvent event) {
        changeScene(event, "");
    }

    @FXML
    public void onViewLogsButtonClick(ActionEvent event) {
    }

    public void onBackButtonClick(ActionEvent event) {
    }

    public void onAddImageButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));

        // Getting stage from the event
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null)
        {
            imagePath = selectedFile.getAbsolutePath();
            imageField.setImage(new Image(selectedFile.toURI().toString()));
        }

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

    public void onAddButtonClick(ActionEvent event) {
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getSelectionModel().getSelectedItem();
        String abstractText = abstractField.getText().trim();
        String fileLink = fileLinkField.getText().trim();

        // Generate unique article ID (e.g., 10000001, 10000002, etc.)
        String articleId = String.format("%08d", ++article_id);
        //validate input
        if (title.isEmpty() || content.isEmpty() || author.isEmpty() || category.isEmpty() || fileLink.isEmpty()) {
            showAlert("Error", "All fields (title, content, author, category, and fileLink) are required.");
            return;
        }

        // Build SQL query to insert the article
        String query = String.format(
                "INSERT INTO article (article_id, title, content, author, category, content_text, article_material, imagePath) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                articleId, title, content, author, category, abstractText, fileLink, imagePath
        );
        //Execute query
        try {
            DatabaseHandler.iud(query);
            showAlert("Success", "Article added.");
        }catch (Exception e)
        {
            e.printStackTrace();
            showAlert("Error", "Failed to add article, please try again");
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/oodprojectfx/views/articlePage-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryField.getItems().addAll(category);

    }

}
