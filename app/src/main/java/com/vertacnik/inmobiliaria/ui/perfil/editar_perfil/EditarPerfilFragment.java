package com.vertacnik.inmobiliaria.ui.perfil.editar_perfil;

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

import com.vertacnik.inmobiliaria.MainActivity;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentPerfilEditarBinding;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel vm;
    private FragmentPerfilEditarBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(EditarPerfilViewModel.class);
        b = FragmentPerfilEditarBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getPerfilActualizado().observe(getViewLifecycleOwner(), result -> {
            MainActivity m = (MainActivity) getActivity();
            m.cargarPropietario(); // actualizar el propietario

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_editarPerfilFragment_to_perfilFragment);
        });

        vm.getPropietario().observe(getViewLifecycleOwner(), p -> {
            b.etEditarPerfilDni.getEditText().setText(p.getDni());
            b.etEditarPerfilNombre.getEditText().setText(p.getNombre());
            b.etEditarPerfilApellido.getEditText().setText(p.getApellido());
            b.etEditarPerfilEmail.getEditText().setText(p.getEmail());
            b.etEditarPerfilTelefono.getEditText().setText(p.getTelefono());
        });

        vm.recuperarPropietario(getArguments());

        b.btActualizarPerfil.setOnClickListener(v -> {
            vm.actualizarPropietario(
                b.etEditarPerfilDni.getEditText().getText().toString(),
                b.etEditarPerfilNombre.getEditText().getText().toString(),
                b.etEditarPerfilApellido.getEditText().getText().toString(),
                b.etEditarPerfilEmail.getEditText().getText().toString(),
                b.etEditarPerfilTelefono.getEditText().getText().toString()
            );
        });

        return b.getRoot();
    }
}