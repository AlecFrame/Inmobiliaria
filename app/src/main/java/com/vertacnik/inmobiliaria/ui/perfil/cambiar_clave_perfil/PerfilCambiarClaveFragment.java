package com.vertacnik.inmobiliaria.ui.perfil.cambiar_clave_perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentPerfilCambiarClaveBinding;

public class PerfilCambiarClaveFragment extends Fragment {

    private PerfilCambiarClaveViewModel vm;
    private FragmentPerfilCambiarClaveBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PerfilCambiarClaveViewModel.class);
        b = FragmentPerfilCambiarClaveBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        b.btClavePerfilCambiar.setOnClickListener(v -> {
            vm.cambiarClave(
                    b.etClavePerfilActual.getEditText().getText().toString(),
                    b.etClavePerfilNueva.getEditText().getText().toString()
            );
        });

        return b.getRoot();
    }

}