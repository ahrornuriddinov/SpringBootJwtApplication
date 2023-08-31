package com.mohirdev.mohirdevlesson.controller.vm;

public class LoginVM {
    private String username;
    private String password;
    private boolean RememberMe;

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

    public boolean isRememberMe() {
        return RememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        RememberMe = rememberMe;
    }
}
