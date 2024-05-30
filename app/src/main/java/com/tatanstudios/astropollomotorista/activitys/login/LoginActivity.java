package com.tatanstudios.astropollomotorista.activitys.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.fragmentos.login.FragmentLogin;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentLogin fragmentLogin = new FragmentLogin();

        Bundle bundle = new Bundle();
        fragmentLogin.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentLogin)
                .addToBackStack(null)
                .commit();
    }
}