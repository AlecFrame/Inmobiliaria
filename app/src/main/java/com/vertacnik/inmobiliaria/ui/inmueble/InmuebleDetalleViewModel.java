package com.vertacnik.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Inmueble;

public class InmuebleDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();

    public InmuebleDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getInmuebleMutable() {
        if (inmuebleMutable == null){
            inmuebleMutable = new MutableLiveData<>();
        }
        return inmuebleMutable;
    }

    //Bundle de origen: InmueblesAdapter utilizado por InmueblesFragment
    public void cargarDetalleInmueble(Bundle bundle) {
        Inmueble bundleInmueble = (Inmueble) bundle.getSerializable("inmueble");
        inmuebleMutable.setValue(bundleInmueble);
    }

}