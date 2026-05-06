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

    }
}