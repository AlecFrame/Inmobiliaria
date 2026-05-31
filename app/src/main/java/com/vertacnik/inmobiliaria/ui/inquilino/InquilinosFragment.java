package com.vertacnik.inmobiliaria.ui.inquilino;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vertacnik.inmobiliaria.databinding.ActivityLoginBinding;
import com.vertacnik.inmobiliaria.databinding.FragmentInquilinosBinding;
import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.R;

import java.util.List;

public class InquilinosFragment extends Fragment {
    private FragmentInquilinosBinding binding;
    private InquilinosViewModel mViewModel;
    private InquilinoAdapter inquilinoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentInquilinosBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        mViewModel.getInmuebles().observe(getViewLifecycleOwner(), inmuebles -> {
            inquilinoAdapter = new InquilinoAdapter(inmuebles,getContext(), getLayoutInflater());

            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL,false);

            binding.rvListaInq.setLayoutManager(glm);
            binding.rvListaInq.setAdapter(inquilinoAdapter);
        });

        mViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvMensajeCargandoInmueblesVigentes.setText(message);
        });

        mViewModel.getMessageVisible().observe(getViewLifecycleOwner(), visible -> {
            binding.tvMensajeCargandoInmueblesVigentes.setVisibility(visible);
        });

        mViewModel.cargarInmueblesVigentes();

        return binding.getRoot();
    }

}