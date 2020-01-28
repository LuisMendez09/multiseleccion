package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CatalogoPuestos implements Parcelable {
    private Long id;
    private String descripcion;

    public CatalogoPuestos() {}

    public CatalogoPuestos(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }

    public CatalogoPuestos(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion.toUpperCase();
    }

    public CatalogoPuestos(Cursor cursor){
        this.id = Long.parseLong(cursor.getString(0));
        this.descripcion = cursor.getString(1).toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }


    public String toString1() {
        return "CatalogoPuestos{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.descripcion);
    }

    protected CatalogoPuestos(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.descripcion = in.readString().toUpperCase();
    }

    public boolean validarCamposVacios(){
        boolean r = true;
        Log.e("MSN",this.getDescripcion());
        if(this.getDescripcion().equals(""))
            r = false;

        return r;
    }

    public static final Parcelable.Creator<CatalogoPuestos> CREATOR = new Parcelable.Creator<CatalogoPuestos>() {
        @Override
        public CatalogoPuestos createFromParcel(Parcel source) {
            return new CatalogoPuestos(source);
        }

        @Override
        public CatalogoPuestos[] newArray(int size) {
            return new CatalogoPuestos[size];
        }
    };
}
