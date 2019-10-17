package com.example.tugaseai.ajeng;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tugaseai.MainActivity;
import com.example.tugaseai.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieList extends AppCompatActivity {
    RecyclerView recyclerView;
    private MovieAdapter adapter;
    private GoogleSignInClient googleSignInClient;
    ArrayList <movie> listMovie = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielist);

        recyclerView = findViewById(R.id.recyclerviewMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setDataFromAPI();
    }

    public void setDataFromAPI()
    {
        API api = serviceAPI.getAPIdata().create(API.class);
        Call<responseAPI> call = api.getMovieData();
        call.enqueue(new Callback<responseAPI>() {
            @Override
            public void onResponse(Call<responseAPI> call, Response<responseAPI> response) {
                ArrayList<dataAPI> dataAPIArrayList = response.body().getResults();
                adapter = new MovieAdapter(getApplicationContext(), dataAPIArrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<responseAPI> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
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
                Intent intent=new Intent(MovieList.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


}
