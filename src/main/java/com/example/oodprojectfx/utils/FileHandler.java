package com.example.oodprojectfx.utils;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.services.NLPPipeline;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileHandler {

    public static void addArticlesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Connection connection = DatabaseHandler.connect()) { // Obtain a database connection
            String line;
            br.readLine(); // Skip the header line

            // Prepared statements for duplicate check and insertion
            String duplicateCheckQuery = "SELECT * FROM article WHERE title = ? OR articleText = ?";
            String insertQuery = "INSERT INTO article (title, content, author, articleText, date_published, category) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the statements once, outside the loop
            PreparedStatement duplicateCheckStmt = connection.prepareStatement(duplicateCheckQuery);
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length != 5) {
                    System.out.println("Invalid line format: " + line);
                    continue;
                }

                String title = fields[0].trim().replaceAll("^\"|\"$", "");
                String content = fields[1].trim().replaceAll("^\"|\"$", "");
                String author = fields[2].trim().replaceAll("^\"|\"$", "");
                String articleText = fields[3].trim().replaceAll("^\"|\"$", "");
                String datePublished = fields[4].trim().replaceAll("^\"|\"$", "");

                try {
                    // Check for duplicates
                    duplicateCheckStmt.setString(1, title);
                    duplicateCheckStmt.setString(2, articleText);
                    ResultSet resultSet = duplicateCheckStmt.executeQuery();
                    if (resultSet.next()) {
                        System.out.println("Duplicate article found, skipping: " + title);
                        continue;
                    }

                    // Categorize the article
                    NLPPipeline nlpPipeline = NLPPipeline.getInstance();
                    String category = nlpPipeline.categorize(articleText);

                    // Insert the article into the database
                    insertStmt.setString(1, title);
                    insertStmt.setString(2, content);
                    insertStmt.setString(3, author);
                    insertStmt.setString(4, articleText);
                    insertStmt.setString(5, datePublished);
                    insertStmt.setString(6, category);

                    insertStmt.executeUpdate();
                    System.out.println("Article added successfully: " + title);

                } catch (SQLException e) {
                    System.out.println("Error processing article: " + title);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading the file: " + filePath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        addArticlesFromFile("C:\\Users\\Dinurakshan\\OneDrive\\Desktop\\Second Year Sem 1\\Courseworks\\Object oriented programming\\OODCW\\OOD-ProjectFx\\src\\main\\resources\\com\\example\\oodprojectfx\\views\\ArticleDataSet.csv"); // Replace with your actual file path
    }
}
