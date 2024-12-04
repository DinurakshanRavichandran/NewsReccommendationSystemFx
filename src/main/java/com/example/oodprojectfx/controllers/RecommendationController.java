package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.models.Article;
import com.example.oodprojectfx.models.UserSession;
import com.example.oodprojectfx.services.RecommendationEngine;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class RecommendationController {

    @FXML
    public TableView<Article> articleTable;
    @FXML
    public TableColumn<Article, String> titleField;
    @FXML
    public TableColumn<Article, String> authorField;
    @FXML
    public TableColumn<Article, Void> viewButtonField;

    private final ObservableList<Article> articleList = FXCollections.observableArrayList();
    private final RecommendationEngine recommendationEngine = new RecommendationEngine();
    public Label userNameTag;

    @FXML
    public void initialize() {

        // Set the user's name tag
        String userName = UserSession.getInstance().getCurrentUser().getUsername();
        userNameTag.setText("Welcome, " + userName + "!");

        // Bind table columns
        titleField.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorField.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Configure view button column
        viewButtonField.setCellFactory(getViewButtonCellFactory());

        // Load recommended articles in the background
        loadRecommendedArticlesAsync();

        // Bind the list to the TableView
        articleTable.setItems(articleList);
    }

    private void loadRecommendedArticlesAsync() {
        String userEmail = UserSession.getInstance().getCurrentUser().getEmail();

        // Create a Task to fetch recommendations
        Task<List<Article>> fetchRecommendationsTask = new Task<>() {
            @Override
            protected List<Article> call() throws Exception {
                return recommendationEngine.getRecommendedArticles(userEmail);
            }
        };

        // Update the TableView once the task is complete
        fetchRecommendationsTask.setOnSucceeded(event -> {
            articleList.setAll(fetchRecommendationsTask.getValue());
        });

        fetchRecommendationsTask.setOnFailed(event -> {
            Throwable error = fetchRecommendationsTask.getException();
            System.out.println("Error loading recommendations: " + error.getMessage());
            error.printStackTrace();
        });

        // Run the task in a background thread
        new Thread(fetchRecommendationsTask).start();
    }

    private Callback<TableColumn<Article, Void>, TableCell<Article, Void>> getViewButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Article, Void> call(TableColumn<Article, Void> param) {
                return new TableCell<>() {
                    private final Button viewButton = new Button("View");

                    {
                        viewButton.setOnAction(event -> {
                            Article article = getTableView().getItems().get(getIndex());
                            showArticleDetails(event, article);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(viewButton);
                        }
                    }
                };
            }
        };
    }

    private void showArticleDetails(ActionEvent event, Article article) {
        // Implementation for showing article details
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodprojectfx/views/userArticleView-view.fxml"));
            Parent root = loader.load();

            // Pass article data to details controller
            UserArticleViewViewController controller = loader.getController();
            controller.setArticle(article);
            controller.setArticleList(articleList);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    public void onHomeButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/homeUser-view.fxml");

    }

    @FXML
    public void onArticleButtonClick(ActionEvent event) {
        // Navigate to articles
        changeToNextScene(event, "/com/example/oodprojectfx/views/articlePageUserView-view.fxml");

    }

    @FXML
    public void onRecommendationButtonClick(ActionEvent event) {
        // Refresh recommendations
    }

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
    }
}