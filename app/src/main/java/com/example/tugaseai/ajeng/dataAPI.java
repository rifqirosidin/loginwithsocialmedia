package com.example.tugaseai.ajeng;

import com.google.gson.annotations.SerializedName;

public class dataAPI {


    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("release_date")
    private String release_date;

    public dataAPI(String title, String poster_path, String release_date)
    {
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

}
