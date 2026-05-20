package com.vertacnik.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
    private MutableLiveData<String> textoDisponibilidadM = new MutableLiveData<>();

    public InmuebleDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getInmuebleMutable() {
        if (inmuebleMutable == null){
            inmuebleMutable = new MutableLiveData<>();
        }
        return inmuebleMutable;
    }

    public LiveData<String> getTextoDisponibilidad() {
        if (textoDisponibilidadM == null){
            textoDisponibilidadM = new MutableLiveData<>();
        }
        return textoDisponibilidadM;
    }

    //Bundle de origen: InmueblesAdapter utilizado por InmueblesFragment
    public void cargarDetalleInmueble(Bundle bundle) {
        Inmueble bundleInmueble = bundle.getSerializable("inmueble", Inmueble.class);
        inmuebleMutable.setValue(bundleInmueble);

        if (inmuebleMutable.getValue().isDisponible()) {
            textoDisponibilidadM.postValue("Disponible para alquilar");
        }else {
            textoDisponibilidadM.postValue(("No disponible para alquilar"));
        }
    }

    public void cambiarDisponibilidad(boolean disponible) {
        Inmueble inmueble = inmuebleMutable.getValue();
        inmueble.setDisponible(disponible);

        String token = ApiClient.obtenerToken(getApplication());
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        Call<Inmueble> call = servicio.cambiarDisponibilidad(token, inmueble);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmuebleMutable.postValue(inmueble);
                    if (inmueble.isDisponible()) {
                        textoDisponibilidadM.postValue("Disponible para alquilar");
                    }else {
                        textoDisponibilidadM.postValue(("No disponible para alquilar"));
                    }
                    Log.d("INMUEBLE_DETALLE", "Disponibilidad cambiada");
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {

            }
        });
    }

}