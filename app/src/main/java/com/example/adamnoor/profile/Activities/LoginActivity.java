package com.example.adamnoor.profile.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adamnoor.profile.Fragments.RegisterFragment;
import com.example.adamnoor.profile.Utils.AlertsUtils;
import com.example.adamnoor.profile.Utils.Api;
import com.example.adamnoor.profile.Utils.sharePrefrence;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.techease.profile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnFacebook,btnLogin,btnGoogle;
    EditText etEmail,etPass;
    TextView tvRegister;
    SignInButton signInButton;
    String strEmail,strPass,userId,userName,firstName,lastName,email;
    LoginButton loginButton;
    CallbackManager callbackManager;
    int RC_SIGN_IN=1;
    GoogleSignInClient mGoogleSignInClient;
    android.support.v7.app.AlertDialog alertDialog;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) this).getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        btnFacebook=findViewById(R.id.btnFacbebook);
        btnGoogle=findViewById(R.id.btnGoogle);
        loginButton= findViewById(R.id.btnFb);
        btnLogin=findViewById(R.id.btnLogin);
        etEmail=findViewById(R.id.etEmailLogin);
        etPass=findViewById(R.id.etPasslogin);
        tvRegister=findViewById(R.id.tvRegister);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInButton.performClick();
                SignIn();
            }
        });

        //for getting profile info and check singed in user


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new RegisterFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).addToBackStack("login").commit();
            }
        });
        callbackManager = CallbackManager.Factory.create();


        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();
                callbackManager = CallbackManager.Factory.create();
                loginButton.setReadPermissions(Arrays.asList(EMAIL));
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        String accessToken = loginResult.getAccessToken().getToken();
                        Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
                        Log.i("success",loginResult.toString());


                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                            Log.d("zmaFberror",error.toString());
                    }
                });

            }
        });
    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                sharePrefrence.SetSharedPrefrence(LoginActivity.this,"name",personName);
                Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
            }
            sharePrefrence.SetSharedPrefrence(LoginActivity.this,"token","login");

            startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
            finish();
            // Signed in successfully, show authenticated UI.


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }
    private void check() {
        strEmail=etEmail.getText().toString();
        strPass=etPass.getText().toString();
        if (strEmail.equals("") && !strEmail.contains("@") && !strEmail.contains(".com"))
        {
            etEmail.setError("Please enter valid email");
        }
        else
            if (strPass.equals(""))
            {
                etPass.setError("Pleaes enter valid password");
            }
            else
            {
                if (alertDialog==null)
                {
                    alertDialog= AlertsUtils.createProgressDialog(this);
                    alertDialog.show();
                }
                apiCall();
            }
    }

    private void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaReg",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("data");
                    email=object.getString("email");
                    userId=object.getString("id");
                    firstName=object.getString("firstname");
                    lastName=object.getString("lastname");
                    sharePrefrence.SetSharedPrefrence(LoginActivity.this,"id",userId);
                    sharePrefrence.SetSharedPrefrence(LoginActivity.this,"token","login");

                    startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
                    finish();

                    Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zma error", String.valueOf(error.getCause()));

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertsUtils.showErrorDialog(LoginActivity.this, "Network Error");
                } else if (error instanceof AuthFailureError) {
                    AlertsUtils.showErrorDialog(LoginActivity.this, "Email or Password Error");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(LoginActivity.this, "Server Error");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(LoginActivity.this, "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(LoginActivity.this, "Parsing Error");
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",strEmail);
                params.put("password",strPass);

                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }
}
