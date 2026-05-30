package com.vertacnik.inmobiliaria.ui.contrato;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Contrato;

public class ContratoDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();

    public ContratoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContratoMutable() {
        if (contratoMutable == null) {
            contratoMutable = new MutableLiveData<>();
        }
        return contratoMutable;
    }

    //Bundle de origen: ContratoAdapter utilizado por ContratosFragment
    public void cargarDetalleContrato(Bundle bundle) {
        Contrato bundleContrato = bundle.getSerializable("contrato", Contrato.class);
        contratoMutable.setValue(bundleContrato);
    }
}
