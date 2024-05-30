package com.tatanstudios.astropollomotorista.activitys.ordenes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.activitys.mapa.MapaActivity;
import com.tatanstudios.astropollomotorista.adaptadores.ordenes.ProductoEntregandoOrdenAdapter;
import com.tatanstudios.astropollomotorista.network.ApiService;
import com.tatanstudios.astropollomotorista.network.RetrofitBuilder;
import com.tatanstudios.astropollomotorista.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EstadoEntregandoOrdenActivity extends AppCompatActivity {


    RecyclerView recyclerProductos;

    TextView txtToolbar;

    RelativeLayout root;

    ConstraintLayout vista;


    Button btnFinalizar;

    Button btnMapa;

    TextView txtNumOrden;

    int idOrden = 0;

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;

    ProductoEntregandoOrdenAdapter adapter;

    String latitudCliente = "";
    String longitudCliente = "";

    ImageView imgTelefono;

    String telefonoCliente = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_entregando_orden);


        recyclerProductos = findViewById(R.id.recyclerProductos);
        txtToolbar = findViewById(R.id.txtToolbar);
        root = findViewById(R.id.root);
        vista = findViewById(R.id.vista);
        btnFinalizar = findViewById(R.id.btnIniciar);
        btnMapa = findViewById(R.id.btnMapa);
        txtNumOrden = findViewById(R.id.txtnumorden);
        imgTelefono = findViewById(R.id.imgTelefono);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        txtToolbar.setText(getString(R.string.productos));

        Intent intent = getIntent();
        if (intent != null) {
            idOrden = intent.getIntExtra("KEY_ORDEN", 0);
            telefonoCliente = intent.getStringExtra("KEY_TELEFONO");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerProductos.setLayoutManager(layoutManager);
        recyclerProductos.setHasFixedSize(true);
        adapter = new ProductoEntregandoOrdenAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerProductos.addItemDecoration(dividerItemDecoration);
        recyclerProductos.setAdapter(adapter);

        btnMapa.setOnClickListener(v -> verMapa());
        btnFinalizar.setOnClickListener(v ->finalizarOrden());

        imgTelefono.setOnClickListener(v -> hacerLlamada());

        txtNumOrden.setText("Orden #" + idOrden);

        peticionServidor();
    }


    void hacerLlamada(){
        String numero = "tel:"+telefonoCliente;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(numero)));
    }


    void finalizarOrden(){

        new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
                .setContentTextSize(20)
                .setTitleText(getString(R.string.finalizar_orden))
                .setConfirmText(getString(R.string.si))
                .setContentTextSize(16)
                .confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    peticionSeleccionarOrden();
                })
                .cancelButtonColor(R.drawable.dialogo_theme_cancel)
                .setContentTextSize(16)
                .setCancelText(getString(R.string.cancelar))
                .setCancelClickListener(kAlertDialog -> kAlertDialog.dismissWithAnimation())
                .show();
    }


    void peticionSeleccionarOrden(){

        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.finalizarEntrega(idOrden)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            // ORDEN FINALIZAADA
                                            alertaOrdenFinalizada(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
                                        }
                                        else{
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


    void verMapa(){
        if(!TextUtils.isEmpty(latitudCliente) && !TextUtils.isEmpty(longitudCliente)){

            Intent res = new Intent(this, MapaActivity.class);
            res.putExtra("KEY_LATITUD", latitudCliente);
            res.putExtra("KEY_LONGITUD", longitudCliente);
            startActivity(res);

        }else{
            Toasty.info(this, getString(R.string.sin_ubicacion)).show();
        }
    }


    void alertaOrdenFinalizada(String titulo, String mensaje){

        Toasty.info(this, titulo).show();
        onBackPressed();
    }


    void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.verListadoDeProductosOrden(idOrden)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);
                                    vista.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            latitudCliente = apiRespuesta.getLatitud();
                                            longitudCliente = apiRespuesta.getLongitud();

                                            adapter = new ProductoEntregandoOrdenAdapter(getApplicationContext(), apiRespuesta.getProductos(), this);
                                            recyclerProductos.setAdapter(adapter);

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

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.success(this, getString(R.string.sin_conexion)).show();
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}


