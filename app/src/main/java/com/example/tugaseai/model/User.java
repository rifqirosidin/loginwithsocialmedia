package com.example.tugaseai.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;



    @SerializedName("user")
    ArrayList<User> user;


    public User(String username, String email, String name, String id, String status, ArrayList<User> user) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.id = id;
        this.status =status;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    public String getStatus()
    {
        return status;
    }
    public ArrayList<User> getUser() {
        return user;
    }
}
