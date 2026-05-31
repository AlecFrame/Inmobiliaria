package com.vertacnik.inmobiliaria.ui.contrato;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.modelo.Inquilino;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContratoDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();
    private MutableLiveData<Inquilino> contratoInquilinoMutable = new MutableLiveData<>();
    private MutableLiveData<Inmueble> contratoInmuebleMutable = new MutableLiveData<>();

    public ContratoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContratoMutable() {
        if (contratoMutable == null) {
            contratoMutable = new MutableLiveData<>();
        }
        return contratoMutable;
    }

    public LiveData<Inquilino> getContratoInquilinoMutable() {
        if (contratoInquilinoMutable == null) {
            contratoInquilinoMutable = new MutableLiveData<>();
        }
        return contratoInquilinoMutable;
    }

    public LiveData<Inmueble> getContratoInmuebleMutable() {
        if (contratoInmuebleMutable == null) {
            contratoInmuebleMutable = new MutableLiveData<>();
        }
        return contratoInmuebleMutable;
    }

    //Bundle de origen: ContratoAdapter utilizado por ContratosFragment
    public void cargarDetalleContrato(Bundle bundle) {
        Contrato bundleContrato = bundle.getSerializable("contrato", Contrato.class);
        if (bundleContrato!=null) {
            bundleContrato.setFechaInicio(obtenerFechaFormateada(bundleContrato.getFechaInicio()));
            bundleContrato.setFechaFinalizacion(obtenerFechaFormateada(bundleContrato.getFechaFinalizacion()));
            contratoMutable.setValue(bundleContrato);

            Inquilino inquilino = bundleContrato.getInquilino();
            if (inquilino != null) {
                contratoInquilinoMutable.setValue(inquilino);
            }
            Inmueble inmueble = bundleContrato.getInmueble();
            if (inmueble != null) {
                contratoInmuebleMutable.setValue(inmueble);
            }
        }
    }

    public String obtenerFechaFormateada(String fechaOriginal) {
        if (fechaOriginal.contains("/")) return fechaOriginal;
        LocalDate fecha = LocalDate.parse(fechaOriginal);
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
