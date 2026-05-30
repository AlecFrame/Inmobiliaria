package com.vertacnik.inmobiliaria.ui.contrato;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.List;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ContratoViewHolder> {
    private List<Contrato> contratos;
    private Context context;
    private LayoutInflater layoutInflater;

    public ContratoAdapter(List<Contrato> contratos, Context context, LayoutInflater layoutInflater) {
        this.contratos = contratos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ContratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_contrato, parent, false);
        return new ContratoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratoViewHolder holder, int position) {
        Contrato contratoActual = contratos.get(position);

        //La consigna muestra el inmueble alquilado en cada tarjeta del contrato
        if (contratoActual.getInmueble() != null) {
            holder.direccion.setText(contratoActual.getInmueble().getDireccion());
            Glide.with(context)
                    .load(ApiClient.BASE_URL + contratoActual.getInmueble().getImagen())
                    .placeholder(R.drawable.fondo_01_purple_casa)
                    .error(R.drawable.fondo_01_purple_casa)
                    .into(holder.foto);
        }

        holder.monto.setText("$" + contratoActual.getMontoAlquiler());

        if (contratoActual.getInquilino() != null) {
            holder.inquilino.setText(contratoActual.getInquilino().getNombre() + " "
                    + contratoActual.getInquilino().getApellido());
        }

        holder.estado.setText(contratoActual.isEstado() ? "VIGENTE" : "FINALIZADO");

        holder.verMas.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("contrato", contratoActual);
            Navigation.findNavController(v)
                    .navigate(R.id.action_contratosFragment_to_contratoDetalleFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return contratos.size();
    }

    //Clase interna - Representacion de la Card
    public class ContratoViewHolder extends RecyclerView.ViewHolder {
        TextView direccion;
        TextView monto;
        TextView inquilino;
        TextView estado;
        ImageView foto;
        Button verMas;

        public ContratoViewHolder(@NonNull View itemView) {
            super(itemView);
            direccion = itemView.findViewById(R.id.tvContratoDireccion);
            monto = itemView.findViewById(R.id.tvContratoMonto);
            inquilino = itemView.findViewById(R.id.tvContratoInquilino);
            estado = itemView.findViewById(R.id.tvContratoEstado);
            foto = itemView.findViewById(R.id.ivContratoFoto);
            verMas = itemView.findViewById(R.id.btnContratoVer);
        }
    }
}
