package com.vertacnik.inmobiliaria.ui.inicio;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vertacnik.inmobiliaria.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private InicioViewModel vm;
    private FragmentInicioBinding b;

    public static InicioFragment newInstance() {
        return new InicioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentInicioBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        return b.getRoot();
    }

}