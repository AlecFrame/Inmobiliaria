package com.vertacnik.inmobiliaria.ui.contrato;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Pago;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> pagosMutable;
    private Context context;

    public PagosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Pago>> getPagosMutable() {
        if (pagosMutable == null) {
            pagosMutable = new MutableLiveData<>();
        }
        return pagosMutable;
    }

    public void cargarPagos(int idContrato) {
        String token = ApiClient.obtenerToken(context);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        servicio.getPagosPorContrato(token, idContrato).enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pagosMutable.postValue(response.body());
                } else {
                    //La API devuelve 404 cuando el contrato no tiene pagos: mostramos lista vacía
                    Log.d("PAGOS", "Sin pagos para el contrato " + idContrato + " (HTTP " + response.code() + ")");
                    pagosMutable.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo lista pagos: " + t.getMessage());
                pagosMutable.postValue(new ArrayList<>());
            }
        });
    }
}
