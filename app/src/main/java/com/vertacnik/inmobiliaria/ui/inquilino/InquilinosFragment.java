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

import com.vertacnik.inmobiliaria.databinding.ActivityLoginBinding;
import com.vertacnik.inmobiliaria.databinding.FragmentInquilinosBinding;
import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.R;

import java.util.List;

public class InquilinosFragment extends Fragment {
    private FragmentInquilinosBinding binding;
    private InquilinosViewModel mViewModel;
    private InquilinoAdapter inquilinoAdapter;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentInquilinosBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        mViewModel.getInquilinoMutable().observe(getViewLifecycleOwner(), inquilinos -> {
            inquilinoAdapter = new InquilinoAdapter(inquilinos,getContext(), getLayoutInflater());

            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL,false);

            binding.rvListaInq.setLayoutManager(glm);
            binding.rvListaInq.setAdapter(inquilinoAdapter);

            mViewModel.cargarScrollInquilinoId(getArguments());
        });

        mViewModel.getScrollInquilinoId().observe(getViewLifecycleOwner(), id -> {
            binding.rvListaInq.smoothScrollToPosition(id);
        });

        mViewModel.cargarInquilinos();
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_inquilinos, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        // TODO: Use the ViewModel
    }

}