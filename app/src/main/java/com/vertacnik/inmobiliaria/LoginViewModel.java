package com.vertacnik.inmobiliaria;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getMensaje() {
        if (mensaje==null) {
            mensaje = new MutableLiveData<>();
        }
        return mensaje;
    }

    public void iniciarSesion(String email, String clave) {
        if (email.isBlank() || clave.isBlank()) {
            mensaje.setValue("Complete todos los campos");
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<String> call = servicio.iniciarSesion(email, clave);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // ApiClient.guardarToken(getApplication(), response.body());
                    Log.d("INMUEBLE_LOGS", response.body()); // mostrar token
                    Intent i = new Intent(getApplication(), MainActivity.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(i);
                } else {
                    Log.d("INMUEBLE_LOGS", response.message()); // mensaje de error
                    Log.d("INMUEBLE_LOGS", response.code()+""); // muestra código del error
                    Log.d("INMUEBLE_LOGS", response.errorBody().toString()+""); // trae el conjunto
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("INMUEBLE_LOGS", t.getMessage());
                Toast.makeText(getApplication(), "Fallo del Callback", Toast.LENGTH_LONG).show();
            }
        });
    }
}
