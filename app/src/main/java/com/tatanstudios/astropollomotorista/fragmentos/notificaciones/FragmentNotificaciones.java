package com.tatanstudios.astropollomotorista.fragmentos.notificaciones;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.angads25.toggle.widget.LabeledSwitch;
import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.network.ApiService;
import com.tatanstudios.astropollomotorista.network.RetrofitBuilder;
import com.tatanstudios.astropollomotorista.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentNotificaciones extends Fragment {

    RelativeLayout root;

    Button btnGuardar;

    LabeledSwitch switch1;
    ScrollView scroll;

    TextView txtMensaje;

    ProgressBar progressBar;
    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int disponible = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        txtMensaje = vista.findViewById(R.id.txtMensaje);
        scroll = vista.findViewById(R.id.scroll);
        switch1 = vista.findViewById(R.id.switch1);
        btnGuardar = vista.findViewById(R.id.btnGuardar);
        root = vista.findViewById(R.id.root);


        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        peticionServidor();

        switch1.setLabelOff(getString(R.string.no));
        switch1.setLabelOn(getString(R.string.si));

        btnGuardar.setOnClickListener(v -> peticionGuardar());


        return vista;
    }



    void peticionServidor(){

        String id = tokenManager.getToken().getId();
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.verOpcionNotificaciones(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuestas != null) {

                                        if(apiRespuestas.getSuccess() == 1) {
                                            scroll.setVisibility(View.VISIBLE);

                                            txtMensaje.setText(apiRespuestas.getMensaje());

                                            if(apiRespuestas.getOpcion() == 1){
                                                switch1.setOn(true);
                                            }else{
                                                switch1.setOn(false);
                                            }

                                        }else {
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    void peticionGuardar(){
        String id = tokenManager.getToken().getId();
        progressBar.setVisibility(View.VISIBLE);

        boolean sw1 = switch1.isOn();

        if(sw1){
            disponible = 1;
        }else{
            disponible = 0;
        }

        compositeDisposable.add(
                service.editarEstadoNotificaciones(id, disponible)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuestas != null) {

                                        if (apiRespuestas.getSuccess() == 1){
                                            Toasty.success(getActivity(),getString(R.string.actualizado)).show();
                                        }else {
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getResources().getString(R.string.sin_conexion)).show();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onPause(){
        compositeDisposable.clear();
        super.onPause();
    }

}
