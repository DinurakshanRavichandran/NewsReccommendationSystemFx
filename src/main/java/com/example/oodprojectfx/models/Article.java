package com.example.oodprojectfx.models;

public class Article {
    private String articleId;
    private String title;
    private String author;
    private String content;
    private String category;
    private String contentText;
    private String contentLink;
    private String imagePath;

    public Article(String articleId, String title, String author, String content, String category, String contentText, String contentLink, String imagePath) {
        this.articleId = articleId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.category = category;
        this.contentText = contentText;
        this.contentLink = contentLink;
        this.imagePath = imagePath;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }



}
