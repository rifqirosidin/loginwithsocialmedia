package com.example.tugaseai;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class DetailPost extends AppCompatActivity {

    TextView tvTitle, tvBody;
    Button btnEdit, btnDelete;
    int idPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        tvTitle = findViewById(R.id.detail_title);
        tvBody = findViewById(R.id.detail_body);
        btnDelete = findViewById(R.id.delete_post);
        btnEdit = findViewById(R.id.edit_post);


        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String body = intent.getStringExtra("BODY");
        idPost = intent.getIntExtra("ID", 0);

        Log.d("idpost", String.valueOf(idPost));

        tvTitle.setText(title);
        tvBody.setText(body);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost(idPost);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPost.this);
                builder.setTitle("Apakah anda yakin?");
                builder.setMessage("Menghapus postingan ini");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete(idPost);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void delete(int id)
    {
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);

        Call<Post> call = api.delete_post(id);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();

                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DetailPost.this, HomePage.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("delete", t.getMessage() );
            }
        });
    }

    public void editPost(int id){
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);

        Call<Post> call = api.getPostEdit(id);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){

                    Intent intent = new Intent(DetailPost.this, EditPost.class);
                    intent.putExtra("TITLE", response.body().getTitle());
                    intent.putExtra("BODY", response.body().getBody());
                    intent.putExtra("ID", response.body().getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("delete", t.getMessage() );
            }
        });
    }
}
