package com.vertacnik.inmobiliaria.ui.inquilino;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inquilino>> inquilinosMutable;
    private List<Inquilino> listaTodos = new ArrayList<>();
    private MutableLiveData<Integer> scrollInquilinoId;
    private Context context;

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Inquilino>> getInquilinoMutable() {
        if (inquilinosMutable == null) {
            inquilinosMutable = new MutableLiveData<>();
        }
        return inquilinosMutable;
    }
    public LiveData<Integer> getScrollInquilinoId() {
        if (scrollInquilinoId==null) {
            scrollInquilinoId = new MutableLiveData<>();
        }
        return scrollInquilinoId;
    }

    public void cargarInquilinos() {
        String token = ApiClient.obtenerToken(context);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        CountDownLatch latch = new CountDownLatch(2);

        servicio.getListaInquilinos(token).enqueue(new Callback<List<Inquilino>>() {
            @Override
            public void onResponse(Call<List<Inquilino>> call, Response<List<Inquilino>> response) {
                if (response.body() != null) listaTodos = response.body();
                latch.countDown();
            }
            @Override
            public void onFailure(Call<List<Inquilino>> call, Throwable t) { latch.countDown(); }
        });

        servicio.getInquilinosConContrato(token).enqueue(new Callback<List<Inquilino>>() {
            @Override
            public void onResponse(Call<List<Inquilino>> call, Response<List<Inquilino>> response) {
                latch.countDown();
            }
            @Override
            public void onFailure(Call<List<Inquilino>> call, Throwable t) { latch.countDown(); }
        });

        new Thread(() -> {
            try {
                latch.await();
                inquilinosMutable.postValue(listaTodos);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();
    }

    private void compararYPostear() {
        if (listaTodos.isEmpty()) {
            Log.e("API_ERROR", "La lista principal está vacía, nada que postear");
            return;
        }

        inquilinosMutable.postValue(listaTodos);
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

    public void cargarScrollInquilinoId(Bundle bundle) {
        int id = -1;
        if (bundle!=null) {
            id = bundle.getInt("NuevoInquilinoID");

            if (id!=-1) {
                scrollInquilinoId.setValue(id);
            }
        }
    }
}