package com.luis.corte.views.dialogForm;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Puestos;
import com.luis.corte.views.adaptadores.ListaPuestosAdaptar;
import com.luis.corte.views.adaptadores.ListaTrabajadorAdapter;

import java.text.ParseException;
import java.util.ArrayList;

public class DialogModificacionPuestos extends AlertDialog {
    private Controlador controlador;
    private Puestos puestos;
    private TextView lb_nombre;
    private TextView lb_consecutivo;
    private Spinner sp_puesto;
    private TextView tv_horaInicio;
    private TextView tv_horaFinal;
    private Button btn_aceptar;
    private Button btn_cancelar;
    private TimePickerDialog pickerDialog;
    private ListaPuestosAdaptar adaptar;
    private ListaTrabajadorAdapter adaptarTrabajadores;

    protected DialogModificacionPuestos(Context context) {
        super(context);
    }

    public void setControlador(Controlador controlador, Puestos puestos, ListaPuestosAdaptar adaptar, ListaTrabajadorAdapter listaTrabajadorAdapter){
        this.controlador = controlador;
        this.puestos = puestos;
        this.adaptar = adaptar;
        this.adaptarTrabajadores = listaTrabajadorAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_editar_puestos);
        lb_nombre = (TextView) findViewById(R.id.lb_nombreTrabajador_EditPuesto);
        lb_consecutivo = (TextView) findViewById(R.id.lb_consecutivo_EditPuesto);
        sp_puesto = (Spinner) findViewById(R.id.sp_puesto_EditPuesto);
        tv_horaInicio = (TextView) findViewById(R.id.tv_horaInicio_EditPuesto);
        tv_horaFinal = (TextView) findViewById(R.id.tv_horaFin_EditPuesto);
        btn_aceptar = (Button) findViewById(R.id.btn_aceptar_EdiEditPuesto);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_EditPuesto);

        lb_nombre.setText(this.puestos.getTrabajador());
        lb_consecutivo.setText("Consecutivo: "+this.puestos.getConsecutivo().toString());

        ArrayList<CatalogoPuestos> categoriasArrayList = this.controlador.getListaPuestos();
        ArrayAdapter<CatalogoPuestos> categoriasArrayAdapter = new ArrayAdapter<CatalogoPuestos>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,categoriasArrayList);
        sp_puesto.setAdapter(categoriasArrayAdapter);

        sp_puesto.setSelection(Complementos.getIndex(sp_puesto,this.puestos.getPuestos().getDescripcion()));
        tv_horaInicio.setText(this.puestos.getHoraInicioString());
        tv_horaFinal.setText(this.puestos.getHoraFinalString());

        setButtonLisener();
    }

    private void setButtonLisener(){
        this.btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        this.btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogModificacionPuestos.this.dismiss();
            }
        });

        this.tv_horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(puestos.getId()!=null){
                String h [] = tv_horaInicio.getText().toString().split(":");
                getHoraCambio(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]),"Seleccionar hora inicial",tv_horaInicio);
            }
            }
        });

        this.tv_horaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv_horaFinal.getText().toString().equals("")){
                    String h [] = tv_horaFinal.getText().toString().split(":");
                    getHoraCambio(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]),"Seleccionar hora inicial",tv_horaFinal);
                }
            }
        });
    }

    private void guardar(){
        try {
            Puestos cambioPuestos = new Puestos(this.puestos.getId(),this.puestos.getDateInicio(),this.puestos.getDateFin(),this.puestos.getTrabajadorObjec(),this.puestos.getEnviado(),this.puestos.getPuestos());
            cambioPuestos.setPuestos((CatalogoPuestos) this.sp_puesto.getSelectedItem());

            cambioPuestos.setDateInicio(Complementos.convertirStringAlong(cambioPuestos.getFechaString(),tv_horaInicio.getText().toString()));
            cambioPuestos.setDateFin(tv_horaFinal.getText().toString().equals("")?0:Complementos.convertirStringAlong(cambioPuestos.getFechaString(),tv_horaFinal.getText().toString()));

            Controlador.TiposError respuesta = controlador.updatePuesto(cambioPuestos,this.puestos);
            Log.i("verificacionHora",respuesta.name());
            if(respuesta == Controlador.TiposError.EXITOSO){

                adaptar.updatePuesto(cambioPuestos);
                adaptarTrabajadores.actualizarAdapter();
                DialogModificacionPuestos.this.dismiss();

                Log.i("verificacionHora",cambioPuestos.toString());
            }else{

                FileLog.i(Complementos.TAG_DIALOG_MODIFICAION_PUESTO,"ERROR AL GUARDAR CAMBIO");
                Complementos.mensajesError(this.controlador.getActivity(),respuesta);

                if(respuesta == Controlador.TiposError.SIN_CAMBIOS)
                    DialogModificacionPuestos.this.dismiss();
            }

        } catch (ParseException e) {
            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.ERROR_CONVERSION_FECHA);
        }
    }

    public void getHoraCambio(int hora, int minutos, final int segundos, final int milisegundos, String titulo, final TextView textView){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(hourOfDay + ":" + minute + ":" + segundos+ ":" +milisegundos);
            }
        };

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.P){
            pickerDialog = new TimePickerDialog(this.getContext(),onTimeSetListener,hora,minutos,true);
        }else{
            pickerDialog = new TimePickerDialog(this.getContext(),android.R.style.Theme_Holo_Light_Dialog,onTimeSetListener,hora,minutos,true);
        }

        pickerDialog.setTitle(titulo);
        pickerDialog.show();

    }

}
