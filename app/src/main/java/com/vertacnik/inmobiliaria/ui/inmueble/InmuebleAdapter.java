package com.vertacnik.inmobiliaria.ui.inmueble;

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

import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.modelo.Inmueble;

import java.util.List;
import java.util.Random;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;
    private LayoutInflater layoutInflater;

    public InmuebleAdapter(List<Inmueble> inmuebles, Context context, LayoutInflater layoutInflater) {
        this.inmuebles = inmuebles;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_inmueble, parent, false);
        return new InmuebleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {

        Inmueble inmuebleActual = inmuebles.get(position);
        holder.ambientes.setText(inmuebleActual.getAmbientes()+" ");
        holder.direccion.setText(inmuebleActual.getDireccion());
        holder.valor.setText("$"+inmuebleActual.getValor());

        String tipo = inmuebleActual.getTipo();
        holder.tipo.setText(tipo);

        //Carga de imagenes randoms
        int[] poolCasa = {
                R.drawable.fondo_01_purple_casa,
                R.drawable.fondo_04_green_casa,
                R.drawable.fondo_06_lime_casa,
                R.drawable.fondo_08_teal_casa
        };
        int[] poolDepto = {
                R.drawable.fondo_02_blue_departamento,
                R.drawable.fondo_07_amber_departamento,
        };
        int[] poolLocal = {
                R.drawable.fondo_03_terracotta_local,
        };
        Random random = new Random();

        switch (tipo.trim().toLowerCase()){
            case "casa":
                int imgCasa = poolCasa[random.nextInt(poolCasa.length)];
                holder.fondo.setImageResource(imgCasa);
                break;
            case "departamento":
                int imgDepto = poolDepto[random.nextInt(poolDepto.length)];
                holder.fondo.setImageResource(imgDepto);
                break;
            case "localcomercial":
                int imgLocal = poolLocal[random.nextInt(poolLocal.length)];
                holder.fondo.setImageResource(imgLocal);
                break;
            default:
                holder.fondo.setImageResource(R.drawable.fondo_01_purple_casa);
                break;
        }


        holder.verMas.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("inmueble", inmuebleActual);
            Navigation.findNavController(v)
                    .navigate(R.id.action_inmueblesFragment_to_inmuebleDetalleFragment, args);
        });

    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    //Clase interna - Representacion de la Card
    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
        //Representacion de la carta
        TextView direccion;
        TextView ambientes;
        TextView tipo;
        TextView uso;
        TextView valor;
        ImageView fondo;
        Button verMas;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            direccion = itemView.findViewById(R.id.tvDireccion);
            ambientes =  itemView.findViewById(R.id.tvAmbientes);
            tipo =  itemView.findViewById(R.id.tvTipo);
            uso =  itemView.findViewById(R.id.tvUso);
            valor =  itemView.findViewById(R.id.tvValor);
            fondo = itemView.findViewById(R.id.ivFondo);
            verMas = itemView.findViewById(R.id.btnVerMas);
        }

    }
    public interface OnVerMasClickListener {
        void onVerMasClick(Inmueble inmueble);
    }
}
