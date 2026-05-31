package com.vertacnik.inmobiliaria.ui.inquilino;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {
    private MutableLiveData<Contrato> contratoM = new MutableLiveData<>();
    private MutableLiveData<String> mToastMessage = new MutableLiveData<>();

    public InquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContrato() {
        return contratoM;
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public void cargarInquilino(Bundle bundle) {
        Inmueble inmueble = bundle.getSerializable("inmueble", Inmueble.class);

        if (inmueble==null) {
            mToastMessage.postValue("Inmueble no cargado");
            return;
        }

        String token = ApiClient.obtenerToken(getApplication());
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        Call<Contrato> call = servicio.getContratoPorInmueble(token, inmueble.getIdInmueble());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful()) {
                    contratoM.postValue(response.body());
                } else {
                    manejarErrorHttp(response.code());
                }
            }
            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Log.e("API_ERROR", "Fallo lista inmuebles: " + t.getMessage());
                mToastMessage.postValue("Sin conexión con el servidor");
            }
        });
    }

    private void manejarErrorHttp(int codigo) {
        switch (codigo) {
            case 401: Log.e("API_ERROR", "No autorizado: Token inválido"); break;
            case 403: Log.e("API_ERROR", "Prohibido: Sin permisos suficientes"); break;
            case 404: Log.e("API_ERROR", "No encontrado"); break;
            case 500: Log.e("API_ERROR", "Error del servidor"); break;
            default:  Log.e("API_ERROR", "Error desconocido: Código " + codigo); break;
        }
    }
}