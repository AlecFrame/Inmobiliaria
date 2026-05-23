package com.vertacnik.inmobiliaria.ui.inmueble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.databinding.FragmentInmuebleNuevoBinding;

public class InmuebleNuevoFragment extends Fragment {

    private InmuebleNuevoViewModel mViewModel;
    private FragmentInmuebleNuevoBinding binding;

    //Una vez recibida la respuesta se inicializa la activity
    private ActivityResultLauncher<Intent> selector;

    //Es para abrir la galeria
    private Intent intent;


    public static InmuebleNuevoFragment newInstance() {
        return new InmuebleNuevoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleNuevoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InmuebleNuevoViewModel.class);

        binding.cardFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //= a StartActivity
                selector.launch(intent);
            }
        });


        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String direccion = binding.etDireccion.getText().toString();
                String precio = binding.etPrecio.getText().toString();
                boolean comercial = binding.rbComercial.isChecked();
                boolean resindecial = binding.rbResidencial.isChecked();
                String ambiente = binding.srAmbientes.getSelectedItem().toString();
                boolean disponible = binding.cbDisponible.isChecked();

                int chipsId = binding.chipGroupTipo.getCheckedChipId();

                mViewModel.evaluarChipSeleccionado(chipsId);
                mViewModel.crearNuevoInmueble(direccion, precio, comercial, resindecial, ambiente,disponible);

            }
        });

        mViewModel.getUriFotoMutable().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivFotoInmueble.setImageURI(uri);
                binding.ivFotoIcono.setVisibility(View.GONE);
                binding.tvTocaFoto.setVisibility(View.GONE);
                binding.ivFotoInmueble.setVisibility(View.VISIBLE);
            }
        });

        abrirGaleria();
        return binding.getRoot();
    }

    //Quiero que la galeria se inicialice al tocar el boton
    private void abrirGaleria(){
        //Instanciamos el intent para abrir la galeria
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Instanciamos el selector
        selector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                                             new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) {
                        mViewModel.recibirFoto(resultado);
                        Log.d("galeria","onActivityResult"+resultado.toString());
                    }
                });
    }

}