package com.vertacnik.inmobiliaria.ui.perfil;

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

public class PerfilViewModel extends AndroidViewModel {
    MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<String> mToastMessage;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPropietario() {
        if (mPropietario==null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    public LiveData<String> getToastMessage() {
        if (mToastMessage==null) {
            mToastMessage = new MutableLiveData<>();
        }
        return mToastMessage;
    }

    public void cargarPropietario() {
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        String token = ApiClient.obtenerToken(getApplication());
        if(token == null) { return; }

        Call<Propietario> call = servicio.getPropietario(token);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("LOG_PERFIL", "Propietario obtenido");
                    mPropietario.postValue(response.body());
                }else{
                    Log.d("LOG_PERFIL_ERROR","Código: " + response.code());
                    try {
                        Log.d("LOG_PERFIL_ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mToastMessage.postValue("Error al obtener el Propietario del PerfilViewModel");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("LOG_PERFIL_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del CallBack en el PerfilViewModel");
            }
        });
    }
}