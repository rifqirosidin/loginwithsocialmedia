package com.example.tugaseai.ajeng;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String API_KEY = "7439c984818f4a180a49f812b98c1f6a";

    @GET("discover/movie?api_key=" + API_KEY + "&language=en-US")
     Call<responseAPI> getMovieData();
}
