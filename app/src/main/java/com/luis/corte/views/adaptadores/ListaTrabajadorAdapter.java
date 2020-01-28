package com.luis.corte.views.adaptadores;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;
import com.luis.corte.views.dialogForm.DialogListaPuestos;
import com.luis.corte.views.dialogForm.InterfaceDialogs;
import com.luis.corte.views.adaptadores.holders.LongClickListener;
import com.luis.corte.views.adaptadores.holders.TrabajadoresViewHolder;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Trabajadores;


/**
 * Created by josu on 3/9/2017.
 */

public class ListaTrabajadorAdapter extends ArrayAdapter<Trabajadores> implements InterfaceDialogs {

    private Context context;
    private Controlador controlador;
    private Trabajadores trabajadores;
    private TextView asistencia;
    private Class origen;
    Integer posicion1;

    public TextView consecutivo;
    public TextView trabajador;
    public TextView puesto;

    public ListaTrabajadorAdapter(Context context, Controlador controlador,TextView asistencia,Class origen) {
        super(context, R.layout.item_list_trabajadores, controlador.getListaTrabajadores());
        this.context = context;
        this.controlador = controlador;
        this.asistencia = asistencia;
        this.origen = origen;
    }



    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_list_trabajadores, parent, false);
        }

        try {
            final Trabajadores empleado = controlador.getTrabajador(position);


            TrabajadoresViewHolder holder = new TrabajadoresViewHolder(convertView, origen);

            holder.consecutivo.setText(empleado.getConsecutivo().toString());
            holder.trabajador.setText(empleado.getTrabajador());
            holder.puesto.setText(empleado.getPuestosActual().getDescripcion());

            holder.setLongClickListener(new LongClickListener() {
                @Override
                public void OnItemLongClik() {
                    trabajadores = empleado;
                    posicion1 = position;
                }
            },empleado);


        }catch (Exception e){
            System.out.println("error "+e.getCause());
        }

        return convertView;
    }



    @Override
    public void add(@Nullable Trabajadores object) {
        object.setConsecutivo(this.getCount()+1);
        Controlador.TiposError tieposError = controlador.setTrabajador(object);
        Complementos.mensajesError(controlador.getActivity(),tieposError);
    }

    @Nullable
    @Override
    public Trabajadores getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public void getSelectedItem(MenuItem item){

            switch (item.getTitle().toString()){
                case "EDITAR TRABAJADOR":
                    if(controlador.validarInicioSesion()!= Controlador.TiposError.SESION_FINALIZADA){
                        new CapturaDialogDialog(controlador, ListaTrabajadorAdapter.this,trabajadores, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
                    }else{
                        Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                    }
                    break;
                case "PUESTOS REALIZADOS":
                    DialogListaPuestos dmp = new DialogListaPuestos(controlador.getActivity());
                    dmp.setPuestos(controlador.getPuestosTrabajador(trabajadores),trabajadores,this.controlador,ListaTrabajadorAdapter.this);
                    dmp.show();
                    break;
            }

    }


    public void actualizarAdapter(){
        ListaTrabajadorAdapter.this.notifyDataSetChanged();
        asistencia.setText(this.controlador.totalAsistencia());
    }

    /////////////////////interface/////////////////////
    @Override
    public void onDialogPositiveClickCapturaPuestos(String descripcion) {

    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores,Trabajadores trabajdorAnterior) {
        //actualizar trabajador
        Controlador.TiposError tiposError = controlador.updateTrabajadores(trabajadores, trabajdorAnterior);
        if(tiposError==Controlador.TiposError.EXITOSO){
            tiposError = controlador.updateNombreTrabajadorPuesto(trabajadores);

            if(tiposError==Controlador.TiposError.EXITOSO){
                actualizarAdapter();
                tiposError = controlador.updateNombreTrabajadorProduccion(trabajadores);
            }
        }

        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas TamanioCaja) {

    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {

    }

    @Override
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos, String horaCambio) {

        Controlador.TiposError tiposError = controlador.cambiarPuesto(consecutivo, catalogoPuestos, horaCambio);

        if(tiposError==Controlador.TiposError.EXITOSO){
            ListaTrabajadorAdapter.this.notifyDataSetChanged();
            asistencia.setText("Asistencia: "+controlador.totalAsistencia());
        }else{
            Complementos.mensajesError(controlador.getActivity(),tiposError);
        }
    }

    @Override
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades) {
        //npo implementar
    }


    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto, Boolean addProduccion) {

    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {
        //no implementar.
    }

    @Override
    public void onDialogPositiveClickFinalizarJornada() {
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {

    }
}
