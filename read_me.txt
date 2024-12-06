News Recommendation System
Welcome to the News Recommendation System project! This README file will guide you through the process of setting up the database required for the application and provide basic instructions for usage.

Prerequisites
Before proceeding, ensure you have the following installed:

A MySQL Database Server.
A MySQL Client or any compatible tool to execute SQL queries.
Programming language and dependencies required for the project (refer to the main documentation or requirements.txt).
Setting Up the Database
To use the application, you must first create the required database and tables.

Step 1: Create the Database
Execute the following SQL command to create the database:

sql
Copy code
CREATE DATABASE news_recommendation_system;
Step 2: Create the Tables
Switch to the newly created database and run the following SQL commands to create the necessary tables:

Create user Table
sql
Copy code
CREATE TABLE user (
    email VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    accountType VARCHAR(10) NOT NULL,
    CONSTRAINT usertable_email_primaryKey PRIMARY KEY (email)
);
Create article Table
sql
Copy code
CREATE TABLE article (
    article_id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(1000),
    content TEXT,
    category VARCHAR(50),
    author VARCHAR(1000),
    articleText TEXT,
    date_published VARCHAR(100),
    no_of_likes INT DEFAULT 0,
    no_of_read INT DEFAULT 0,
    PRIMARY KEY (article_id)
);
Create user_article_alloc Table
sql
Copy code
CREATE TABLE user_article_alloc (
    email VARCHAR(50) NOT NULL,
    article_id INT NOT NULL,
    liked_article BOOLEAN DEFAULT false,
    skipped_article BOOLEAN DEFAULT false,
    read_article BOOLEAN DEFAULT false,
    CONSTRAINT user_article_alloc_email_foreign_key FOREIGN KEY (email) REFERENCES user(email),
    CONSTRAINT user_article_alloc_article_id_foreign_key FOREIGN KEY (article_id) REFERENCES article(article_id),
    PRIMARY KEY (email, article_id)
);
Create recommendation Table
sql
Copy code
CREATE TABLE recommendation (
    recommendationId INT NOT NULL AUTO_INCREMENT,
    recommendationType VARCHAR(50) NOT NULL,
    score FLOAT,
    email VARCHAR(50),
    PRIMARY KEY (recommendationId),
    CONSTRAINT recommendation_table_foreign_key_email FOREIGN KEY (email) REFERENCES user(email)
);
Create rec_article_alloc Table
sql
Copy code
CREATE TABLE rec_article_alloc (
    recommendationId INT NOT NULL,
    article_id INT NOT NULL,
    CONSTRAINT rec_article_alloc_foreign_key_recommendationId FOREIGN KEY (recommendationId) REFERENCES recommendation(recommendationId),
    CONSTRAINT rec_article_alloc_foreign_key_article_id FOREIGN KEY (article_id) REFERENCES article(article_id),
    PRIMARY KEY (recommendationId, article_id)
);
Running the Application
Clone the repository and install any required dependencies using the provided setup instructions (refer to the main project documentation).
Ensure the database is set up and populated with appropriate data.
Configure the application to connect to your database. Update the database connection string or environment variables as required.
Start the application.


After creating the database for testing purposes kindly run the FileHandler class to upload articles to the database.