package com.example.oodprojectfx.models;

public class Article {
    private String articleId;
    private String title;
    private String author;
    private String content;
    private String category;
    private String articleText;
    private String datePublished;

    public Article()
    {

    }

    public Article(String articleId, String title, String author, String content, String category, String articleText, String datePublished) {
        this.articleId = articleId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.category = category;
        this.articleText = articleText;
        this.datePublished = datePublished;
    }

    private int noOfLikes;

    public int getNo_of_read() {
        return no_of_read;
    }

    public void setNo_of_read(int no_of_read) {
        this.no_of_read = no_of_read;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public int getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(int noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    private int no_of_read;


}