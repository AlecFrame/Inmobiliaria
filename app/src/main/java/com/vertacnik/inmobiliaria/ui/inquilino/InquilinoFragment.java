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

        mViewModel.getInquilinoMutable().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                binding.V1nombre.setText(inquilino.getNombre()+""+inquilino.getApellido());
                binding.V1dni.setText(inquilino.getDni());
                binding.V1email.setText(inquilino.getEmail());
                binding.V1telefono.setText(inquilino.getTelefono());
            }
        });

        mViewModel.cargarInquilino(getArguments());

        return binding.getRoot();
    }

}