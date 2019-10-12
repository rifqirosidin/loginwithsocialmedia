package com.example.tugaseai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.tugaseai.adapter.PostAdapter;
import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.Post;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter adapter;
    String nama;
    private GoogleSignInClient googleSignInClient;
    private AppPreferences appPreferences = null;
    private String token = null;
    private View info = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_list_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
        recyclerView.setHasFixedSize(true);
        getPostData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AddPost.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        nama = intent.getStringExtra("AKUN");
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
        recyclerView = findViewById(R.id.rv_list_post);
        adapter = new PostAdapter(this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomePage.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        if (nama != ""){
            menu.add(nama);
        }
        inflater.inflate(R.menu.menu, menu);


        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.logout){
          SignOut();

        }


        return true;
    }

    public void SignOut()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //On Succesfull signout we navigate the user back to LoginActivity
                Intent intent=new Intent(HomePage.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        LoginManager.getInstance().logOut();
        logout();
    }

    public void logout() {
        appPreferences = new AppPreferences(this);
        info = findViewById(R.id.info);
        token = null;
        appPreferences.clear();
    }

}
