package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.luis.corte.complementos.FileLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Puestos extends  Trabajadores implements Parcelable {
    private Long Id;
    private CatalogoPuestos puestos;
    private Long DateInicio;
    private Long  DateFin;
    private Integer Enviado;

    public Puestos(Cursor cursor,Trabajadores trabajadores,CatalogoPuestos puestos){
        super(trabajadores);
        this.Id = Long.parseLong(cursor.getString(0));
        this.puestos = puestos;
        this.DateInicio = Long.parseLong(cursor.getString(1));
        this.DateFin = Long.parseLong(cursor.getString(4));;
        this.Enviado = Integer.parseInt(cursor.getString(10));
    }
    public Puestos(Long id, Long dateInicio, Long dateFin, Trabajadores trabajadores, Integer enviado,CatalogoPuestos puestos) {
        super(trabajadores);
        this.Id = id;
        this.DateInicio = dateInicio;
        this.DateFin = dateFin;
        this.Enviado = enviado;
        this.puestos = puestos;
    }

    public Puestos(Long dateInicio, Long dateFin, Trabajadores trabajadores, Integer enviado,CatalogoPuestos puestos) {
        super(trabajadores);
        this.DateInicio = dateInicio;
        this.DateFin = dateFin;
        this.Enviado = enviado;
        this.puestos = puestos;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getDateInicio() {
        return DateInicio;
    }

    public void setDateInicio(Long dateInicio) {
        DateInicio = dateInicio;
    }

    public Long getDateFin() {
        return DateFin;
    }

    public void setDateFin(Long dateFin) {
        DateFin = dateFin;
    }

    public Integer getEnviado() {
        return Enviado;
    }

    public void setEnviado(Integer enviado) {
        Enviado = enviado;
    }

    public CatalogoPuestos getPuestos() {
        return this.puestos;
    }

    public void setPuestos(CatalogoPuestos puestos) {
        this.puestos = puestos;
    }

    public String getFechaString() {

       return new SimpleDateFormat("dd/MM/yyyy").format(new Date(DateInicio));


    }

    public Trabajadores getTrabajadorObjec(){
        return (Trabajadores) this;
    }
    public String getHoraInicioString() {
        String h = "";

        h=   new SimpleDateFormat("HH:mm:ss:SSSS").format(new Date(DateInicio));

        FileLog.e("cambioPuesto","inicio "+new Date(DateInicio).toString()+"---"+h);
        return h;
    }

    public String getHoraFinalString() {

        String h = "";
        if(getDateFin()!=0)
            h =new SimpleDateFormat("HH:mm:ss:SSSS").format(new Date(DateFin));

        return h;
    }

    @Override
    public String toString() {
        return "Puestos{" +
                "Id=" + Id +
                ", DateInicio=" + DateInicio +
                ", DateFin=" + DateFin +
                ", puesto="+puestos.getDescripcion()+
                ", trabajadores=" + super.getTrabajador() +
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
        dest.writeValue(this.DateInicio);
        dest.writeValue(this.DateFin);
        dest.writeValue(this.Enviado);
    }

    protected Puestos(Parcel in) {
        super(in);
        this.Id = (Long) in.readValue(Long.class.getClassLoader());
        this.DateInicio = (Long) in.readValue(Long.class.getClassLoader());
        this.DateFin = (Long) in.readValue(Long.class.getClassLoader());
        this.Enviado = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Puestos> CREATOR = new Creator<Puestos>() {
        @Override
        public Puestos createFromParcel(Parcel source) {
            return new Puestos(source);
        }

        @Override
        public Puestos[] newArray(int size) {
            return new Puestos[size];
        }
    };
}
