package com.vertacnik.inmobiliaria.ui.inmueble;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleNuevoViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> inmuebleMutable;
    private  MutableLiveData<String> tipoInmuebleSeleccionado;
    private MutableLiveData<Uri> uriFotoMutable;

    public InmuebleNuevoViewModel(@NonNull Application application) {
        super(application);

        tipoInmuebleSeleccionado = new MutableLiveData<>();

    }

    public MutableLiveData<Inmueble> getInmuebleMutable() {
        if (inmuebleMutable == null) {
            inmuebleMutable = new MutableLiveData<>();
        }
        return inmuebleMutable;
    }
    public MutableLiveData<String> getTipoInmuebleSeleccionado() {
        if (tipoInmuebleSeleccionado == null) {
            tipoInmuebleSeleccionado = new MutableLiveData<>();
        }
        return tipoInmuebleSeleccionado;
    }
    public MutableLiveData<Uri> getUriFotoMutable() {
        if (uriFotoMutable == null) {
            uriFotoMutable = new MutableLiveData<>();
        }
        return uriFotoMutable;
    }

    public void evaluarChipSeleccionado(int checkedId) {
        String tipo = "";


        if (checkedId == R.id.chipCasa) {
            tipo = "Casa";
        } else if (checkedId == R.id.chipDepartamento) {
            tipo = "Departamento";
        } else if (checkedId == R.id.chipLocal) {
            tipo = "Local";
        } else if (checkedId == R.id.chipPH) {
            tipo = "PH";
        } else if (checkedId == R.id.chipCochera) {
            tipo = "Cochera";
        }

        if (!tipo.isEmpty()) {
            tipoInmuebleSeleccionado.setValue(tipo);
        }
    }
    public void crearNuevoInmueble(String direccion, String precio, boolean comercial,
                                   boolean residencial, String ambiente, boolean disponible,
                                   String superficie, String latitud, String longitud){

        try {
            if (direccion.isEmpty() || precio.isEmpty() || ambiente.isEmpty() || superficie.isEmpty() ||
                    latitud.isEmpty() || longitud.isEmpty()) {
                Toast.makeText(getApplication(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
            }else{
                Inmueble nuevoInmueble = new Inmueble();

                nuevoInmueble.setAmbientes(Integer.parseInt(ambiente));
                nuevoInmueble.setDireccion(direccion);
                nuevoInmueble.setValor(Integer.parseInt(precio));
                nuevoInmueble.setTipo(tipoInmuebleSeleccionado.getValue());
                nuevoInmueble.setSuperficie(Integer.parseInt(superficie));
                nuevoInmueble.setLatitud(Integer.parseInt(latitud));
                nuevoInmueble.setLongitud(Integer.parseInt(longitud));

                if (comercial) {
                    nuevoInmueble.setUso("Comercial");
                } else if (residencial) {
                    nuevoInmueble.setUso("Residencial");
                }
                if (disponible) {
                    nuevoInmueble.setDisponible(true);
                } else {
                    nuevoInmueble.setDisponible(false);
                }

                byte[] foto = parseUri();

                if (foto.length==0){
                    Toast.makeText(getApplication(), "Debe ingresar una foto", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    String inmuebleJson = new Gson().toJson(nuevoInmueble);
                    RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), foto);
                    MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "foto.jpg", requestFile);
                    ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
                    String token = ApiClient.obtenerToken(getApplication());
                    Call<Inmueble> call = servicio.CargarInmueble(token, imagenPart, inmuebleBody);
                    call.enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                            if (response.isSuccessful()) {
                                inmuebleMutable.postValue(response.body());
                                Toast.makeText(getApplication(), "Inmueble creado", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplication(), "Error al crear el inmueble", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Inmueble> call, Throwable t) {
                            Toast.makeText(getApplication(), "Error del servidor.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }catch (NumberFormatException e){
                Toast.makeText(getApplication(), "Debe ingresar un numero valido", Toast.LENGTH_LONG).show();
        }
    }

    public void recibirFoto(ActivityResult resultado) {
        //Trabajamos con el resultado del intent (uri de la foto)
        if (resultado.getResultCode() == Activity.RESULT_OK){
            Intent data = resultado.getData();
            Uri uri = data.getData();
            Log.d("galeria","uri: "+uri.toString());
            uriFotoMutable.setValue(uri);

        }
    }
    public byte[] parseUri(){
        try {
            Uri uri = uriFotoMutable.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            Toast.makeText(getApplication(), "Debe ingresar una foto", Toast.LENGTH_LONG).show();
            return new byte[]{};
        }
    }
}