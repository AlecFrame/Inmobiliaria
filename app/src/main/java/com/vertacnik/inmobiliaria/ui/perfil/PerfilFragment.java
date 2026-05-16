package com.vertacnik.inmobiliaria.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vertacnik.inmobiliaria.MainViewModel;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentPerfilBinding;

import java.util.Objects;

public class PerfilFragment extends Fragment {

    private PerfilViewModel vm;
    private FragmentPerfilBinding b;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentPerfilBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);
        b.btPerfilEditar.setEnabled(false);
        Bundle bundle = new Bundle();

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getPropietario().observe(getViewLifecycleOwner(), p -> {
            b.tvPerfilUserImage.setText(p.getNombre().charAt(0)+""+p.getApellido().charAt(0));
            b.tvPerfilTitulo.setText(p.getNombre()+" "+p.getApellido());

            b.perfilTvDni.setText(p.getDni());
            b.perfilTvNombre.setText(p.getNombre());
            b.perfilTvApellido.setText(p.getApellido());
            b.perfilTvEmail.setText(p.getEmail());
            b.perfilTvTelefono.setText(p.getTelefono());
            bundle.putSerializable("propietario", p);
            b.btPerfilEditar.setEnabled(true);
        });

        vm.cargarPropietario();

        b.btPerfilEditar.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_perfilFragment_to_editarPerfilFragment, bundle);
        });
        b.btPerfilCambiarClave.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_perfilFragment_to_perfilCambiarClaveFragment);
        });

        return b.getRoot();
    }

}