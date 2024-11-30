package com.example.oodprojectfx.models;

public class UserSession {
    //Thread local to store user sessions for each thread independently
    private static final ThreadLocal<UserSession> threadLocalInstance = ThreadLocal.withInitial(UserSession :: new);
    private User currentUser;

    private UserSession()
    {

    }

    // private constructor to enforce singleton
    public static UserSession getInstance()
    {
        return threadLocalInstance.get();
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
