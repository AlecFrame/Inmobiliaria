package com.vertacnik.inmobiliaria.ui.inquilino;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<Inquilino> inquilinoMutable = new MutableLiveData<>();

    public InquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getInquilinoMutable() {
        if (inquilinoMutable == null){
            inquilinoMutable = new MutableLiveData<>();
        }
        return inquilinoMutable;
    }

    public void cargarInquilino(Bundle bundle) {
        Inquilino bundleInquilino = bundle.getSerializable("inquilino", Inquilino.class);
        inquilinoMutable.setValue(bundleInquilino);
    }

}