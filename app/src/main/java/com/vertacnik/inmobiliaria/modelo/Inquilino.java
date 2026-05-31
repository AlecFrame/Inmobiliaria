package com.vertacnik.inmobiliaria.modelo;

import java.io.Serializable;

public class Inquilino implements Serializable {
    private int idInquilino;
    private String nombre, apellido, dni, telefono, email, Gnombre, Gapellido, Gdni, Gtelefono, Gemail;
    public Inquilino() {}
    public Inquilino(int idInquilino, String nombre, String apellido,String email, String dni, String telefono, String Gnombre, String Gapellido,String Gemail, String Gdni, String Gtelefono) {
        this.idInquilino = idInquilino;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.Gemail = Gemail;
        this.Gnombre = Gnombre;
        this.Gapellido = Gapellido;
        this.Gdni = Gdni;
        this.Gtelefono = Gtelefono;

    }
    public int getIdInquilino() {
        return idInquilino;
    }
    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGNombre() {
        return Gnombre;
    }
    public void setGNombre(String Gnombre) {
        this.Gnombre = Gnombre;
    }
    public String getGApellido() {
        return Gapellido;
    }
    public void setGApellido(String Gapellido) {
        this.Gapellido = Gapellido;
    }
    public String getGDni() {
        return Gdni;
    }
    public void setGDni(String Gdni) {
        this.Gdni = Gdni;
    }
    public String getGTelefono() {
        return Gtelefono;
    }
    public void setGTelefono(String Gtelefono) {
        this.Gtelefono = Gtelefono;
    }
    public String getGEmail() {
        return Gemail;
    }
    public void setGEmail(String Gemail) {
        this.Gemail = Gemail;
    }

    @Override
    public String toString() {
        return "Inquilino{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", Garante='" + telefono + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
