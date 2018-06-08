package com.example.adamnoor.profile.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.adamnoor.profile.Utils.sharePrefrence;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.techease.profile.R;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) this).getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        sharedPreferences = SplashActivity.this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

     //   getHasKey();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    token= sharedPreferences.getString("token","");
                    if (token.equals("login"))
                    {
                        startActivity(new Intent(SplashActivity.this,BottomNavigationActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        timer.start();
    }

    void getHasKey()
    {
        //Get Has Key
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.techease.profile", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
