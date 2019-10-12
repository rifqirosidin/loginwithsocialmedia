package com.example.tugaseai;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugaseai.api.EndPoint;
import com.example.tugaseai.api.RetrofitClient;
import com.example.tugaseai.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AuthenticationListener {
    Boolean cekLoginFb = false;
    Button btnGoogle, btnFB, btnLogin;
    TextView tvDaftar;
    EditText edtUsername, edtPassword;
    private static final String TAG = "AndroidCarified";
    private GoogleSignInClient googleSignInClient;
    private SignInButton goSignInButton;
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button button = null;
    private View info = null;
    Button btnTwitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        goSignInButton = findViewById(R.id.sign_in_button);
        btnFB = findViewById(R.id.btn_fb);
        tvDaftar = findViewById(R.id.textView_daftar);
        btnLogin = findViewById(R.id.btn_login);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnTwitter = findViewById(R.id.btn_twitter);


        loginButton = findViewById(R.id.btn_fb);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        button = findViewById(R.id.btn_instagram);
        info = findViewById(R.id.info);
        appPreferences = new AppPreferences(this);

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwitterHelper.class);
                startActivity(intent);
            }
        });
        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }


//        viewGroupOfSomeSort.addView(telegramButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

         callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        cekLoginFb = true;
                        AccessToken  accessToken  =  loginResult.getAccessToken();
                        Log.d(TAG, accessToken.getToken());
                        useLoginInformation(accessToken);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        goSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }


        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String pass = edtPassword.getText().toString().trim();
                userLogin(username, pass);
                Log.d(TAG, "kliked" + username);
            }
        });
    }
    public void userLogin(String username, String pass)
    {
        EndPoint api = RetrofitClient.getApiData().create(EndPoint.class);
        Call<User> call = api.user_login(username, pass);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
//                    ArrayList<User> users = response.body().getUser();
                    String status = response.body().getStatus();
                    if (status.equalsIgnoreCase("Login Sucessfully")){
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        intent.putExtra("AKUN", edtUsername.getText().toString().trim());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;



            }

                try {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                    super.onActivityResult(requestCode, resultCode, data);

                } catch (FacebookException e){
                    Log.w(TAG, "signInResult:failed code=" + e.getMessage());
                }

    }

    private void onLoggedIn(GoogleSignInAccount account) {

        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("AKUN", account.getDisplayName());

        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
        }
    }

    private void useLoginInformation(AccessToken accessToken) {
        /**
         Creating git the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    intent.putExtra("AKUN", name);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }



    public void logout() {
        button.setText("INSTAGRAM LOGIN");
        token = null;
        info.setVisibility(View.GONE);
        appPreferences.clear();
    }
    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        getUserInfoByAccessToken(token);
    }

    public void onClick(View view) {
        if(token!=null)
        {
            logout();
        }
        else {
            authenticationDialog = new AuthenticationDialog(this,  this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();
        }
    }
    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя
                       String username = jsonData.getString("username");
//                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
//                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
//                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));

                        Toast toast = Toast.makeText(getApplicationContext(),"Sukses",Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        intent.putExtra("AKUN",  username);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }




}