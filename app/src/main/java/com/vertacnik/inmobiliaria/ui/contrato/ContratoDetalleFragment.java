package com.vertacnik.inmobiliaria.ui.contrato;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentContratoDetalleBinding;
import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.request.ApiClient;

public class ContratoDetalleFragment extends Fragment {

    private ContratoDetalleViewModel mViewModel;
    private FragmentContratoDetalleBinding binding;
    private int idContratoActual;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratoDetalleBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(ContratoDetalleViewModel.class);

        mViewModel.getContratoMutable().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                idContratoActual = contrato.getIdContrato();

                binding.tvDetCodigo.setText(contrato.getIdContrato() + "");
                binding.tvDetFechaInicio.setText(contrato.getFechaInicio());
                binding.tvDetFechaFin.setText(contrato.getFechaFinalizacion());
                binding.tvDetMonto.setText("$" + contrato.getMontoAlquiler());
            }
        });

        mViewModel.getContratoInquilinoMutable().observe(getViewLifecycleOwner(), inquilino -> {
            binding.tvDetInquilino.setText(inquilino.getNombre() + " " + inquilino.getApellido());
        });

        mViewModel.getContratoInmuebleMutable().observe(getViewLifecycleOwner(), inmueble -> {
            binding.tvDetInmueble.setText(inmueble.getDireccion());
            Glide.with(getContext())
                    .load(ApiClient.BASE_URL + inmueble.getImagen())
                    .placeholder(R.drawable.fondo_01_purple_casa)
                    .error(R.drawable.fondo_01_purple_casa)
                    .into(binding.ivDetFoto);
        });

        //Valor del bundle recibido a traves de getArguments
        mViewModel.cargarDetalleContrato(getArguments());

        binding.btnVerPagos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idContrato", idContratoActual);
            Navigation.findNavController(v)
                    .navigate(R.id.action_contratoDetalleFragment_to_pagosFragment, bundle);
        });

        return binding.getRoot();
    }
}
