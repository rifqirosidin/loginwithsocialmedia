package com.example.tugaseai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    EditText edtName, edtUsername, edtEmail, edtPass, edtKonfirPass;
    Button register;
    TextView tvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edt_name);
        edtUsername = findViewById(R.id.editText_Username);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.editTextPass);
        edtKonfirPass = findViewById(R.id.editText_konfir_pass);
        tvLogin  = findViewById(R.id.tv_login);
        register = findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                userRegister(name,username,email,pass);
            }
        });


    }

    public void userRegister(String nama, String username, String email, String pass)
    {
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);
        Call<User> call = api.user_register(nama,username,email,pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "Register Successfuly", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Register Faield", Toast.LENGTH_LONG).show();
            }
        });
    }
}
