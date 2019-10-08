package com.example.tugaseai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tugaseai.adapter.PostAdapter;
import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home extends AppCompatActivity {
    RecyclerView recyclerView;
    PostAdapter adapter;
    TextView tv_create_post;
//    ArrayList<Post> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.rv_post);
        tv_create_post = findViewById(R.id.create_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(home.this));
        recyclerView.setHasFixedSize(true);

        getPostData();

        tv_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, AddPost.class);
                startActivity(intent);
            }
        });
    }

    public void getPostData()
    {
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);
        Call<List<Post>> call = api.getPostData();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("error", t.getMessage() );
            }
        });

    }
    private void generateDataList(List<Post> list) {
        recyclerView = findViewById(R.id.rv_post);
        adapter = new PostAdapter(this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(home.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
