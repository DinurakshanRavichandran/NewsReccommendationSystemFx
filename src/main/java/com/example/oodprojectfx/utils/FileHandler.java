package com.example.oodprojectfx.utils;

import com.example.oodprojectfx.database.DatabaseHandler;
import com.example.oodprojectfx.services.NLPPipeline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileHandler {

    public static void addArticlesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line

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
                    // Check for duplicates using DatabaseHandler
                    String duplicateCheckQuery = "SELECT * FROM article WHERE title = '" + title + "' OR articleText = '" + articleText + "'";
                    ResultSet resultSet = DatabaseHandler.search(duplicateCheckQuery);
                    if (resultSet != null && resultSet.next()) {
                        System.out.println("Duplicate article found, skipping: " + title);
                        continue;
                    }

                    // Categorize the article
                    NLPPipeline nlpPipeline = NLPPipeline.getInstance();
                    String category = nlpPipeline.categorize(articleText);

                    // Insert the article into the database
                    String insertQuery = "INSERT INTO article (title, content, author, articleText, date_published, category) " +
                            "VALUES ('" + title + "', '" + content + "', '" + author + "', '" + articleText + "', '" + datePublished + "', '" + category + "')";
                    DatabaseHandler.iud(insertQuery);
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
