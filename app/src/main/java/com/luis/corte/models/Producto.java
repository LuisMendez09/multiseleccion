package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Producto extends Trabajadores implements Parcelable {
    private  Long Id;
    private Long Date;
    private CatalogoActividades actividades;
    private Integer Primera;
    private Integer Segunda;
    private Integer Agranel;
    private Integer Enviado;

    public Producto(Cursor cursor, Trabajadores trabajadores,CatalogoActividades actividad){
        super(trabajadores);
        Id = Long.parseLong(cursor.getString(0));
        Date = Long.parseLong(cursor.getString(1));
        actividades = actividad;
        Primera = Integer.parseInt(cursor.getString(8));
        Segunda = Integer.parseInt(cursor.getString(9));;
        Agranel = Integer.parseInt(cursor.getString(10));;
        Enviado = Integer.parseInt(cursor.getString(11));;
    }


    public Producto(Long id, Long date, Trabajadores trabajadores,CatalogoActividades actividad, Integer primera, Integer segunda, Integer agranel, Integer enviado) {
        super(trabajadores);
        Id = id;
        Date = date;
        actividades = actividad;
        Primera = primera;
        Segunda = segunda;
        Agranel = agranel;
        Enviado = enviado;
    }

    public Producto(Long date, Trabajadores trabajadores,CatalogoActividades actividad ,Integer primera, Integer segunda, Integer agranel) {
        super(trabajadores);
        Date = date;
        actividades = actividad;
        Primera = primera;
        Segunda = segunda;
        Agranel = agranel;
        Enviado = 0;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    public Integer getPrimera() {
        return Primera;
    }

    public void setPrimera(Integer primera) {
        Primera = primera;
    }

    public Integer getSegunda() {
        return Segunda;
    }

    public void setSegunda(Integer segunda) {
        Segunda = segunda;
    }

    public Integer getAgranel() {
        return Agranel;
    }

    public void setAgranel(Integer agranel) {
        Agranel = agranel;
    }

    public Integer getEnviado() {
        return Enviado;
    }

    public void setEnviado(Integer enviado) {
        Enviado = enviado;
    }
    public String getFechaString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(Date));
    }

    public String getHoraString() {
        return new SimpleDateFormat("HH:MM:ss").format(new Date(Date));
    }

    public CatalogoActividades getActividades() {
        return actividades;
    }

    public void setActividades(CatalogoActividades actividades) {
        this.actividades = actividades;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "Id=" + Id +
                ", Date=" + Date +
                ", actividades=" + actividades.toString() +
                ", Primera=" + Primera +
                ", Segunda=" + Segunda +
                ", Agranel=" + Agranel +
                ", Enviado=" + Enviado +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.Id);
        dest.writeValue(this.Date);
        dest.writeValue(this.Primera);
        dest.writeValue(this.Segunda);
        dest.writeValue(this.Agranel);
        dest.writeValue(this.Enviado);
    }

    protected Producto(Parcel in) {
        super(in);
        this.Id = (Long) in.readValue(Long.class.getClassLoader());
        this.Date = (Long) in.readValue(Long.class.getClassLoader());
        this.Primera = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Segunda = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Agranel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Enviado = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel source) {
            return new Producto(source);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
}
