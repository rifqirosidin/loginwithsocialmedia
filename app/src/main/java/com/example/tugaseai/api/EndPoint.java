package com.example.tugaseai.api;

import com.example.tugaseai.model.Post;
import com.example.tugaseai.model.User;
import com.google.gson.annotations.Expose;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface EndPoint {

    @FormUrlEncoded
    @POST("login")
    Call<User> user_login(@Field("username") String username,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<User> user_register(@Field("name") String name,
                             @Field("username") String username,
                             @Field("email") String email,
                             @Field("password") String password);

    @GET("posts")
    Call<List<Post>> getPostData();

    @FormUrlEncoded
    @POST("posts/store")
    Call<Post> create_post(@Field("title") String title,
                            @Field("body") String body);


    @GET("posts/edit/{id}")
    Call<Post> getPostEdit(@Path("id") int id);


    @FormUrlEncoded
    @PUT("posts/edit/{id}")
    Call<Post> update_post(@Path("id") int id,@Field("title") String title,
                           @Field("body") String body);

    @DELETE("posts/delete/{id}")
    Call<Post> delete_post(@Path("id") int id);

}
