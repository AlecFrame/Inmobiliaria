package com.vertacnik.inmobiliaria.ui.inicio;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private InicioViewModel vm;
    private FragmentInicioBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentInicioBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        vm.getMapActual().observe(getViewLifecycleOwner(), mapaActual -> {
            ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(mapaActual);
        });

        vm.cargarMapa();

        return b.getRoot();
    }

}