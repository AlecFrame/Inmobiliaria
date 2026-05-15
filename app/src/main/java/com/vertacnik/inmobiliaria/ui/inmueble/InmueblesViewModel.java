package com.vertacnik.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> inmueblesMutable;
    private Context context;
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Inmueble>> getInmueblesMutable() {
        if (inmueblesMutable == null){
            inmueblesMutable = new MutableLiveData<>();
        }
        return inmueblesMutable;
    }

    public void cargarInmuebles(){
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        String token = ApiClient.obtenerToken(context);
        Call<List<Inmueble>> call = servicio.getListaInmuebles(token);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {

                    if(response.body()!=null){
                        inmueblesMutable.postValue(response.body());
                    }else {
                        //Por ahora usamos toast porque no se como hacer para mostrar otra cosa
                        //En cada caso se deberia tomar una medida diferente como si es un 401 redirigir
                        //al login. Preguntar en clase
                        switch (response.code()){
                            case 401:
                                Log.e("API_ERROR", "No autorizado: Token inválido");
                                break;

                            case 403:

                                Log.e("API_ERROR", "Prohibido: Sin permisos suficientes");
                                break;

                            case 404:

                                Log.e("API_ERROR", "No encontrado: La lista de inmuebles no está disponible");
                                break;

                            case 500:

                                Log.e("API_ERROR", "Error del servidor: El backend falló");
                                break;

                            default:

                                Log.e("API_ERROR", "Error desconocido: Código " + response.code());
                                break;
                        }
                    }

            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(context, "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}