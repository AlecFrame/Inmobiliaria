package com.vertacnik.inmobiliaria.ui.contrato;

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

import com.vertacnik.inmobiliaria.databinding.FragmentContratosBinding;
import com.vertacnik.inmobiliaria.modelo.Contrato;

import java.util.List;

public class ContratosFragment extends Fragment {

    private FragmentContratosBinding binding;
    private ContratosViewModel mViewModel;
    private ContratoAdapter contratoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        mViewModel.getContratosMutable().observe(getViewLifecycleOwner(), new Observer<List<Contrato>>() {
            @Override
            public void onChanged(List<Contrato> contratos) {
                contratoAdapter = new ContratoAdapter(contratos, getContext(), getLayoutInflater());

                GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

                binding.rvListaContrato.setLayoutManager(glm);
                binding.rvListaContrato.setAdapter(contratoAdapter);
            }
        });

        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvMensajeCargandoContratos.setText(message);
        });

        mViewModel.getMessageVisible().observe(getViewLifecycleOwner(), visible -> {
            binding.tvMensajeCargandoContratos.setVisibility(visible);
        });

        mViewModel.cargarContratos();
        return binding.getRoot();
    }
}
