package com.vertacnik.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

public class InmueblesViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> inmueblesMutable;
    private List<Inmueble> listaTodos = new ArrayList<>();
    private List<Inmueble> listaVigentes = new ArrayList<>();
    private MutableLiveData<Integer> scrollInmuebleId;
    private Context context;

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Inmueble>> getInmueblesMutable() {
        if (inmueblesMutable == null) {
            inmueblesMutable = new MutableLiveData<>();
        }
        return inmueblesMutable;
    }
    public LiveData<Integer> getScrollInmuebleId() {
        if (scrollInmuebleId==null) {
            scrollInmuebleId = new MutableLiveData<>();
        }
        return scrollInmuebleId;
    }

    public void cargarInmuebles() {
        String token = ApiClient.obtenerToken(context);
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        //Contador regresivo para liberar el await el cual desprende otro hilo
        CountDownLatch latch = new CountDownLatch(2);
        Call<List<Inmueble>> callListaCompleta = servicio.getListaInmuebles(token);

        callListaCompleta.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.body() != null) {
                    listaTodos = response.body();
                } else {
                    manejarErrorHttp(response.code());
                }
                //Resta 1
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo lista inmuebles: " + t.getMessage());
                Toast.makeText(context, "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
                //Resta 1 si falla
                latch.countDown();
            }
        });
        Call<List<Inmueble>> callListaContratoVigente =servicio.getInmueblesConContratoVigente(token);
        callListaContratoVigente.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.body() != null) {
                    listaVigentes = response.body();
                } else {
                    Log.e("API_ERROR", "Error al obtener contratos vigentes: " + response.code());
                }
                //Resta 1 dejando en cero el contador
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo lista vigentes: " + t.getMessage());
                //Si falla igualemtne resta 1 dejando en cero el contador
                latch.countDown();
            }
        });

        //Nuevo hilo en paralelo al principal (vista)
        //Se ejecutará cuando el hilo de retrofit finalice su cuent regresiva
        new Thread(() -> {
            try {
                //Aca se libera el await
                latch.await();
                //Hilo de comparacion y posteo
                compararYPostear();
            } catch (InterruptedException e) {
                Log.e("API_ERROR", "Interrupción al esperar listas: " + e.getMessage());
            }
        }).start();
    }

    private void compararYPostear() {
        if (listaTodos.isEmpty()) {
            Log.e("API_ERROR", "La lista principal está vacía, nada que postear");
            return;
        }

        Set<Integer> idsVigentes = new HashSet<>();

        for (Inmueble v : listaVigentes) {
            idsVigentes.add(v.getIdInmueble());
        }

        for (Inmueble inmueble : listaTodos) {
            inmueble.setTieneContratoVigente(idsVigentes.contains(inmueble.getIdInmueble()));
        }

        inmueblesMutable.postValue(listaTodos);
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

    public void cargarScrollInmuebleId(Bundle bundle) {
        int id = -1;
        if (bundle!=null) {
            id = bundle.getInt("NuevoInmuebleID");

            if (id!=-1) {
                scrollInmuebleId.setValue(id);
            }
        }
    }

}