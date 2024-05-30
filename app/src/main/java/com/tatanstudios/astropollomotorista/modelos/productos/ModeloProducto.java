package com.tatanstudios.astropollomotorista.modelos.productos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloProducto {
    @SerializedName("success")
    public Integer success;


    @SerializedName("latitud")
    public String latitud;

    @SerializedName("longitud")
    public String longitud;


    @SerializedName("productos")
    public ArrayList<ModeloProductoList> productos = null;


    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Integer getSuccess() {
        return success;
    }

    public ArrayList<ModeloProductoList> getProductos() {
        return productos;
    }

}