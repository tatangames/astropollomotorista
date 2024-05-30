package com.tatanstudios.astropollomotorista.adaptadores.extras;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.activitys.productos.ListadoProductosActivity;
import com.tatanstudios.astropollomotorista.modelos.productos.ModeloProductoList;
import com.tatanstudios.astropollomotorista.network.RetrofitBuilder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductoOrdenCompletadaHoyAdapter extends RecyclerView.Adapter<ProductoOrdenCompletadaHoyAdapter.MyViewHolder>  {

    Context context;
    public ArrayList<ModeloProductoList> modeloTipo;
    ListadoProductosActivity listadoProductosActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public ProductoOrdenCompletadaHoyAdapter(){}

    public ProductoOrdenCompletadaHoyAdapter(Context context, ArrayList<ModeloProductoList>  modeloTipo, ListadoProductosActivity listadoProductosActivity){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.listadoProductosActivity = listadoProductosActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_productos_orden, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(modeloTipo.get(position).getUtilizaImagen() == 1){
            if(modeloTipo.get(position).getImagen() != null){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + modeloTipo.get(position).getImagen())
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }else{
                Glide.with(context)
                        .load(R.drawable.camaradefecto)
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }

        }else{
            Glide.with(context)
                    .load(R.drawable.camaradefecto)
                    .apply(opcionesGlide)
                    .into(holder.imgProducto);
        }

        // nombre completo
        holder.txtNombre.setText(modeloTipo.get(position).getNombreproducto());

        // mostrar toda la nota del producto
        if(modeloTipo.get(position).getNota() != null){
            holder.txtNota.setText(modeloTipo.get(position).getNota());
            holder.txtNota.setTextColor(Color.RED);
        }else{
            holder.txtNota.setText("");
            holder.txtNota.setTextColor(Color.BLACK);
        }

        holder.txtPrecio.setText(modeloTipo.get(position).getMultiplicado());
        holder.txtCantidad.setText(modeloTipo.get(position).getCantidad() + "x");
    }

    @Override
    public int getItemCount() {
        if(modeloTipo != null){
            return modeloTipo.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgProducto;
        TextView txtNombre;
        TextView txtCantidad;
        TextView txtPrecio;
        TextView txtNota;




        public MyViewHolder(View itemView) {
            super(itemView);

           imgProducto = itemView.findViewById(R.id.imgProducto);
           txtNombre = itemView.findViewById(R.id.txt);
           txtCantidad = itemView.findViewById(R.id.txtCantidad);
           txtPrecio = itemView.findViewById(R.id.txtPrecio);
           txtNota = itemView.findViewById(R.id.txt5);


        }


    }


}