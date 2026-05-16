package com.vertacnik.inmobiliaria.modelo;

import java.io.Serializable;

public class Propietario implements Serializable {
    private int idPropietario;
    private String nombre, apellido, dni, telefono, email;
    public Propietario() {}
    public Propietario(int idPropietario, String nombre, String apellido, String dni, String telefono, String email) {
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }
    public int getIdPropietario() {return idPropietario;}
    public void setIdPropietario(int idPropietario) {this.idPropietario = idPropietario;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public String getDni() {return dni;}
    public void setDni(String dni) {this.dni = dni;}
    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Propietario{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
