package com.example.ceasepandemic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private ImageView mlogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mlogo = findViewById(R.id.logo);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user!=null){
                    Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this ,mlogo,"logo_transition");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class),options.toBundle());
                    finish();
                }

            }
        },1000);



    }
}
