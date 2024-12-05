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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class articlePageController {

    @FXML
    public Button addArticleButton;
    @FXML
    public TableView<Article> articleTable;
    @FXML
    public TableColumn<Article, String> titleField;
    @FXML
    public TableColumn<Article, String> authorField;
    @FXML
    public TableColumn viewButtonField;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        titleField.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorField.setCellValueFactory(new PropertyValueFactory<>("author"));


        //viewButtonLogic
        viewButtonField.setCellFactory(new Callback<TableColumn<Article, Void>, TableCell<Article, Void>>() {
            @Override
            public TableCell<Article, Void> call(TableColumn<Article, Void> param) {
                return new TableCell<Article, Void>() {
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
        });

        //Load the articles from the database
        loadArticles();


        // Bind the list to the table view
        articleTable.setItems(articleList);
    }
    // This method is so that when the view button is clicked the relavant article's details are passed onto the next scene.
    private void showArticleDetails(ActionEvent event, Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodprojectfx/views/articleAdminView-view.fxml"));
            Parent root = loader.load();

            // Pass the article details to the ArticleDetailsController
            ArticleAdminViewController controller = loader.getController();
            controller.setArticle(article);

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
            String query = "SELECT article_id, title, content, author, articleText, date_published FROM article";
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
                articleList.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading articles: " + e.getMessage());
        }
    }

    public void onAddArticleButtonClick(ActionEvent event) {
        changeScene(event, "/com/example/oodprojectfx/views/addArticles-view.fxml");
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


    public void onDashboardButtonClick(ActionEvent event) {
        changeScene(event, "/com/example/oodprojectfx/views/admin-view.fxml");
    }

    public void onLogoutButtonClick(ActionEvent event) {
        UserSession.getInstance().clearSession();
        changeScene(event, "/com/example/oodprojectfx/views/login-view.fxml");
    }
}
