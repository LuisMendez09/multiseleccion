package com.luis.corte.models;

import android.database.Cursor;
import android.util.Log;

import com.luis.corte.Controlers.Controlador;

import java.util.Calendar;

public class ReporteProduccion extends Trabajadores {
    private CatalogoActividades actividad;
    private Puestos puestos;
    private Integer totalPrimera=0;
    private Integer totalSegunda=0;
    private Integer totalAgranel=0;
    private Long dateMinimo=Long.valueOf(0);
    private Long dateMaximo=Long.valueOf(0);

    public ReporteProduccion(Trabajadores trabajadores){
        super(trabajadores);
    }

    public ReporteProduccion(Cursor cursor, Trabajadores trabajadores,CatalogoActividades actividad) {
        super(trabajadores);
        this.actividad = actividad;
        this.totalPrimera = Integer.parseInt(cursor.getString(2));
        this.totalSegunda = Integer.parseInt(cursor.getString(3));
        this.totalAgranel = Integer.parseInt(cursor.getString(4));
    }

    public ReporteProduccion(Cursor cursor, Trabajadores trabajadores,CatalogoActividades actividad,Puestos puestos) {
        super(trabajadores);
        this.actividad = actividad;
        this.puestos = puestos;
        this.totalPrimera = Integer.parseInt(cursor.getString(2));
        this.totalSegunda = Integer.parseInt(cursor.getString(3));
        this.totalAgranel = Integer.parseInt(cursor.getString(4));
        this.dateMinimo = Long.parseLong(cursor.getString(5));
        this.dateMaximo = Long.parseLong(cursor.getString(6));
    }

    public Puestos getPuestos() {
        return puestos;
    }

    public void setPuestos(Puestos puestos) {
        this.puestos = puestos;
    }

    public CatalogoActividades getActividad() {
        return actividad;
    }

    public void setActividad(CatalogoActividades actividad) {
        this.actividad = actividad;
    }

    public Integer getTotalPrimera() {
        return totalPrimera;
    }

    public void setTotalPrimera(Integer totalPrimera) {
        this.totalPrimera = totalPrimera;
    }

    public Integer getTotalSegunda() {
        return totalSegunda;
    }

    public void setTotalSegunda(Integer totalSegunda) {
        this.totalSegunda = totalSegunda;
    }

    public Integer getTotalAgranel() {
        return totalAgranel;
    }

    public void setTotalAgranel(Integer totalAgranel) {
        this.totalAgranel = totalAgranel;
    }
/*******************************cajas******************************************/

    public int getCajasPrimera() {
        return (int)Controlador.getCajas(this.totalPrimera,this.actividad.getTamanioCaja().getTotalCajas());
    }

    public int getCajasSegunda() {
        return (int) Controlador.getCajas(this.totalSegunda,12);
    }

    public int getCajasAgranel() {
        return (int) Controlador.getCajas(this.totalAgranel,1);
    }

    public int getVazquetesPrimera() {

        float c = getCajasPrimera();
        float t = Controlador.getCajas(this.totalPrimera,this.actividad.getTamanioCaja().getTotalCajas());
        float r = (t-c)*(float)this.actividad.getTamanioCaja().getTotalCajas();
        return Math.round(r);
    }

    public int getVazquetesSegunda() {
        float c = getCajasSegunda();
        float t = Controlador.getCajas(this.totalSegunda,12);
        float r = (t-c)*(float)12;

        return (int)r;
    }

    public int getVazquetesAgranel() {
        float c = getCajasAgranel();
        float t = Controlador.getCajas(this.totalAgranel,1);
        float r = (t-c)*(float)1;

        return (int)r;
    }

    public Object [] getArray(){
        if(actividad==null){
            Long dif = (this.puestos.getDateFin()-this.puestos.getDateInicio());
            Float seg = (float)(dif/1000);
            Float minutos = seg/60;
            Float horas = minutos/60;

            return new Object[]{puestos.getFechaString(),getConsecutivo(),getTrabajador(),puestos.getPuestos().getDescripcion(),actividad!=null?actividad.getDescripcion():""
                    ,horas,0.0,0.0,0.0,0.0,0.0};
        }
        else{
            Float cajasPrimera = (float)this.totalPrimera/this.actividad.getTamanioCaja().getTotalCajas();
            Float cajasSegunda = (float)this.totalSegunda/12;
            Float cajasAgranel = (float)this.totalAgranel;
            Long dif = (this.dateMaximo-this.dateMinimo);
            Float seg = (float)(dif/1000);
            Float minutos = seg/60;
            Float horas = minutos/60;

            return new Object[]{puestos.getFechaString(),getConsecutivo(),getTrabajador(),puestos.getPuestos().getDescripcion(),actividad!=null?actividad.getDescripcion():""
                    ,horas,this.totalPrimera,this.totalSegunda,cajasPrimera,cajasSegunda,cajasAgranel};
        }
    }


    @Override
    public String toString() {
        return "ReporteProduccion{" +
                "trabajador="+ getTrabajador()+
                ", actividad=" + actividad +
                ", totalPrimera=" + totalPrimera +
                ", totalSegunda=" + totalSegunda +
                ", totalAgranel=" + totalAgranel +
                '}';
    }
}
