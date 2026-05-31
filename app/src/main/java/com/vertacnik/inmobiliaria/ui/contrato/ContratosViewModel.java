package com.vertacnik.inmobiliaria.ui.contrato;

import static android.view.View.INVISIBLE;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contrato>> contratosMutable;
    private MutableLiveData<String> mMessage;
    private MutableLiveData<Integer> mMessageVisible;
    private Context context;
    private List<Contrato> listaTodos = new ArrayList<>();
    private List<Inmueble> listaVigentes = new ArrayList<>();

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
    public LiveData<String> getMessage() {
        if (mMessage ==null) {
            mMessage = new MutableLiveData<>();
        }
        return mMessage;
    }
    public LiveData<Integer> getMessageVisible() {
        if (mMessageVisible ==null) {
            mMessageVisible = new MutableLiveData<>();
        }
        return mMessageVisible;
    }

    public void cargarContratos() {
        String token = ApiClient.obtenerToken(context);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        CountDownLatch latch = new CountDownLatch(2);

        servicio.getContratos(token).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaTodos = response.body();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                latch.countDown();
            }
        });

        servicio.getInmueblesConContratoVigente(token).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaVigentes = response.body();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                latch.countDown();
            }
        });

        new Thread(() -> {
            try {
                latch.await();
                compararYPostear();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void compararYPostear() {
        if (listaTodos.isEmpty()) {
            Log.e("API_ERROR", "La lista principal está vacía, nada que postear");
            mMessage.postValue("No se encontraron los contratos");
            return;
        }

        Set<Integer> idsVigentes = new HashSet<>();

        for (Inmueble i : listaVigentes) {
            idsVigentes.add(i.getIdInmueble());
        }

        for (Contrato contrato : listaTodos) {
            contrato.setEstado(
                    idsVigentes.contains(contrato.getIdInmueble())
            );
        }

        contratosMutable.postValue(listaTodos);
        mMessageVisible.postValue(INVISIBLE);
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
