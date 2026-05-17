package com.vertacnik.inmobiliaria.ui.perfil.cambiar_clave_perfil;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Propietario;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilCambiarClaveViewModel extends AndroidViewModel {
    private MutableLiveData<String> mToastMessage;
    private MutableLiveData<Boolean> mClaveCambiada;

    public PerfilCambiarClaveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getToastMessage() {
        if (mToastMessage==null) {
            mToastMessage = new MutableLiveData<>();
        }
        return mToastMessage;
    }

    public LiveData<Boolean> getClaveCambiada() {
        if (mClaveCambiada==null) {
            mClaveCambiada = new MutableLiveData<>();
        }
        return mClaveCambiada;
    }

    public void cambiarClave(String actual, String nueva) {
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        String token = ApiClient.obtenerToken(getApplication());
        if(token == null) { return; }

        Call<Void> call = servicio.cambiarClave(token, actual, nueva);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("LOG_CAMBIARCLAVE", "Contraseña actualizada correctamente");
                    mToastMessage.postValue("Contraseña actualizada correctamente");
                    mClaveCambiada.postValue(true);
                } else {
                    Log.d("LOG_CAMBIARCLAVE_ERROR", "Código: " + response.code());
                    try {
                        Log.d("LOG_CAMBIARCLAVE_ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mToastMessage.postValue("El valor ingresado en los campos es incorrecto");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOG_CAMBIARCLAVE_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del CallBack en el PerfilCambiarClaveViewModel");
            }
        });
    }
}