package com.mygdx.game.utils;

public class User {
    String username;
    String email;
    String password;
    boolean admin;
    String status;

    public User(){
        username = "null";
        email = "null";
        password = "null";
        admin = false;
        status = "null";
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", status='" + status + '\'' +
                '}';
    }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setStatus(String status) { this.status = status; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public String getStatus() { return this.status; }
    public boolean getAdmin() { return this.admin; }
}
