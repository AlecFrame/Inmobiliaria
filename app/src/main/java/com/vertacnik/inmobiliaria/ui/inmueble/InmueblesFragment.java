package com.vertacnik.inmobiliaria.ui.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.ActivityLoginBinding;
import com.vertacnik.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.vertacnik.inmobiliaria.modelo.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {
    private FragmentInmueblesBinding binding;
    private InmueblesViewModel mViewModel;
    private InmuebleAdapter inmuebleAdapter;

    public static InmueblesFragment newInstance() {
        return new InmueblesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);


        mViewModel.getInmueblesMutable().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                inmuebleAdapter = new InmuebleAdapter(inmuebles,getContext(), getLayoutInflater());

                GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL,false);

                binding.rvListaInmueble.setLayoutManager(glm);
                binding.rvListaInmueble.setAdapter(inmuebleAdapter);
            }
        });
        mViewModel.cargarInmuebles();
        return binding.getRoot();
    }



}