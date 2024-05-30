package com.tatanstudios.astropollomotorista.activitys.ordenes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.activitys.mapa.MapaActivity;
import com.tatanstudios.astropollomotorista.adaptadores.ordenes.ProductoNuevaOrdenAdapter;
import com.tatanstudios.astropollomotorista.network.ApiService;
import com.tatanstudios.astropollomotorista.network.RetrofitBuilder;
import com.tatanstudios.astropollomotorista.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;

public class EstadoNuevaOrdenActivity extends AppCompatActivity {


    RecyclerView recyclerProductos;

    TextView txtToolbar;

    RelativeLayout root;

    ConstraintLayout vista;


    Button btnIniciar;

    Button btnMapa;

    TextView txtNumOrden;


    int idOrden = 0;

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;

    ProductoNuevaOrdenAdapter adapter;

    String latitudCliente = "";
    String longitudCliente = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_nueva_orden);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        txtToolbar = findViewById(R.id.txtToolbar);
        root = findViewById(R.id.root);
        vista = findViewById(R.id.vista);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnMapa = findViewById(R.id.btnMapa);
        txtNumOrden = findViewById(R.id.txtnumorden);



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
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerProductos.setLayoutManager(layoutManager);
        recyclerProductos.setHasFixedSize(true);
        adapter = new ProductoNuevaOrdenAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerProductos.addItemDecoration(dividerItemDecoration);
        recyclerProductos.setAdapter(adapter);

        btnMapa.setOnClickListener(v -> verMapa());
        btnIniciar.setOnClickListener(v ->aceparOrden());

        txtNumOrden.setText("Orden #" + idOrden);


        peticionServidor();
    }

    void aceparOrden(){
        new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
                .setContentTextSize(20)
                .setTitleText(getString(R.string.seleccionar_orden))
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

        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.seleccionarOrden(idOrden, id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            // REGLAS
                                            // ORDEN YA FUE SELECCIONADA POR OTRO MOTORISTA
                                            // LA ORDEN NO DEBE ESTAR CANCELADA

                                            alertaSeleccionarOrdenError(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            // ORDEN SELECCIONADA

                                            alertaOrdenSeleccionadaCorrectamente(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
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


    void alertaSeleccionarOrdenError(String titulo, String mensaje){

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(titulo);
        pDialog.setContentText(mensaje);
        pDialog.setConfirmText(getString(R.string.aceptar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    onBackPressed();
                });
        pDialog.show();
    }



    void alertaOrdenSeleccionadaCorrectamente(String titulo, String mensaje){

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(titulo);
        pDialog.setContentText(mensaje);
        pDialog.setConfirmText(getString(R.string.aceptar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    onBackPressed();
                });
        pDialog.show();
    }


    // OBTENER ORDENES NUEVAS

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

                                            adapter = new ProductoNuevaOrdenAdapter(getApplicationContext(), apiRespuesta.getProductos(), this);
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
        Toasty.info(this, getString(R.string.sin_conexion)).show();
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


