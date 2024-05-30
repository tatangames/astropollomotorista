package com.tatanstudios.astropollomotorista.activitys.mapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tatanstudios.astropollomotorista.R;


public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {


    String latitud = "";
    String longitud = "";

    private GoogleMap mMap;

    View mapView;

    TextView txtToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        txtToolbar = findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.mapa));

        Intent intent = getIntent();
        if (intent != null) {
            latitud = intent.getStringExtra("KEY_LATITUD");
            longitud = intent.getStringExtra("KEY_LONGITUD");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng cliente = new LatLng(Double.valueOf(latitud), Double.valueOf(longitud));
        mMap.addMarker(new MarkerOptions().position(cliente).title("Cliente"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cliente));
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
}