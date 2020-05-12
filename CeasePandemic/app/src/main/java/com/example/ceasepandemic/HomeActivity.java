package com.example.ceasepandemic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity  {

    private Button mStart,mStop;
    private TextView mTextView;
    private BroadcastReceiver broadcastReceiver;
    private DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");
    private  UserObject currUser;
    private ArrayList<UserObject> contactList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

    }

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
                Intent service = new Intent (getApplicationContext(),GPS_SERVICE.class);
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
