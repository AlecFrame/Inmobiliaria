package com.vertacnik.inmobiliaria.ui.contrato;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contrato>> contratosMutable;
    private Context context;

    public ContratosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Contrato>> getContratosMutable() {
        if (contratosMutable == null) {
            contratosMutable = new MutableLiveData<>();
        }
        return contratosMutable;
    }

    public void cargarContratos() {
        String token = ApiClient.obtenerToken(context);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        servicio.getContratos(token).enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratosMutable.postValue(response.body());
                } else {
                    manejarErrorHttp(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo lista contratos: " + t.getMessage());
                Toast.makeText(context, "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
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
