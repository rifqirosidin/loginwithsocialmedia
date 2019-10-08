package com.example.tugaseai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPost extends AppCompatActivity {

    TextView edt_title, edt_body;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        edt_title = findViewById(R.id.judul);
        edt_body = findViewById(R.id.desc);
        post = findViewById(R.id.btn_post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_title.getText().toString().trim();
                String body = edt_body.getText().toString().trim();
                addPost(title, body);
            }
        });

    }

    public void addPost(String title, String body){

        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);
        Call<Post> call = api.create_post(title, body);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    edt_body.setText("");
                    edt_title.setText("");
                    Intent intent = new Intent(AddPost.this, HomePage.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), status , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                Log.e("error", t.getMessage() );

            }
        });
    }
}
