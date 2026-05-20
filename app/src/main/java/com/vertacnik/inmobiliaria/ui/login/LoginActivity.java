package com.vertacnik.inmobiliaria.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vertacnik.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginViewModel.class);

        vm.getToastMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        b.btIniciarSesion.setOnClickListener(v -> {
            vm.iniciarSesion(
                    b.etUsuario.getEditText().getText().toString(),
                    b.etClave.getEditText().getText().toString()
            );
        });

        b.btRestablecerClave.setOnClickListener(v -> {
            vm.restablecerUsuario();
        });
    }
}