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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
    }

    @FXML
    public void onHomeButtonClick(ActionEvent event) {
        // Navigate to home
    }

    @FXML
    public void onArticleButtonClick(ActionEvent event) {
        // Navigate to articles
    }

    @FXML
    public void onRecommendationButtonClick(ActionEvent event) {
        // Refresh recommendations
        loadRecommendedArticlesAsync();
    }

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
    }
}
