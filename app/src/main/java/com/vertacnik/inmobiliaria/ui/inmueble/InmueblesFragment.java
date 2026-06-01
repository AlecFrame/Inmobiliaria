package com.vertacnik.inmobiliaria.ui.inmueble;

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

import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentInmueblesBinding;

public class InmueblesFragment extends Fragment {
    private FragmentInmueblesBinding binding;
    private InmueblesViewModel mViewModel;
    private InmuebleAdapter inmuebleAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);

        binding.fabAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_inmueblesFragment_to_inmuebleNuevoFragment);
            }
        });

        mViewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        mViewModel.getInmueblesMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            inmuebleAdapter = new InmuebleAdapter(inmuebles,getContext(), getLayoutInflater());

            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL,false);

            binding.rvListaInmueble.setLayoutManager(glm);
            binding.rvListaInmueble.setAdapter(inmuebleAdapter);

            mViewModel.cargarScrollInmuebleId(inmuebles, getArguments());
        });

        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvMensajeCargandoInmuebles.setText(message);
        });

        mViewModel.getMessageVisible().observe(getViewLifecycleOwner(), visible -> {
            binding.tvMensajeCargandoInmuebles.setVisibility(visible);
        });

        mViewModel.getScrollInmuebleId().observe(getViewLifecycleOwner(), id -> {
            binding.rvListaInmueble.smoothScrollToPosition(id);
            getArguments().remove("NuevoInmuebleID");
        });

        mViewModel.cargarInmuebles();
        return binding.getRoot();
    }
}