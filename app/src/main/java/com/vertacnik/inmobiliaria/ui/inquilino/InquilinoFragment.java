package com.vertacnik.inmobiliaria.ui.inquilino;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vertacnik.inmobiliaria.databinding.FragmentInquilinoBinding;
import com.vertacnik.inmobiliaria.modelo.Inquilino;

public class InquilinoFragment extends Fragment {

    private InquilinoViewModel mViewModel;
    private FragmentInquilinoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);

        mViewModel.getInquilinoMutable().observe(getViewLifecycleOwner(), inquilino -> {
            binding.V1nombre.setText(inquilino.getNombre() + " " + inquilino.getApellido());
            binding.V1dni.setText(inquilino.getDni());
            binding.V1telefono.setText(inquilino.getTelefono());
            binding.V1email.setText(inquilino.getEmail());
            binding.VGnombre.setText(inquilino.getGNombre() + " " + inquilino.getGApellido());
            binding.VGdni.setText(inquilino.getGDni());
            binding.VGtelefono.setText(inquilino.getGTelefono());
            binding.VGemail.setText(inquilino.getGEmail());
        });

        mViewModel.cargarInquilino(getArguments());

        return binding.getRoot();
    }

}