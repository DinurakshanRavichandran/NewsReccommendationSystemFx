package com.example.oodprojectfx.controllers;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;
import com.example.oodprojectfx.models.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticlePageUserViewController {

    @FXML
    public TableView<Article> articleTable;
    @FXML
    public TableColumn<Article, String> titleColumn;
    @FXML
    public TableColumn<Article, String> authorColumn;
    @FXML
    public TableColumn<Article, Void> viewButtonColumn;
    @FXML
    public Label userNameTag;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Set the user's name tag
        String userName = UserSession.getInstance().getCurrentUser().getUsername();
        userNameTag.setText("Welcome, " + userName + "!");


        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // View button logic
        viewButtonColumn.setCellFactory(new Callback<TableColumn<Article, Void>, TableCell<Article, Void>>() {
            @Override
            public TableCell<Article, Void> call(TableColumn<Article, Void> param) {
                return new TableCell<Article, Void>() {
                    private final Button viewButton = new Button("View");

                    {
                        viewButton.setOnAction(event -> {
                            Article article = getTableView().getItems().get(getIndex());
                            showArticleDetails(event, article, articleList);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : viewButton);
                    }
                };
            }
        });

        // Load articles from the database
        loadArticles();

        // Bind the list to the TableView
        articleTable.setItems(articleList);
    }


    private void showArticleDetails(ActionEvent event, Article article, ObservableList<Article> articleList) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodprojectfx/views/userArticleView-view.fxml"));
            Parent root = loader.load();

            // Pass the article details to the ArticleDetailsController
            UserArticleViewController controller = loader.getController();
            controller.setArticle(article);// sending the article from this class to the next
            controller.setArticleList(articleList); // sending the article list from this class to the next

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadArticles() {
        try {
            // Query to fetch only the required columns
            String query = "SELECT article_id, title, content, author, articleText, date_published, category FROM article";
            ResultSet resultSet = DatabaseHandler.search(query);

            // Iterate through the result set and add articles to the list
            while (resultSet.next()) {
                Article article = new Article();
                article.setArticleId(resultSet.getString("article_id"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setAuthor(resultSet.getString("author"));
                article.setArticleText(resultSet.getString("articleText"));
                article.setDatePublished(resultSet.getString("date_published"));
                article.setCategory(resultSet.getString("category"));
                articleList.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading articles: " + e.getMessage());
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
        changeToNextScene(event, "/com/example/oodprojectfx/views/articlePageUserView-view.fxml");
    }

    public void onRecommendationButtonClick(ActionEvent event) {
        changeToNextScene(event, "/com/example/oodprojectfx/views/recommendation-view.fxml");
    }

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
        changeToNextScene(event, "/com/example/oodprojectfx/views/login-view.fxml");
    }
}
