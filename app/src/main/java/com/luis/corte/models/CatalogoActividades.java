package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class CatalogoActividades implements Parcelable {
    private Long Id;
    private String Descripcion;
    private CatalogoCajas tamanioCaja;

    public CatalogoActividades() {}


    public CatalogoActividades(Long id, String descripcion,CatalogoCajas catalogoCajas) {
        Id = id;
        Descripcion = descripcion;
        tamanioCaja = catalogoCajas;
    }

    public CatalogoActividades(String descripcion,CatalogoCajas tamanioCaja) {
        this.Descripcion = descripcion.toUpperCase();
        this.tamanioCaja = tamanioCaja;
    }

    public CatalogoActividades(Cursor cursor,CatalogoCajas catalogoCajas){
        Id = Long.parseLong(cursor.getString(0));
        Descripcion = cursor.getString(1).toUpperCase();
        tamanioCaja = catalogoCajas;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion.toUpperCase();
    }

    public boolean validarCamposVacios(){
        boolean r = true;
        if(this.getDescripcion().equals("") || this.getTamanioCaja() == null)
            r = false;

        return r;
    }

    public CatalogoCajas getTamanioCaja() {
        return tamanioCaja;
    }

    public void setTamanioCaja(CatalogoCajas tamanioCaja) {
        this.tamanioCaja = tamanioCaja;
    }

    @Override
    public String toString() {
        return Descripcion ;
    }

    public String toString1() {
        return "CatalogoActividades{" +
                "Id=" + Id +
                ", Descripcion='" + Descripcion + '\'' +
                ", TamañoCaja='" + tamanioCaja.getDescripcion() + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Id);
        dest.writeString(this.Descripcion);
        dest.writeParcelable(this.tamanioCaja, flags);
    }

    protected CatalogoActividades(Parcel in) {
        this.Id = (Long) in.readValue(Long.class.getClassLoader());
        this.Descripcion = in.readString();
        this.tamanioCaja = in.readParcelable(CatalogoCajas.class.getClassLoader());
    }

    public static final Creator<CatalogoActividades> CREATOR = new Creator<CatalogoActividades>() {
        @Override
        public CatalogoActividades createFromParcel(Parcel source) {
            return new CatalogoActividades(source);
        }

        @Override
        public CatalogoActividades[] newArray(int size) {
            return new CatalogoActividades[size];
        }
    };
}
