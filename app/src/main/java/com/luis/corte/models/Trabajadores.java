package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Trabajadores implements Parcelable {
    private Long id;
    private Integer consecutivo;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private CatalogoPuestos puestosActual;

    public Trabajadores(){}

    public  Trabajadores(Trabajadores t){
        this.id = t.id;
        this.consecutivo = t.consecutivo;
        this.nombre = t.nombre.toUpperCase();
        this.apellidoPaterno = t.apellidoPaterno.toUpperCase();
        this.apellidoMaterno = t.apellidoMaterno.toUpperCase();
        this.puestosActual = t.puestosActual;
    }

    public Trabajadores(Long id, Integer consecutivo, String nombre, String apellidoPaterno, String apellidoMaterno, CatalogoPuestos puestosActual) {
        this.id = id;
        this.consecutivo = consecutivo;
        this.nombre = nombre.toUpperCase();
        this.apellidoPaterno = apellidoPaterno.toUpperCase();
        this.apellidoMaterno = apellidoMaterno.toUpperCase();
        this.puestosActual = puestosActual;
    }

    public Trabajadores(Integer consecutivo, String nombre, String apellidoPaterno, String apellidoMaterno, CatalogoPuestos puestosActual) {
        this.consecutivo = consecutivo;
        this.nombre = nombre.toUpperCase();
        this.apellidoPaterno = apellidoPaterno.toUpperCase();
        this.apellidoMaterno = apellidoMaterno.toUpperCase();
        this.puestosActual = puestosActual;
    }

    public Trabajadores(Cursor cursor, CatalogoPuestos puestosActual){
        this.id = Long.parseLong(cursor.getString(0));
        this.consecutivo = Integer.parseInt(cursor.getString(1));
        this.nombre = cursor.getString(2).toUpperCase();
        this.apellidoPaterno = cursor.getString(3).toUpperCase();
        this.apellidoMaterno = cursor.getString(4).toUpperCase();
        this.puestosActual = puestosActual;
    }

    public Long getIdTrabajdor() {
        return id;
    }

    public void setIdTrabajdor(Long id) {
        this.id = id;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno.toUpperCase();
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno.toUpperCase();
    }

    public String getTrabajador(){
        return this.nombre+" "+this.apellidoPaterno+" "+this.apellidoMaterno;
    }

    public boolean validarCamposVacios(){
        boolean r = true;
        if(this.getNombre().equals("") ||this.getApellidoPaterno().equals("") || this.getPuestosActual().getId()==1 )
            r = false;

        return r;
    }

    public boolean validarCambios(Trabajadores trabajadores){
        boolean r = true;
        if(trabajadores.getNombre().equals(this.nombre) &&trabajadores.getApellidoPaterno().equals(this.apellidoPaterno) &&
                trabajadores.getApellidoMaterno().equals(this.apellidoMaterno) && trabajadores.getPuestosActual().getId()== this.puestosActual.getId() )
            r = false;

        return r;
    }

    @Override
    public String toString() {
        return "Trabajadores{" +
                "id=" + id +
                ", consecutivo=" + consecutivo +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", puestosActual=" + puestosActual.getDescripcion() +
                '}';
    }

    public CatalogoPuestos getPuestosActual() {
        return puestosActual;
    }

    public void setPuestosActual(CatalogoPuestos puestosActual) {
        this.puestosActual = puestosActual;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.consecutivo);
        dest.writeString(this.nombre);
        dest.writeString(this.apellidoPaterno);
        dest.writeString(this.apellidoMaterno);
        dest.writeParcelable(this.puestosActual, flags);
    }

    protected Trabajadores(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.consecutivo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.nombre = in.readString().toUpperCase();
        this.apellidoPaterno = in.readString().toUpperCase();
        this.apellidoMaterno = in.readString().toUpperCase();
        this.puestosActual = in.readParcelable(CatalogoPuestos.class.getClassLoader());
    }

    public static final Creator<Trabajadores> CREATOR = new Creator<Trabajadores>() {
        @Override
        public Trabajadores createFromParcel(Parcel source) {
            return new Trabajadores(source);
        }

        @Override
        public Trabajadores[] newArray(int size) {
            return new Trabajadores[size];
        }
    };
}
