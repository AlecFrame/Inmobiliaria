package com.vertacnik.inmobiliaria.ui.inquilino;

import android.annotation.SuppressLint;
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
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.InquilinoViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;
    private LayoutInflater layoutInflater;

    public InquilinoAdapter(List<Inmueble> inmuebles, Context context, LayoutInflater layoutInflater) {
        this.inmuebles = inmuebles;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public InquilinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_inquilino, parent, false);
        return new InquilinoViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InquilinoViewHolder holder, int position) {

        Inmueble inmueble = inmuebles.get(position);
        holder.nombre.setText(inmueble.getDireccion());
        holder.direccion.setText(inmueble.getUso()+": "+inmueble.getTipo());

        if (inmueble.getImagen()!=null && !inmueble.getImagen().isBlank()) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + inmueble.getImagen())
                    .placeholder(R.drawable.fondo_01_purple_casa)
                    .error(R.drawable.fondo_01_purple_casa)
                    .into(holder.fondo);
        }

        holder.verInquilino.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            Navigation.findNavController(v)
                    .navigate(R.id.action_inquilinosFragment_to_inquilinoFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    public class InquilinoViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView direccion;
        ImageView fondo;
        Button verInquilino;

        public InquilinoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.Vnombre);
            direccion =  itemView.findViewById(R.id.Vdireccion);
            fondo =  itemView.findViewById(R.id.Vfondo);
            verInquilino = itemView.findViewById(R.id.btnVerInquilino);
        }
    }
}

