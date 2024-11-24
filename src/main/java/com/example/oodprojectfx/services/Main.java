package com.example.oodprojectfx.services;


    public class Main {
        public static void main(String[] args) {
            String article = "Artificial intelligence and machine learning are key technologies in modern computing.";

            // Get the singleton instance
            NLPPipeline pipeline = NLPPipeline.getInstance();

            // Call the categorize method
            String category = pipeline.categorize(article);

            System.out.println("The article belongs to the category: " + category);
        }
    }

