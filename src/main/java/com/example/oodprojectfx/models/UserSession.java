package com.example.oodprojectfx.models;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession()
    {

    }// private constructor to enforce singleton

    public static UserSession getInstance()
    {
        if(instance ==  null)
        {
            instance = new UserSession();
        }
        return instance;
    }

    public void setCurrentUser( User currentUser)
    {
        this.currentUser = currentUser;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void clearSession()
    {
        currentUser = null; // Reset user session
    }
}
