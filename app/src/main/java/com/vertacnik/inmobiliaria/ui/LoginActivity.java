package com.vertacnik.inmobiliaria.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vertacnik.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());



        /**Actualmente login funciona como la activity
         *
         * con el que se inicia la navegacion.
         * Quizas entro momento se deba crear un metodo en el
         * viewmodel que verifique la existencia de un token si es el
         * caso esta activiy navega directamente hacia otra vista */

    }
}