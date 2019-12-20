package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstActivity extends AppCompatActivity {

    private SharedPreferences saved_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean login_state = saved_information.getBoolean("login_state", false);

        if (login_state)
            startActivity(new Intent(FirstActivity.this, WelcomeActivity.class));
        else
            startActivity(new Intent(FirstActivity.this, LoginActivity.class));

        finish();
    }
}
