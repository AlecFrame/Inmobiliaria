package com.vertacnik.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertacnik.inmobiliaria.modelo.Contrato;
import com.vertacnik.inmobiliaria.modelo.Inmueble;
import com.vertacnik.inmobiliaria.modelo.Inquilino;
import com.vertacnik.inmobiliaria.modelo.Pago;
import com.vertacnik.inmobiliaria.modelo.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {
    public final static String BASE_URL = "https://capacitacion.alwaysdata.net/";

    public static MiServicioInmobiliaria getServicio() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MiServicioInmobiliaria.class);
    }
    public interface MiServicioInmobiliaria {
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> iniciarSesion(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        //INMUEBLES
        @GET("/api/Inmuebles")
        Call<List<Inmueble>> getListaInmuebles(@Header("Authorization") String token);
        @GET("/api/Inmuebles/GetContratoVigente")
        Call<List<Inmueble>> getInmueblesConContratoVigente(@Header("Authorization") String token);

        //CONTRATOS Y PAGOS
        @GET("api/Contratos")
        Call<List<Contrato>> getContratos(@Header("Authorization") String token);
        @GET("api/Pagos/Contrato/{idContrato}")
        Call<List<Pago>> getPagosPorContrato(@Header("Authorization") String token, @Path("idContrato") int idContrato);

        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoPorInmueble(@Header("Authorization") String token, @Path("id") int idInmueble);
        @PUT("api/propietarios/fix-id3")
        Call<Void> restablecerUsuario3();
        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> cambiarDisponibilidad(@Header("Authorization") String token, @Body Inmueble inmueble);

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarClave(@Header("Authorization") String token,
                                @Field("currentPassword") String actual,
                                @Field("newPassword") String nueva);


        //Multipart para subir imagenes y JSON en una sola llamada a la API.
        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> CargarInmueble(@Header("Authorization") String token,
                                      @Part MultipartBody.Part imagen,
                                      @Part("inmueble") RequestBody inmuebleBody);
    }
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //Agregue el Baerer para no tener que configuralo cada vez que lo llamamos
        editor.putString("token", "Bearer "+token);
        editor.apply();
    }
    public static String obtenerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
    public static void eliminarCredenciales(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
