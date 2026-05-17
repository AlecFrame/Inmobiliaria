package com.vertacnik.inmobiliaria.ui.perfil.editar_perfil;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.modelo.Propietario;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilViewModel extends AndroidViewModel {
    MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<String> mToastMessage;
    private MutableLiveData<Boolean> mPerfilActualizado;

    public EditarPerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPropietario() {
        if (mPropietario==null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    public LiveData<String> getToastMessage() {
        if (mToastMessage==null) {
            mToastMessage = new MutableLiveData<>();
        }
        return mToastMessage;
    }

    public LiveData<Boolean> getPerfilActualizado() {
        if (mPerfilActualizado==null) {
            mPerfilActualizado = new MutableLiveData<>();
        }
        return mPerfilActualizado;
    }

    public void recuperarPropietario(Bundle b) {
        Propietario p = b.getSerializable("propietario", Propietario.class);
        if (p != null) {
            mPropietario.setValue(p);
        }
    }

    public void actualizarPropietario(String dni, String nombre,
                                      String apellido, String email,
                                      String telefono) {
        Propietario p = mPropietario.getValue();

        if (dni.isBlank() || nombre.isBlank() || apellido.isBlank()
                || email.isBlank() || telefono.isBlank()) {
            mToastMessage.postValue("Todos los datos son obligatorios");
            return;
        }

        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setDni(dni);
        p.setEmail(email);
        p.setTelefono(telefono);

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        String token = ApiClient.obtenerToken(getApplication());
        if(token == null) { return; }

        Call<Propietario> call = servicio.actualizarPropietario(token, p);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("LOG_PERFIL_EDITAR", "Propietario actualizado");
                    mToastMessage.postValue("Propietario actualizado");
                    mPropietario.postValue(response.body());
                    mPerfilActualizado.postValue(true);
                }else{
                    Log.d("LOG_PERFIL_EDITAR_ERROR","Código: " + response.code());
                    try {
                        Log.d("LOG_PERFIL_EDITAR_ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mToastMessage.postValue("Error al actualizar el Propietario del EditarPerfilViewModel");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("LOG_PERFIL_EDITAR_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del CallBack en el EditarPerfilViewModel");
            }
        });
    }
}