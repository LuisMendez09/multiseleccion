package com.luis.corte.models;


import org.jetbrains.annotations.NotNull;

public class ReporteCajasTotales extends  Trabajadores{
    private Integer cajasPrimera=0;
    private Integer vasquetesPrimera=0;
    private Integer cajasSegunda=0;
    private Integer vasquetesSegunda=0;
    private Integer cajasAgranel=0;
    private Integer vasquetesAgranel=0;



    public ReporteCajasTotales(Trabajadores trabajador){
      super(trabajador);
    }

    public void setCajas(Integer cajasPrimera,Integer cajasSegunda,Integer cajasAgranel){
        this.cajasPrimera = this.cajasPrimera +cajasPrimera;
        this.cajasSegunda = this.cajasSegunda+cajasSegunda;
        this.cajasAgranel = this.cajasAgranel+cajasAgranel;

    }

    public void setVasquetes(Integer vasquetesPrimera,Integer vasquetesSegunda,Integer vasquetesAgranel){
        this.vasquetesPrimera =this.vasquetesPrimera+vasquetesPrimera;
        this.vasquetesSegunda = this.vasquetesSegunda+vasquetesSegunda;
        this.vasquetesAgranel = this.vasquetesAgranel+vasquetesAgranel;
    }
    /***********************************************************/
    public Integer getCajasPrimera() {
        return cajasPrimera;
    }

    public Integer getVasquetesPrimera() {
        return vasquetesPrimera;
    }

    public Integer getCajasSegunda() {
        return cajasSegunda;
    }

    public Integer getVasquetesSegunda() {
        return vasquetesSegunda;
    }

    public Integer getCajasAgranel() {
        return cajasAgranel;
    }

    public Integer getVasquetesAgranel() {
        return vasquetesAgranel;
    }

    public void setCajasSegunda(Integer cajasSegunda) {
        this.cajasSegunda = cajasSegunda;
    }

    public void setVasquetesSegunda(Integer vasquetesSegunda) {
        this.vasquetesSegunda = vasquetesSegunda;
    }

    /***********************************************************/

    @NotNull
    @Override
    public String toString() {
        return "ReporteCajasTotales{" +
                "idTrabajador=" + getIdTrabajdor() +
                "cajasPrimera=" + cajasPrimera +
                ", vasquetesPrimera=" + vasquetesPrimera +
                ", cajasSegunda=" + cajasSegunda +
                ", vasquetesSegunda=" + vasquetesSegunda +
                ", cajasAgranel=" + cajasAgranel +
                ", vasquetesAgranel=" + vasquetesAgranel +
                '}';
    }
}
