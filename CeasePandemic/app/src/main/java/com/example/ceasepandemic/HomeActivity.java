package com.example.ceasepandemic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ceasepandemic.fragments.HomeFragment;
import com.example.ceasepandemic.fragments.SettingsFragment;
import com.example.ceasepandemic.fragments.WorldFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  {

    private Button mStart,mStop;
    private TextView mTextView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mStart = findViewById(R.id.start);
        mStop = findViewById(R.id.stop);
        mTextView = findViewById(R.id.textView);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent (getApplicationContext(), GPS_SERVICE.class);
                startService(service);
            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent (getApplicationContext(),GPS_SERVICE.class);
                stopService(service);
            }
        });
        callPermissions();

        bottomNavigationView = findViewById(R.id.bottomNav);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,new HomeFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId()){
                    case R.id.news:
                        fragment = new HomeFragment();
                        break;
                    case R.id.world:
                        fragment = new WorldFragment();
                        break;
                    case R.id.settings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fragment).commit();
                return false;
            }
        });

    }

    public void callPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        String rationale = "Please provide location permission";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location Permission")
                .setSettingsDialogTitle("Warning");

        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                //requestLocationUpdates();
                callLocationService();
                mTextView.setText("Service Started");
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                callPermissions();
            }
        });
    }
    private void callLocationService() {
        Intent locationService = new Intent (getApplicationContext(),GPS_SERVICE.class);
        startService(locationService);
    }
}
