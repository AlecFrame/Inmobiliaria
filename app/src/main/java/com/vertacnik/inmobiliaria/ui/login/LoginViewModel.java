package com.vertacnik.inmobiliaria.ui.login;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vertacnik.inmobiliaria.MainActivity;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> mToastMessage;
    private AgitarEvent sensorListener;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getToastMessage() {
        if (mToastMessage==null) {
            mToastMessage = new MutableLiveData<>();
        }
        return mToastMessage;
    }

    public void iniciarSesion(String email, String clave) {
        if (email.isBlank() || clave.isBlank()) {
            mToastMessage.postValue("Complete todos los campos");
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<String> call = servicio.iniciarSesion(email, clave);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    ApiClient.guardarToken(getApplication(), token);
                    Log.d("LOG_LOGIN", token);

                    Intent i = new Intent(getApplication(), MainActivity.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(i);
                } else {
                    Log.d("LOG_LOGIN_ERROR","Código: " + response.code());
                    try {
                        Log.d("LOG_LOGIN_ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mToastMessage.postValue("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("LOG_LOGIN_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del Callback en el LoginViewModel");
            }
        });
    }

    public void restablecerUsuario() {
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<Void> call = servicio.restablecerUsuario3();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("LOG_LOGIN", "Usuario 3 Restablecido");
                    mToastMessage.postValue("Usuario Restablecido");
                } else {
                    Log.d("LOG_LOGIN_ERROR","Código: " + response.code());
                    try {
                        Log.d("LOG_LOGIN_ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mToastMessage.postValue("Error al restablecer usuario");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOG_LOGIN_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del CallBack en el LoginViewModel");
            }
        });
    }

    public void registrarSensorAgitar() {
        SensorManager sm = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if (!list.isEmpty()) {
            Sensor accelerometer = list.get(0);
            if (sensorListener==null) {
                sensorListener = new AgitarEvent();
                sm.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    public void desRegistrarSensorAgitar() {
        SensorManager sm = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if (!list.isEmpty()) {
            sm.unregisterListener(sensorListener);
            sensorListener = null;
        }
    }

    private class AgitarEvent implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (x>13 || y>13 || z>13) {
                Toast.makeText(getApplication(), "Agitación detectada llamando a la Inmobiliaria de la ULP", Toast.LENGTH_LONG).show();
                Log.d("LoginViewModel.AgitarEvent_LOG", "Agitación detectada");

                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:2664553747"));
                call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(call);
            }
        }
    }
}
