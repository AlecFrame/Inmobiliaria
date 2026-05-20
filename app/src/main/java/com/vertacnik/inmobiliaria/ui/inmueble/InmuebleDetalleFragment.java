package com.vertacnik.inmobiliaria.ui.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentInmuebleDetalleBinding;
import com.vertacnik.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

public class InmuebleDetalleFragment extends Fragment {

    private InmuebleDetalleViewModel mViewModel;
    private FragmentInmuebleDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleDetalleBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        mViewModel.getInmuebleMutable().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                Glide.with(getContext())
                        .load(ApiClient.BASE_URL + inmueble.getImagen())
                        .placeholder(R.drawable.fondo_01_purple_casa)
                        .error(R.drawable.fondo_01_purple_casa)
                        .into(binding.ivFotoDetalle);
                binding.tvAmbientesDetalle.setText(inmueble.getAmbientes()+"");
                binding.tvDireccionDetalle.setText(inmueble.getDireccion());
                binding.tvTipoDetalle.setText(inmueble.getTipo());
                binding.tvUsoDetalle.setText(inmueble.getUso());
                binding.tvPrecioDetalle.setText("$"+inmueble.getValor());
                binding.tvLatitudDetalle.setText(inmueble.getLatitud()+"");
                binding.tvLongitudDetalle.setText(inmueble.getLongitud()+"");
                binding.chDisponible.setChecked(inmueble.isDisponible());
            }
        });

        mViewModel.getTextoDisponibilidad().observe(getViewLifecycleOwner(), message -> {
            binding.tvInmuebleDisponibilidad.setText(message);
        });

        //Valor del bundle recibido a traves de getArguments
        mViewModel.cargarDetalleInmueble(getArguments());

        binding.chDisponible.setOnClickListener(v -> {
            mViewModel.cambiarDisponibilidad(binding.chDisponible.isChecked());
        });

        return binding.getRoot();
    }


}