package com.vertacnik.inmobiliaria.ui.contrato;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vertacnik.inmobiliaria.databinding.FragmentPagosBinding;
import com.vertacnik.inmobiliaria.modelo.Pago;

import java.util.List;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private PagosViewModel mViewModel;
    private PagoAdapter pagoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPagosBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);

        mViewModel.getPagosMutable().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                pagoAdapter = new PagoAdapter(pagos, getContext(), getLayoutInflater());
                binding.rvListaPagos.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvListaPagos.setAdapter(pagoAdapter);
            }
        });

        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvMensajeCargandoPagos.setText(message);
        });

        mViewModel.getMessageVisible().observe(getViewLifecycleOwner(), visible -> {
            binding.tvMensajeCargandoPagos.setVisibility(visible);
        });

        //El idContrato llega por el Bundle desde ContratoDetalleFragment
        int idContrato = getArguments() != null ? getArguments().getInt("idContrato") : -1;
        mViewModel.cargarPagos(idContrato);

        return binding.getRoot();
    }
}
