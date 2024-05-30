package com.tatanstudios.astropollomotorista.activitys.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.astropollomotorista.R;

import com.tatanstudios.astropollomotorista.fragmentos.splash.FragmentSplash;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentSplash())
                .commit();
    }

}