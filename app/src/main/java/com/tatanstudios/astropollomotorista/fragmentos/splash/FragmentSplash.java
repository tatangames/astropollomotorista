package com.tatanstudios.astropollomotorista.fragmentos.splash;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.activitys.principal.PrincipalActivity;
import com.tatanstudios.astropollomotorista.fragmentos.login.FragmentLogin;
import com.tatanstudios.astropollomotorista.network.TokenManager;


public class FragmentSplash extends Fragment {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_splash, container, false);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        new Handler().postDelayed(() -> {

            // inicio automatico con token que iria en el SPLASH
            if (tokenManager.getToken().getId() != null) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PrincipalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {

                FragmentLogin fragmentLogin = new FragmentLogin();
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
                if(currentFragment.getClass().equals(fragmentLogin.getClass())) return;

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContenedor, fragmentLogin)
                        .addToBackStack(null)
                        .commit();

            }
        }, SPLASH_DISPLAY_LENGTH);


        return vista;

    }


}
