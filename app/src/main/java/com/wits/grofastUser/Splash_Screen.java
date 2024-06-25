package com.wits.grofastUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.wits.grofastUser.MainHomePage.HomePage;
import com.wits.grofastUser.session.UserActivitySession;

public class Splash_Screen extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        UserActivitySession session = new UserActivitySession(getApplicationContext());
        Intent in;
        if (session.isUserLoggedIn()) {
            in = new Intent(getApplicationContext(), HomePage.class);
        } else {
            in = new Intent(getApplicationContext(), MainActivity.class);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }, SPLASH_SCREEN);
    }
}