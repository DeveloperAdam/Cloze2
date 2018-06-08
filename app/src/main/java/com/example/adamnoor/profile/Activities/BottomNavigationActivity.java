package com.example.adamnoor.profile.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.adamnoor.profile.Fragments.LoginFragment;
import com.example.adamnoor.profile.Fragments.ProfileFragment;
import com.google.android.gms.maps.MapFragment;
import com.techease.profile.R;

public class BottomNavigationActivity extends AppCompatActivity {
    Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case
                    R.id.navigation_setting:
                    setTitle("Settings");
                     fragment=new ProfileFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("anc").commit();
                    return true;
                case R.id.navigation_home:
                    setTitle("Home");
                     fragment=new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("anc").commit();
                    return true;
                case R.id.navigation_map:
                    setTitle("Map");
                    fragment=new MapFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("anc").commit();
                    return true;
                case R.id.navigation_search:
                    setTitle("Search");
                    fragment=new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("anc").commit();
                    return true;
                case R.id.navigation_promo:
                    setTitle("Promotion");
                    fragment=new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("anc").commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle("Local Bars");
    }

}
