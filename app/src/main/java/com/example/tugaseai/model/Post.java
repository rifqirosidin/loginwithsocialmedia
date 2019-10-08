package com.example.tugaseai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;



    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    public Post(String title, String body, int id, String status) {
        this.title = title;
        this.body = body;
        this.id = id;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }
    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }


    public String getStatus()
    {
        return status;
    }



}
