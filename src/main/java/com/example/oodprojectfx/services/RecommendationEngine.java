package com.example.oodprojectfx.services;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.models.Article;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.*;

public class RecommendationEngine {
    private static final int DEFAULT_SCORE = 10;
    private static final int TOTAL_RECOMMENDED_ARTICLES = 50;
    private static final int LIKE_SCORE = 1;
    private static final int READ_SCORE = 1;
    private static final int SKIP_SCORE = -1;

    private static final String[] CATEGORIES = {"Politics", "Sports", "Technology", "Celebrity", "Business"};
    private static final int THREAD_POOL_SIZE = 5; // Adjust based on hardware resources

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public Map<String, Integer> recommendedArticles(String userEmail) {
        Map<String, Integer> categoryScores = initializeDefaultScores();
        Map<String, Integer> categoryCounts = new HashMap<>();

        try {
            String query = "SELECT a.category, u.liked_article, u.skipped_article, u.read_article " +
                    "FROM user_article_alloc u " +
                    "JOIN article a ON u.article_id = a.article_id " +
                    "WHERE u.email = '" + userEmail + "'";
            ResultSet resultSet = DatabaseHandler.search(query);

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                boolean liked = resultSet.getBoolean("liked_article");
                boolean skipped = resultSet.getBoolean("skipped_article");
                boolean read = resultSet.getBoolean("read_article");

                if (liked) categoryScores.put(category, categoryScores.get(category) + LIKE_SCORE);
                if (skipped) categoryScores.put(category, categoryScores.get(category) + SKIP_SCORE);
                if (read) categoryScores.put(category, categoryScores.get(category) + READ_SCORE);
            }

            int totalScore = categoryScores.values().stream().mapToInt(Integer::intValue).sum();
            for (String category : CATEGORIES) {
                int score = categoryScores.getOrDefault(category, DEFAULT_SCORE);
                int articleCount = (int) Math.round((double) score / totalScore * TOTAL_RECOMMENDED_ARTICLES);
                categoryCounts.put(category, articleCount);
            }
        } catch (Exception e) {
            System.out.println("Error calculating recommendations: " + e.getMessage());
            e.printStackTrace();
        }

        return categoryCounts;
    }

    public List<Article> getRecommendedArticles(String userEmail) {
        Map<String, Integer> categoryCounts = recommendedArticles(userEmail);
        List<Future<List<Article>>> futures = new ArrayList<>();
        List<Article> recommendedArticles = new ArrayList<>();

        // Submit tasks for fetching articles by category
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            String category = entry.getKey();
            int count = entry.getValue();
            futures.add(executorService.submit(() -> fetchArticlesByCategory(category, count, userEmail)));
        }

        // Collect results from each future
        for (Future<List<Article>> future : futures) {
            try {
                recommendedArticles.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error fetching articles: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return recommendedArticles;
    }

    private List<Article> fetchArticlesByCategory(String category, int articleCount, String userEmail) {
        List<Article> articles = new ArrayList<>();
        try {
            String query = "SELECT a.article_id, a.title, a.category, a.content, a.author, a.articleText, " +
                    "a.date_published, COUNT(ua.liked_article) AS like_count " +
                    "FROM article a " +
                    "LEFT JOIN user_article_alloc ua ON a.article_id = ua.article_id AND ua.liked_article = true " +
                    "WHERE a.category = '" + category + "' " +
                    "GROUP BY a.article_id, a.title, a.category, a.content, a.author, a.articleText, a.date_published " +
                    "ORDER BY like_count DESC";
            ResultSet resultSet = DatabaseHandler.search(query);

            while (resultSet.next() && articles.size() < articleCount) {
                String articleId = String.valueOf(resultSet.getInt("article_id"));
                String title = resultSet.getString("title");
                String articleCategory = resultSet.getString("category");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String articleText = resultSet.getString("articleText");
                String datePublished = resultSet.getString("date_published");
                int likeCount = resultSet.getInt("like_count");

                if (!hasUserReadArticle(userEmail, articleId)) {
                    articles.add(new Article(articleId, title, articleCategory, content, author, articleText, datePublished, likeCount));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching articles: " + e.getMessage());
        }

        return articles;
    }

    private boolean hasUserReadArticle(String userEmail, String articleId) {
        try {
            String query = "SELECT * FROM user_article_alloc " +
                    "WHERE email = '" + userEmail + "' AND article_id = " + articleId + " AND read_article = true";
            ResultSet resultSet = DatabaseHandler.search(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error checking if user has read the article: " + e.getMessage());
            return false;
        }
    }

    private Map<String, Integer> initializeDefaultScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (String category : CATEGORIES) {
            scores.put(category, DEFAULT_SCORE);
        }
        return scores;
    }
}
