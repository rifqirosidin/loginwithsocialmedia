package com.example.tugaseai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPost extends AppCompatActivity {
    EditText edtTitle, edtBody;
    Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        edtTitle = findViewById(R.id.edit_judul);
        edtBody = findViewById(R.id.edit_body);
        btnUpdate = findViewById(R.id.btn_update);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String body = intent.getStringExtra("BODY");
        final int id = intent.getIntExtra("ID", 0);

        edtTitle.setText(title);
        edtBody.setText(body);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost(id, edtTitle.getText().toString().trim(), edtBody.getText().toString().trim());
            }
        });
    }

    public void updatePost(int id, String title, String body){
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);
        Call<Post> call = api.update_post(id, title, body);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getStatus(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditPost.this, HomePage.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });

    }
}
