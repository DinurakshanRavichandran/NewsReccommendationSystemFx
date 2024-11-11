package com.example.oodprojectfx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/news_recommendation_system";
    private static final String USERNAME = "root";  // Replace with your actual MySQL username
    private static final String PASSWORD = "";  // Replace with your actual MySQL password
    private static Connection connection = null;

    // Method to connect to the database
    public static Connection connect() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully.");
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Failed to connect to the database.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Method to execute INSERT, UPDATE, DELETE operations
    public static void iud(String query) {
        try {
            Statement statement = connect().createStatement();
            statement.executeUpdate(query);
            System.out.println("Query executed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing query: " + query);
            e.printStackTrace();
        }
    }

    // Method to execute SELECT operations and return ResultSet
    public static ResultSet search(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connect().createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + query);
            e.printStackTrace();
        }
        return resultSet;
    }

    // Optional: Method to close the connection when done
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }
}
