package com.tatanstudios.astropollomotorista.adaptadores.ordenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollomotorista.fragmentos.ordenes.FragmentNuevasOrdenes;
import com.tatanstudios.astropollomotorista.modelos.ordenes.ModeloOrdenesList;

import java.util.List;


public class NuevasOrdenesAdapter extends RecyclerView.Adapter<NuevasOrdenesAdapter.MyViewHolder> {

    // carga los servicios por zona

    Context context;
    List<ModeloOrdenesList> modeloTipo;
    FragmentNuevasOrdenes fTipoServicio;

    public NuevasOrdenesAdapter(){}

    public NuevasOrdenesAdapter(Context context, List<ModeloOrdenesList> modeloTipo, FragmentNuevasOrdenes fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_nuevas_ordenes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // numero de orden
        holder.txtOrdenNum.setText(String.valueOf(modeloTipo.get(position).getId()));
        // fecha de orden
        holder.txtFecha.setText(modeloTipo.get(position).getFechaOrden());
        // venta
        holder.txtVenta.setText(modeloTipo.get(position).getTotalFormat());


        // DATOS DE CUPONES

        if(modeloTipo.get(position).getHaycupon() == 1){

            holder.txtCupon.setText(modeloTipo.get(position).getMensajeCupon());

            holder.textoCupon.setVisibility(View.VISIBLE);
            holder.txtCupon.setVisibility(View.VISIBLE);
        }else{
            holder.textoCupon.setVisibility(View.GONE);
            holder.txtCupon.setVisibility(View.GONE);
        }



        // DATOS PARA PREMIOS
        if(modeloTipo.get(position).getHayPremio() == 1){

            holder.txtPremio.setText(modeloTipo.get(position).getTextoPremio());

            holder.textoPremio.setVisibility(View.VISIBLE);
            holder.txtPremio.setVisibility(View.VISIBLE);
        }else{
            holder.textoPremio.setVisibility(View.GONE);
            holder.txtPremio.setVisibility(View.GONE);
        }



        // cliente
        holder.txtCliente.setText(modeloTipo.get(position).getCliente());
        // direccion
        holder.txtDireccion.setText(modeloTipo.get(position).getDireccion());

        if(modeloTipo.get(position).getReferencia() != null){
            holder.txtReferencia.setText(modeloTipo.get(position).getReferencia());
        }else{
            holder.txtReferencia.setText("");
        }



        holder.txtDireccion.setText(modeloTipo.get(position).getDireccion());


        if(modeloTipo.get(position).getNotaOrden() != null) {
            holder.txtNotaCliente.setText(modeloTipo.get(position).getNotaOrden());

            holder.txtNotaCliente.setVisibility(View.VISIBLE);
        }else{
            holder.txtNotaCliente.setText("");

            holder.textoNota.setVisibility(View.GONE);
            holder.txtNotaCliente.setVisibility(View.GONE);
        }

        // Redireccionamiento al fragmento correspondiente segun el servicio
        holder.setListener((view, position1) -> {
            int pos = modeloTipo.get(position).getId();
            fTipoServicio.abrirOrdenes(pos);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloTipo != null){
            return modeloTipo.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtOrdenNum;


        TextView txtFecha;


        TextView txtVenta;



        TextView textoCupon;


        TextView txtCupon;

        TextView txtCliente;

        TextView txtDireccion;


        TextView txtNotaCliente;


        TextView textoNota;


        TextView txtReferencia;


        TextView textoPremio;
        TextView txtPremio;


        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }


        public MyViewHolder(View itemView){
            super(itemView);

            txtReferencia = itemView.findViewById(R.id.txtCupon2);
            textoNota = itemView.findViewById(R.id.txtt4);
            txtNotaCliente = itemView.findViewById(R.id.txtNota);
            txtDireccion = itemView.findViewById(R.id.txtCupon);
            txtCliente = itemView.findViewById(R.id.txtTotal);
            txtCupon = itemView.findViewById(R.id.txtEntrega3);
            textoCupon = itemView.findViewById(R.id.txtS4);
            txtVenta = itemView.findViewById(R.id.txtEntrega);
            txtFecha = itemView.findViewById(R.id.txtDireccion);
            txtOrdenNum = itemView.findViewById(R.id.txtOrdenNum);

            textoPremio = itemView.findViewById(R.id.txtS5);
            txtPremio = itemView.findViewById(R.id.txtEntrega2);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}