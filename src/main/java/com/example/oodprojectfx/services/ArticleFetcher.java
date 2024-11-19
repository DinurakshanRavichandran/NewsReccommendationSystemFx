package com.example.oodprojectfx.services;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ArticleFetcher {
    private static final String API_KEY = "02bcf0fbf9a64d029e4d1256731d7b2b";
    private static final String BASE_URL = "https://newsapi.org/v2/everything";

    /**
     * Fetch articles based on a keyword.
     *
     * @param keyword The search keyword for fetching articles.
     * @return A list of Article objects.
     */

    public List<Article> fetchArticlesByKeyword(String keyword)
    {
        List<Article> articles = new ArrayList<>();
        try{
            // Build the API URL
            String url = String.format("%s?q=%s&apiKey=%s", BASE_URL, keyword, API_KEY);
            //Create an HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            //Send the response and get the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.body());

            JsonNode articlesNode = root.get("articles");
            if(articlesNode != null)
            {
                for(JsonNode articleNode : articlesNode)
                {
                    String title = articleNode.get("title").asText();
                    String content = articleNode.has("content") ? articleNode.get("content").asText() : "No content available";
                    String author = articleNode.has("author") ? articleNode.get("author").asText() : "Unknown";
                    String urlToArticle = articleNode.get("url").asText();
                    String imageUrl = articleNode.has("urlToImage") ? articleNode.get("urlToImage").asText() : null;

                    // Create an Article object and add it to the list
                    articles.add(new Article(title, author, content, "General", urlToArticle, imageUrl));
                }
            }




        }catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Error fetching articles: " + e.getMessage());

        }

        return articles;
    }
    // Method to save a single Article to the database
    public static void saveArticle(Article article) {
        String query = "INSERT INTO articles (title, author, content, category, urlToArticle, image_path) VALUES ("
                + "'" + article.getTitle() + "', "
                + "'" + article.getAuthor() + "', "
                + "'" + article.getContent() + "', "
                + "'" + article.getCategory() + "', "
                + "'" + article.getContentLink() + "', "
                + "'" + article.getImagePath() + "')";
        try {
            DatabaseHandler.iud(query);
        } catch (Exception e) {
            System.out.println("Error saving article: " + article.getTitle());
            e.printStackTrace();
        }
    }

    // Method to save a list of Articles to the database
    public static void saveArticles(List<Article> articles) {
        for (Article article : articles) {
            saveArticle(article);
        }
    }



}
