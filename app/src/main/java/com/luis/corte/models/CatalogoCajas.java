package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class CatalogoCajas implements Parcelable {
    private Long id;
    private String descripcion;
    private Integer totalCajas;

    public CatalogoCajas(Long id, String descripcion, int totalCajas) {
        this.id = id;
        this.descripcion = descripcion;
        this.totalCajas = totalCajas;
    }

    public CatalogoCajas(String descripcion, int totalCajas) {
        this.descripcion = descripcion;
        this.totalCajas = totalCajas;
    }

    public CatalogoCajas(Cursor cursor) {
        this.id = Long.parseLong(cursor.getString(0));
        this.descripcion = cursor.getString(1);
        this.totalCajas = Integer.parseInt(cursor.getString(2));
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
        this.descripcion = descripcion;
    }

    public Integer getTotalCajas() {
        return totalCajas;
    }

    public void setTotalCajas(Integer totalCajas) {
        this.totalCajas = totalCajas;
    }

    public boolean validarCamposVacios(){
        boolean r = true;
        if(this.getDescripcion().equals("") )
            r = false;

        return r;
    }

    @Override
    public String toString() {
        return  descripcion ;
    }

    public String toString1() {
        return "CatalogoCajas{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", totalCajas=" + totalCajas +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.descripcion);
        dest.writeValue(this.totalCajas);
    }

    protected CatalogoCajas(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.descripcion = in.readString();
        this.totalCajas = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CatalogoCajas> CREATOR = new Parcelable.Creator<CatalogoCajas>() {
        @Override
        public CatalogoCajas createFromParcel(Parcel source) {
            return new CatalogoCajas(source);
        }

        @Override
        public CatalogoCajas[] newArray(int size) {
            return new CatalogoCajas[size];
        }
    };
}
