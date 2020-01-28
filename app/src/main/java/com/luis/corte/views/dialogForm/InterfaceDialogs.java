package com.luis.corte.views.dialogForm;


import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.models.Trabajadores;

public interface InterfaceDialogs {
    public void onDialogPositiveClickFinalizarJornada();
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion,Configuracion configuracionAnterior);
    public void onDialogPositiveClickCapturaPuestos(String descripcion);
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion,Integer cantidad);
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCaja);
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores,Trabajadores trabajadorAnterior);

    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades);
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos,String horaCambio);
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades);
    public void onDialogPositiveClickCapturarProduccion(Producto producto,Boolean addProduccion);

}
