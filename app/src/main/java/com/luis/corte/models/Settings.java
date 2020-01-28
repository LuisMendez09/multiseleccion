package com.luis.corte.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Settings implements Parcelable {
    private Long Id;
    private long FechaInicio;
    private long FechaFinal;
    private Integer FinJornada;//INDICADOR DE FIN DE JORNADA
    private Integer InicioJornada;//INDICADOR DE INICIO DE JORNADA
    private Integer EnviarInformacion;//INDICADOR DE INFORMACION PENDIENTE
    private Integer CapturaTrabaajdor;//TOTAL EMPLEADOS
    private CatalogoActividades actividades;

    public Settings() {
    }

    public Settings(long fechaInicio, long fechaFinal, Integer finJornada, Integer inicioJornada, Integer enviarInformacion, Integer capturaTrabaajdor, CatalogoActividades actividades) {
        FechaInicio = fechaInicio;
        FechaFinal = fechaFinal;
        FinJornada = finJornada;
        InicioJornada = inicioJornada;
        EnviarInformacion = enviarInformacion;
        CapturaTrabaajdor = capturaTrabaajdor;
        this.actividades = actividades;
    }

    public Settings(Long id, long fechaInicio, long fechaFinal, Integer finJornada, Integer inicioJornada, Integer enviarInformacion, Integer capturaTrabaajdor, CatalogoActividades actividades) {
        Id = id;
        FechaInicio = fechaInicio;
        FechaFinal = fechaFinal;
        FinJornada = finJornada;
        InicioJornada = inicioJornada;
        EnviarInformacion = enviarInformacion;
        CapturaTrabaajdor = capturaTrabaajdor;
        this.actividades = actividades;
    }

    public Settings (Cursor cursor,CatalogoActividades actividades){
        Id = Long.parseLong(cursor.getString(0));
        FechaInicio = Long.parseLong(cursor.getString(1));
        FechaFinal = Long.parseLong(cursor.getString(2));
        FinJornada = Integer.parseInt(cursor.getString(6));
        InicioJornada = Integer.parseInt(cursor.getString(7));
        EnviarInformacion = Integer.parseInt(cursor.getString(8));
        CapturaTrabaajdor = Integer.parseInt(cursor.getString(9));
        this.actividades = actividades;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public long getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(long fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public long getFechaFinal() {
        return FechaFinal;
    }

    public void setFechaFinal(long fechaFinal) {
        FechaFinal = fechaFinal;
    }

    public Integer getFinJornada() {
        return FinJornada;
    }

    public void setFinJornada(Integer finJornada) {
        FinJornada = finJornada;
    }

    public Integer getInicioJornada() {
        return InicioJornada;
    }

    public void setInicioJornada(Integer inicioJornada) {
        InicioJornada = inicioJornada;
    }

    public Integer getEnviarInformacion() {
        return EnviarInformacion;
    }

    public void setEnviarInformacion(Integer enviarInformacion) {
        EnviarInformacion = enviarInformacion;
    }

    public Integer getCapturaTrabaajdor() {
        return CapturaTrabaajdor;
    }

    public void setCapturaTrabaajdor(Integer capturaTrabaajdor) {
        CapturaTrabaajdor = capturaTrabaajdor;
    }

    public CatalogoActividades getActividades() {
        return actividades;
    }

    public void setActividades(CatalogoActividades actividades) {
        this.actividades = actividades;
    }

    public String getFechaString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(FechaInicio));
    }

    public String getHoraInicioString() {
        return new SimpleDateFormat("HH:MM:ss").format(new Date(FechaInicio));
    }

    public String getHoraFinalString() {
        return new SimpleDateFormat("HH:MM:ss").format(new Date(FechaFinal));
    }

    @Override
    public String toString() {
        return "Settings{" +
                "Id=" + Id +
                ", FechaInicio=" + FechaInicio +
                ", FechaFinal=" + FechaFinal +
                ", FinJornada=" + FinJornada +
                ", InicioJornada=" + InicioJornada +
                ", EnviarInformacion=" + EnviarInformacion +
                ", CapturaTrabaajdor=" + CapturaTrabaajdor +
                ", actividades=" + actividades +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Id);
        dest.writeLong(this.FechaInicio);
        dest.writeLong(this.FechaFinal);
        dest.writeValue(this.FinJornada);
        dest.writeValue(this.InicioJornada);
        dest.writeValue(this.EnviarInformacion);
        dest.writeValue(this.CapturaTrabaajdor);
        dest.writeParcelable(this.actividades, flags);
    }

    protected Settings(Parcel in) {
        this.Id = (Long) in.readValue(Long.class.getClassLoader());
        this.FechaInicio = in.readLong();
        this.FechaFinal = in.readLong();
        this.FinJornada = (Integer) in.readValue(Integer.class.getClassLoader());
        this.InicioJornada = (Integer) in.readValue(Integer.class.getClassLoader());
        this.EnviarInformacion = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CapturaTrabaajdor = (Integer) in.readValue(Integer.class.getClassLoader());
        this.actividades = in.readParcelable(CatalogoActividades.class.getClassLoader());
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };
}
