package com.example.oodprojectfx.models;

public class User {
    private String email;
    private String username;
    private String password;
    private String accountType;

    public User(String email, String password, String username, String accountType) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.accountType = accountType;
    }
    public User(String username, String email)
    {
        this.username = username;
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
