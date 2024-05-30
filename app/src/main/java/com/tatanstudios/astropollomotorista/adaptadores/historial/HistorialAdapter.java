package com.tatanstudios.astropollomotorista.adaptadores.historial;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tatanstudios.astropollomotorista.R;
import com.tatanstudios.astropollomotorista.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollomotorista.fragmentos.historial.FragmentListaHistorial;
import com.tatanstudios.astropollomotorista.modelos.ordenes.ModeloOrdenesList;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.MyViewHolder> {

    // lista de pagos al servicio

    Context context;
    List<ModeloOrdenesList> modeloTipo;
    FragmentListaHistorial fTipoServicio;

    public HistorialAdapter(Context context, List<ModeloOrdenesList> modeloTipo, FragmentListaHistorial fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_historial, parent, false);
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

        holder.txtEstado.setText(modeloTipo.get(position).getEstado());

        if(modeloTipo.get(position).getEstadoCancelada() == 1){
            holder.txtEstado.setTextColor(Color.parseColor("#C8FF0000"));
        }
        else{
            holder.txtEstado.setTextColor(Color.parseColor("#000000"));
        }

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

        if(modeloTipo.get(position).getNotaOrden() != null) {
            holder.txtNotaCliente.setText(modeloTipo.get(position).getNotaOrden());

            holder.txtNotaCliente.setVisibility(View.VISIBLE);
        }else{
            holder.txtNotaCliente.setText("");

            holder.textoNota.setVisibility(View.GONE);
            holder.txtNotaCliente.setVisibility(View.GONE);
        }


        if(modeloTipo.get(position).getReferencia() != null) {
            holder.txtReferencia.setText(modeloTipo.get(position).getReferencia());
        }else{
            holder.txtReferencia.setText("");
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

        TextView txtReferencia;

        TextView txtCupon;

        TextView txtCliente;

        TextView txtDireccion;

        TextView txtNotaCliente;

        TextView textoNota;

        TextView txtEstado;


        TextView textoPremio;
        TextView txtPremio;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }


        public MyViewHolder(View itemView){
            super(itemView);

            txtFecha = itemView.findViewById(R.id.txtDireccion);
            txtOrdenNum = itemView.findViewById(R.id.txtOrdenNum);
            txtVenta = itemView.findViewById(R.id.txtEntrega);
            textoCupon = itemView.findViewById(R.id.txtS4);
            txtReferencia = itemView.findViewById(R.id.txtCupon2);
            txtCupon = itemView.findViewById(R.id.txtEntrega3);
            txtCliente = itemView.findViewById(R.id.txtTotal);
            txtDireccion = itemView.findViewById(R.id.txtCupon);
            txtNotaCliente = itemView.findViewById(R.id.txtNota);
            textoNota = itemView.findViewById(R.id.txtt4);
            txtEstado = itemView.findViewById(R.id.txtDireccion2);

            textoPremio = itemView.findViewById(R.id.txtS10);
            txtPremio = itemView.findViewById(R.id.txtEntrega8);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
