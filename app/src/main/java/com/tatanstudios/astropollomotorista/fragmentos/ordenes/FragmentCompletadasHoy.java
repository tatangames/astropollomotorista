package com.tatanstudios.astropollomotorista.fragmentos.ordenes;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.activitys.principal.PrincipalActivity;
import com.tatanstudios.astropollomotorista.activitys.productos.ListadoProductosActivity;
import com.tatanstudios.astropollomotorista.adaptadores.extras.CompletadasHoyOrdenesAdapter;
import com.tatanstudios.astropollomotorista.network.ApiService;
import com.tatanstudios.astropollomotorista.network.RetrofitBuilder;
import com.tatanstudios.astropollomotorista.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCompletadasHoy extends Fragment {

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    CompletadasHoyOrdenesAdapter adapter;

    RecyclerView recyclerOrdenes;
    SwipeRefreshLayout refresh;
    RelativeLayout root;

    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_completadas_hoy_ordenes, container, false);


        root = vista.findViewById(R.id.root);
        refresh = vista.findViewById(R.id.refresh);
        recyclerOrdenes = vista.findViewById(R.id.recyclerOrdenes);




        ((PrincipalActivity)getActivity()).setActionBarTitle(getString(R.string.completadas_hoy));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerOrdenes.setLayoutManager(layoutManager);
        recyclerOrdenes.setHasFixedSize(true);
        adapter = new CompletadasHoyOrdenesAdapter();
        recyclerOrdenes.setAdapter(adapter);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        peticionServidor();
        refresh.setOnRefreshListener(() -> peticionServidor());

        return vista;
    }


    void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);

        refresh.setRefreshing(true);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoOrdenesCompletadasHoy(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);


                                    if(apiRespuesta != null) {


                                        if(apiRespuesta.getSuccess() == 1){

                                            if(apiRespuesta.getHayOrdenes() == 0) {
                                                Toasty.info(getActivity(), getString(R.string.no_hay_ordenes)).show();
                                            }

                                            adapter = new CompletadasHoyOrdenesAdapter(getContext(), apiRespuesta.getOrdenes(), this);
                                            recyclerOrdenes.setAdapter(adapter);

                                        }else{
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



    // ver la orden y su informacion
    public void abrirOrdenes(int ordenid){
        Intent res = new Intent(getActivity(), ListadoProductosActivity.class);
        res.putExtra("KEY_ORDEN", ordenid);
        startActivity(res);
    }


    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }

}
