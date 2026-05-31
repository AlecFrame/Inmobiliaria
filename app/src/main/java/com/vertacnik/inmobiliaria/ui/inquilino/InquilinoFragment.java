package com.vertacnik.inmobiliaria.ui.inquilino;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vertacnik.inmobiliaria.databinding.FragmentInquilinoBinding;
import com.vertacnik.inmobiliaria.modelo.Inquilino;

public class InquilinoFragment extends Fragment {

    private InquilinoViewModel mViewModel;
    private FragmentInquilinoBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);

        mViewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            Inquilino inquilino = contrato.getInquilino();

            binding.V1nombre.setText(inquilino.getNombre() + " " + inquilino.getApellido());
            binding.V1dni.setText(inquilino.getDni());
            binding.V1telefono.setText(inquilino.getTelefono());
            binding.V1email.setText(inquilino.getEmail());
        });

        mViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        mViewModel.cargarInquilino(getArguments());

        return binding.getRoot();
    }

}