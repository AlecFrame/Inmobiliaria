package com.vertacnik.inmobiliaria.ui.inquilino;

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
import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.InquilinoViewHolder> {
    private List<Inquilino> inquilinos;
    private Context context;
    private LayoutInflater layoutInflater;

    public InquilinoAdapter(List<Inquilino> inquilinos, Context context, LayoutInflater layoutInflater) {
        this.inquilinos = inquilinos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public InquilinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_inquilino, parent, false);
        return new InquilinoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InquilinoViewHolder holder, int position) {

        Inquilino inquilinoActual = inquilinos.get(position);
        holder.nombre.setText(inquilinoActual.getNombre()+" "+inquilinoActual.getApellido());
        holder.email.setText(inquilinoActual.getEmail());
        holder.dni.setText(inquilinoActual.getDni());
        holder.telefono.setText(inquilinoActual.getTelefono());
        holder.Gnombre.setText(inquilinoActual.getGNombre()+" "+inquilinoActual.getGApellido());
        holder.Gemail.setText(inquilinoActual.getGEmail());
        holder.Gdni.setText(inquilinoActual.getGDni());
        holder.Gtelefono.setText(inquilinoActual.getGTelefono());
        holder.verMas.setOnClickListener(v -> {
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("inquilino", inquilinoActual);
            Navigation.findNavController(v)
                    .navigate(R.id.action_inquilinosFragment_to_inquilinoFragment, bundle2);
        });

    }

    @Override
    public int getItemCount() {
        return inquilinos.size();
    }

    public class InquilinoViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView apellido;
        TextView email;
        TextView telefono;
        TextView dni;
        TextView Gnombre;
        TextView Gapellido;
        TextView Gemail;
        TextView Gtelefono;
        TextView Gdni;
        Button verMas;

        public InquilinoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.V1nombre);
            //apellido =  itemView.findViewById(R.id.Vapellido);
            email =  itemView.findViewById(R.id.V1email);
            telefono =  itemView.findViewById(R.id.V1telefono);
            dni =  itemView.findViewById(R.id.V1dni);
            Gnombre = itemView.findViewById(R.id.VGnombre);
            Gemail =  itemView.findViewById(R.id.VGemail);
            Gtelefono =  itemView.findViewById(R.id.VGtelefono);
            Gdni =  itemView.findViewById(R.id.VGdni);
            verMas = itemView.findViewById(R.id.btnVerMas);
        }

    }

}

