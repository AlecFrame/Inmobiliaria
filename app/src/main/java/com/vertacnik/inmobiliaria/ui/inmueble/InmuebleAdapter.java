package com.vertacnik.inmobiliaria.ui.inmueble;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;
    private LayoutInflater layoutInflater;
    //Contadores para las imagenes. Son atributos para que no se reseteen
    // cada vez que se ejecuta el metodo que carga la tarjeta
    private int indiceCasa = 0;
    private int indiceDepto = 0;
    private int indiceLocal = 0;

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

        boolean estado = inmuebleActual.getTieneContratoVigente();
        holder.estado.setText(estado ? "ALQUILADO" : "NO ALQUILADO");
        if(!estado){
            holder.estado.setBackgroundResource(R.drawable.badge_no_disponible);
        }else {
            holder.estado.setBackgroundResource(R.drawable.badge_disponible);

        }

        String tipo = inmuebleActual.getTipo();
        holder.tipo.setText(tipo);

        //Cargamos las imagenes
        asignacionImagen(tipo,holder);

        holder.verMas.setOnClickListener(v -> {
            Bundle bundel = new Bundle();
            bundel.putSerializable("inmueble", inmuebleActual);
            Navigation.findNavController(v)
                    .navigate(R.id.action_inmueblesFragment_to_inmuebleDetalleFragment, bundel);
        });

    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }


    // Asigna imágenes en orden rotativo según el tipo de inmueble.
    // Cada contador (indiceCasa, indiceDepto, indiceLocal) solo avanza
    // cuando se procesa un item de su tipo. El operador % evita que el
    // índice se salga del rango del array, repitiendo el ciclo si hay
    // más items que imágenes disponibles.
    public void asignacionImagen(String tipo, InmuebleViewHolder holder){

        //Carga de imagenes en una pool
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

        switch (tipo.trim().toLowerCase()) {
            case "casa":
                holder.fondo.setImageResource(poolCasa[indiceCasa % poolCasa.length]);
                indiceCasa++;
                break;
            case "departamento":
                holder.fondo.setImageResource(poolDepto[indiceDepto % poolDepto.length]);
                indiceDepto++;
                break;
            case "localcomercial":
                holder.fondo.setImageResource(poolLocal[indiceLocal % poolLocal.length]);
                indiceLocal++;
                break;
            default:
                holder.fondo.setImageResource(R.drawable.fondo_01_purple_casa);
                break;
        }
    }

    //Clase interna - Representacion de la Card
    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
        //Representacion de la carta
        TextView direccion;
        TextView ambientes;
        TextView tipo;
        TextView uso;
        TextView valor;
        TextView estado;
        ImageView fondo;
        Button verMas;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            direccion = itemView.findViewById(R.id.tvDireccion);
            ambientes =  itemView.findViewById(R.id.tvAmbientes);
            tipo =  itemView.findViewById(R.id.tvTipo);
            uso =  itemView.findViewById(R.id.tvUso);
            valor =  itemView.findViewById(R.id.tvValor);
            estado = itemView.findViewById(R.id.tvEstado);
            fondo = itemView.findViewById(R.id.ivFondo);
            verMas = itemView.findViewById(R.id.btnVerMas);
        }

    }

}
