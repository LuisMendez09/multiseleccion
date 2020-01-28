package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Configuracion implements Parcelable {
    private Long id;
    private String urlApi;
    private String para;
    private String cc;
    private String asunto;
    private String mensaje;
    private Integer tiempoMas;
    private Integer tiempoMenos;

    public Configuracion() {
    }

    public Configuracion(String urlApi, String para, String cc, String asunto, String mensaje, Integer tiempoMas, Integer tiempoMenos) {
        this.urlApi = urlApi;
        this.para = para;
        this.cc = cc;
        this.asunto = asunto.toUpperCase();
        this.mensaje = mensaje.toUpperCase();
        this.tiempoMas = tiempoMas;
        this.tiempoMenos = tiempoMenos;
    }

    public Configuracion(Long id, String urlApi, String para, String cc, String asunto, String mensaje, Integer tiempoMas, Integer tiempoMenos) {
        this.id = id;
        this.urlApi = urlApi;
        this.para = para;
        this.cc = cc;
        this.asunto = asunto.toUpperCase();
        this.mensaje = mensaje.toUpperCase();
        this.tiempoMas = tiempoMas;
        this.tiempoMenos = tiempoMenos;
    }

    public Configuracion(Cursor cursor) {
        this.id = Long.parseLong(cursor.getString(0));
        this.urlApi = cursor.getString(1);
        this.para = cursor.getString(2);
        this.cc = cursor.getString(3);
        this.asunto = cursor.getString(4).toUpperCase();
        this.mensaje = cursor.getString(5).toUpperCase();
        this.tiempoMas = Integer.parseInt(cursor.getString(6));
        this.tiempoMenos = Integer.parseInt(cursor.getString(7));
    }


    public Integer getTiempoMas() {
        return tiempoMas;
    }

    public void setTiempoMas(Integer tiempoMas) {
        this.tiempoMas = tiempoMas;
    }

    public Integer getTiempoMenos() {
        return tiempoMenos;
    }

    public void setTiempoMenos(Integer tiempoMenos) {
        this.tiempoMenos = tiempoMenos;
    }


    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto.toUpperCase();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje.toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlApi() {
        return urlApi;
    }

    public void setUrlApi(String urlApi) {
        this.urlApi = urlApi;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "id=" + id +
                ", urlApi='" + urlApi + '\'' +
                ", para='" + para + '\'' +
                ", cc='" + cc + '\'' +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tiempoMas=" + tiempoMas +
                ", tiempoMenos=" + tiempoMenos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.urlApi);
        dest.writeString(this.para);
        dest.writeString(this.cc);
        dest.writeString(this.asunto);
        dest.writeString(this.mensaje);
        dest.writeValue(this.tiempoMas);
        dest.writeValue(this.tiempoMenos);
    }

    protected Configuracion(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.urlApi = in.readString();
        this.para = in.readString();
        this.cc = in.readString();
        this.asunto = in.readString().toUpperCase();
        this.mensaje = in.readString().toUpperCase();
        this.tiempoMas = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tiempoMenos = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Configuracion> CREATOR = new Parcelable.Creator<Configuracion>() {
        @Override
        public Configuracion createFromParcel(Parcel source) {
            return new Configuracion(source);
        }

        @Override
        public Configuracion[] newArray(int size) {
            return new Configuracion[size];
        }
    };
}
