package com.luis.corte.views.dialogForm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.views.MainActivity;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Trabajadores;

import java.util.ArrayList;
import java.util.Calendar;


public class CapturaDialogDialog {

    EditText et_nombre;
    EditText et_apellidoPaterno;
    EditText et_apellidoMaterno;
    Spinner sp_puesto;
    EditText et_descripcionPuestos;
    EditText et_descripcionActividad;
    public int indexActividad;
    Spinner sp_tamanioCajas;
    EditText et_descripcionTamanioCajas;
    EditText et_CantindadTamanioCajas;

    TextView tvTrabajador;
    TextView tvConsecutivo;
    TextView tvPuestoActual;
    TimePicker horaCambio;

    EditText capturaNumeroTrabajador;


    Controlador controlador;

    public static  enum tiposDialogos {
        DIALOG_ADD_PUESTOS,
        DIALOG_ADD_TRABAJADOR,
        DIALOG_ADD_ACTIVIDADES,
        DIALOG_ADD_TAMANIO_CAJAS,
        DIALOG_SELECCION_ACTIVIDAD,
        DIALOG_CAMBIO_ACTIVIDAD,
        DIALOG_CAMBIO_PUESTO,
        DIALOG_CAPTURA_NUMERO_TRABAJADOR,
        DIALOG_CAPTURA_PRODUCCION,
        DIALOG_FINALIZAR_JORNADA,
        DIALOG_CONFIGURACION,
        DIALOG_ENVIAR_CORREO,
    };

    private InterfaceDialogs DialogListener;

    public CapturaDialogDialog(Controlador controlador, final InterfaceDialogs respuestaDialogListener, Object datos, tiposDialogos tiposDialogo){
        //super(controlador.getActivity());
        iniciarliarDialog(controlador,respuestaDialogListener,datos,tiposDialogo,null);
    }

    public CapturaDialogDialog(Controlador controlador, final InterfaceDialogs respuestaDialogListener, Object datos, tiposDialogos tiposDialogo, Boolean addProduccion){
        iniciarliarDialog(controlador,respuestaDialogListener,datos,tiposDialogo,addProduccion);
    }

    private void iniciarliarDialog(Controlador controlador, final InterfaceDialogs respuestaDialogListener, Object datos, tiposDialogos tiposDialogo, Boolean addProduccion){
        this.DialogListener = respuestaDialogListener;
        this.controlador = controlador;

        AlertDialog.Builder builder = new AlertDialog.Builder(controlador.getActivity(),R.style.AlerDialogCustom);

        LayoutInflater layoutInflater = controlador.getActivity().getLayoutInflater();

        AlertDialog dialog = null;

        if(tiposDialogo == tiposDialogos.DIALOG_ADD_PUESTOS){
            dialog = dialogoCapturaPuestos(layoutInflater,builder,respuestaDialogListener);
        }else if (tiposDialogo == tiposDialogos.DIALOG_ADD_TRABAJADOR){
            dialog = dialogoCapturaCapturaTrabajadores(layoutInflater,builder,respuestaDialogListener,(Trabajadores) datos);
        }else if(tiposDialogo == tiposDialogos.DIALOG_ADD_ACTIVIDADES){
            dialog = dialogoCapturaActividades(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_CAMBIO_ACTIVIDAD){
            dialog = dialogoCambioActividades(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_ADD_TAMANIO_CAJAS){
            dialog = dialogoCapturaTamanioCajas(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_SELECCION_ACTIVIDAD){
            dialog = dialogoSeleccionActividades(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_CAMBIO_PUESTO){
            dialog = dialogoCapturaCambioPuesto(layoutInflater,builder,respuestaDialogListener,(Trabajadores) datos);
        }else if(tiposDialogo == tiposDialogos.DIALOG_CAPTURA_NUMERO_TRABAJADOR){
            dialog = dialogoCapturaNumeroTrabajador(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_CAPTURA_PRODUCCION){
            dialog = dialogoCapturaProduccion(layoutInflater,builder,respuestaDialogListener,(Trabajadores) datos,addProduccion);
        }else if(tiposDialogo == tiposDialogos.DIALOG_FINALIZAR_JORNADA){
            dialog = dialogoFinalizarJornada(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_CONFIGURACION){
            dialog = dialogoConfiguracion(layoutInflater,builder,respuestaDialogListener);
        }else if(tiposDialogo == tiposDialogos.DIALOG_ENVIAR_CORREO){
            dialog = dialogoCorreo(layoutInflater,builder);
        }


        if(dialog!=null){
            dialog.show();

            Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if(button!=null)
                button.setTextColor(controlador.getActivity().getResources().getColor(R.color.colorAccentDark));

            button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(button!=null)
                button.setTextColor(controlador.getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private AlertDialog dialogoCapturaCapturaTrabajadores(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener, final Trabajadores trabajadores) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_trabajador,null);


        et_nombre = (EditText) viewDialog.findViewById(R.id.etNombre);
        et_apellidoPaterno = (EditText) viewDialog.findViewById(R.id.etApellidoPaterno);
        et_apellidoMaterno = (EditText) viewDialog.findViewById(R.id.etApellidoMaterno);

        sp_puesto = (Spinner) viewDialog.findViewById(R.id.spPuesto);
        ArrayList<CatalogoPuestos> categoriasArrayList = controlador.getListaPuestos();
        ArrayAdapter<CatalogoPuestos> categoriasArrayAdapter = new ArrayAdapter<CatalogoPuestos>(controlador.getActivity(),R.layout.support_simple_spinner_dropdown_item,categoriasArrayList);
        sp_puesto.setAdapter(categoriasArrayAdapter);

        if(trabajadores !=null){

            et_nombre.setText(trabajadores.getNombre().toString());
            et_apellidoPaterno.setText(trabajadores.getApellidoPaterno().toString());
            et_apellidoMaterno.setText(trabajadores.getApellidoMaterno().toString());
            sp_puesto.setSelection(Complementos.getIndex(sp_puesto,trabajadores.getPuestosActual().getDescripcion()));

            if(!controlador.getActivity().getComponentName().getClassName().equals(MainActivity.class.getName())){
                sp_puesto.setVisibility(View.INVISIBLE);
                ((TextView) viewDialog.findViewById(R.id.lb_puesto_add)).setVisibility(View.INVISIBLE);
            }
        }

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Trabajadores trabajador = new Trabajadores(0,et_nombre.getText().toString(),et_apellidoPaterno.getText().toString(),et_apellidoMaterno.getText().toString(),(CatalogoPuestos) sp_puesto.getSelectedItem());

                        if(trabajadores!=null){//si no es null entonses es una modificacion del trabajador
                            trabajador.setIdTrabajdor(trabajadores.getIdTrabajdor());
                            trabajador.setConsecutivo(trabajadores.getConsecutivo());

                        }

                        respuestaDialogListener.onDialogPositiveClickCapturaTrabajadores(trabajador,trabajadores);

                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCapturaPuestos(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener ){
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_puestos,null);

        et_descripcionPuestos = (EditText) viewDialog.findViewById(R.id.dtv_descripcionPuestos);

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!et_descripcionPuestos.getText().toString().equals("")){
                            respuestaDialogListener.onDialogPositiveClickCapturaPuestos(et_descripcionPuestos.getText().toString());
                            dialogInterface.dismiss();
                        }else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.ERROR_CAMPOS_VACIOS);
                        }


                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCapturaActividades(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener ){
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_actividades,null);

        et_descripcionActividad = (EditText) viewDialog.findViewById(R.id.dtv_actividad);
        sp_tamanioCajas = (Spinner) viewDialog.findViewById(R.id.sp_tamanioCajas);

        ArrayList<CatalogoCajas> categoriasArrayList = controlador.getListaTamaiosCajas();
        ArrayAdapter<CatalogoCajas> categoriasArrayAdapter = new ArrayAdapter<CatalogoCajas>(controlador.getActivity(),R.layout.support_simple_spinner_dropdown_item,categoriasArrayList);
        sp_tamanioCajas.setAdapter(categoriasArrayAdapter);

        builder.setView(viewDialog)
                //.setMessage("Ingrese Actividad")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!et_descripcionActividad.getText().toString().equals("") && sp_tamanioCajas.getSelectedItemPosition()!= 0){
                            respuestaDialogListener.onDialogPositiveClickCapturaActividad(et_descripcionActividad.getText().toString(),((CatalogoCajas)sp_tamanioCajas.getSelectedItem()));
                            dialogInterface.dismiss();
                        } else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.ERROR_CAMPOS_VACIOS);
                        }

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCapturaTamanioCajas(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener ){
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_tamanio_cajas,null);

        et_descripcionTamanioCajas = (EditText) viewDialog.findViewById(R.id.tv_descripcion_caja);
        et_CantindadTamanioCajas = (EditText) viewDialog.findViewById(R.id.tv_cantidad_cajas);

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!et_descripcionTamanioCajas.getText().toString().equals("") && !et_CantindadTamanioCajas.getText().toString().equals("")){
                            respuestaDialogListener.onDialogPositiveClickCapturaTamanioCajas(et_descripcionTamanioCajas.getText().toString(),Integer.parseInt(et_CantindadTamanioCajas.getText().toString()));
                            dialogInterface.dismiss();
                        } else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.ERROR_CAMPOS_VACIOS);
                        }

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoSeleccionActividades(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener ){
        //View viewDialog = layoutInflater.inflate(R.layout.dialog_add_actividades,null);
        final CharSequence[] selectedItems = controlador.getListaActividadesArray();  // Where we track the selected items

        //tv_descripcionActividad = (EditText) viewDialog.findViewById(R.id.dtv_actividad);

        builder//.setView(viewDialog)
                .setSingleChoiceItems(selectedItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("solo"," "+i);
                        indexActividad = i;
                    }
                })
                //.setMessage("Seleccione Actividad")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("solo","seleccion "+indexActividad);
                        if(indexActividad>0){
                            respuestaDialogListener.onDialogPositiveClickSeleccionActividad(controlador.getListaActividades(indexActividad
                            ));

                            dialogInterface.dismiss();
                        }else{
                            Complementos.mensajesError(controlador.getActivity(), Controlador.TiposError.ERROR_VALOR_NO_ENCONTRADO);
                        }


                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCambioActividades(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener ){
        //View viewDialog = layoutInflater.inflate(R.layout.dialog_add_actividades,null);
        final CharSequence[] selectedItems = controlador.getListaActividadesArray();  // Where we track the selected items
        CatalogoActividades actividades = controlador.getSetting().getActividades();
        //tv_descripcionActividad = (EditText) viewDialog.findViewById(R.id.dtv_actividad);
        Log.e("solo"," "+selectedItems.length);
        builder//.setView(viewDialog)
                //.setTitle("ACTIVIDAD ANTERIOR: "+actividades.getDescripcion())
                .setSingleChoiceItems(selectedItems,controlador.getIndexListaActividad(actividades), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        indexActividad = i;
                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(indexActividad>0){
                            respuestaDialogListener.onDialogPositiveClickCambiarActividad(controlador.getListaActividades(indexActividad));

                            dialogInterface.dismiss();
                        }else{
                            Complementos.mensajesError(controlador.getActivity(), Controlador.TiposError.ERROR_VALOR_NO_ENCONTRADO);
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        ListView listView = dialog.getListView();
        /*if(listView!=null)
            listView.setColor(controlador.getActivity().getResources().getColor(R.color.colorFontDark));*/
        return dialog;
    }

    private AlertDialog dialogoCapturaCambioPuesto(LayoutInflater layoutInflater, AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener, final Trabajadores trabajadores) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_cambio_puesto,null);



        tvTrabajador = (TextView) viewDialog.findViewById(R.id.tv_nombre);
        tvConsecutivo = (TextView) viewDialog.findViewById(R.id.tv_numero);
        tvPuestoActual = (TextView) viewDialog.findViewById(R.id.tv_puesto);
        sp_puesto = (Spinner) viewDialog.findViewById(R.id.sp_puesto);
        horaCambio = (TimePicker)  viewDialog.findViewById(R.id.tp_horaCambio);
        horaCambio.setIs24HourView(true);

        ArrayList<CatalogoPuestos> categoriasArrayList = controlador.getListaPuestos();
        ArrayAdapter<CatalogoPuestos> categoriasArrayAdapter = new ArrayAdapter<CatalogoPuestos>(controlador.getActivity(),R.layout.support_simple_spinner_dropdown_item,categoriasArrayList);
        sp_puesto.setAdapter(categoriasArrayAdapter);

        tvTrabajador.setText(trabajadores.getTrabajador());
        tvConsecutivo.setText("Consecutivo: "+trabajadores.getConsecutivo());
        tvPuestoActual.setText(trabajadores.getPuestosActual().getDescripcion());
        sp_puesto.setSelection(0);

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String hora = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hora = horaCambio.getHour()+":"+horaCambio.getMinute()+":"+ Calendar.getInstance().get(Calendar.SECOND);
                        }else{
                            hora = horaCambio.getCurrentHour()+":"+horaCambio.getCurrentMinute()+":"+ Calendar.getInstance().get(Calendar.SECOND)+":"+Calendar.getInstance().get(Calendar.MILLISECOND);
                        }
                        respuestaDialogListener.onDialogPositiveClickCambiarPuesto(trabajadores.getConsecutivo(),(CatalogoPuestos)sp_puesto.getSelectedItem(),hora);
                        //dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCapturaNumeroTrabajador(final LayoutInflater layoutInflater,final AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_numero_captura,null);

        capturaNumeroTrabajador = (EditText) viewDialog.findViewById(R.id.et_dialog_numero_trabajador);


        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String numeroTrabajador = capturaNumeroTrabajador.getText().toString();
                        Trabajadores t = controlador.getTrabajador(numeroTrabajador);

                        if(t!=null){
                            AlertDialog dialog = dialogoCapturaCambioPuesto(layoutInflater, builder, respuestaDialogListener, t);
                            if(dialog!=null){
                                dialog.show();

                                Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                                if(button!=null)
                                    button.setTextColor(controlador.getActivity().getResources().getColor(R.color.colorAccentDark));

                                button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                if(button!=null)
                                    button.setTextColor(controlador.getActivity().getResources().getColor(R.color.colorPrimaryDark));
                            }
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCapturaProduccion(final LayoutInflater layoutInflater, final AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener, final Trabajadores trabajador, final Boolean addProduccion) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_captura_produccion,null);

        TextView tvNombreTrabajador = (TextView) viewDialog.findViewById(R.id.lb_nombreTrabajador_produccion);
        TextView tvConsecutivo = (TextView) viewDialog.findViewById(R.id.lb_consecutivo_produccion);
        final EditText etPrimera = (EditText) viewDialog.findViewById(R.id.et_primera_produccion);
        final EditText etSegunda = (EditText) viewDialog.findViewById(R.id.et_segunda_produccion);
        final EditText etAgranel = (EditText) viewDialog.findViewById(R.id.et_agranel_produccion);

        tvNombreTrabajador.setText(trabajador.getTrabajador());
        tvConsecutivo.setText("Consecutivo: "+trabajador.getConsecutivo());
        etPrimera.setText("");
        etSegunda.setText("");
        etAgranel.setText("");

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Producto p = new Producto(Complementos.getDateTimeActual(),trabajador,controlador.getActividad()
                                ,Integer.parseInt(etPrimera.getText().toString().equals("")?"0":etPrimera.getText().toString())
                                ,Integer.parseInt(etSegunda.getText().toString().equals("")?"0":etSegunda.getText().toString())
                                ,Integer.parseInt(etAgranel.getText().toString().equals("")?"0":etAgranel.getText().toString()));

                        respuestaDialogListener.onDialogPositiveClickCapturarProduccion(p,addProduccion);
                        dialogInterface.dismiss();

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });



        final AlertDialog dialog = builder.create();

        return dialog;
    }

    private AlertDialog dialogoFinalizarJornada(final LayoutInflater layoutInflater,final AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_finalizar_jormada,null);

        //capturaNumeroTrabajador = (EditText) viewDialog.findViewById(R.id.et_dialog_numero_trabajador);


        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        respuestaDialogListener.onDialogPositiveClickFinalizarJornada();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoConfiguracion(final LayoutInflater layoutInflater,final AlertDialog.Builder builder, final InterfaceDialogs respuestaDialogListener) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_configuracion_app,null);

        final EditText etPara = (EditText) viewDialog.findViewById(R.id.et_para);
        final EditText etcc = (EditText) viewDialog.findViewById(R.id.et_conCopia);
        final EditText etAsunto = (EditText) viewDialog.findViewById(R.id.et_asunto);
        final EditText etMensaje = (EditText) viewDialog.findViewById(R.id.et_mensaje);
        final EditText etTiempoCaptura = (EditText) viewDialog.findViewById(R.id.et_tiempoMas);

        final Configuracion configuracionAnterior = controlador.getConfiguracion();

        if(configuracionAnterior!=null){
            etPara.setText(configuracionAnterior.getPara());
            etcc.setText(configuracionAnterior.getCc());
            etAsunto.setText(configuracionAnterior.getAsunto());
            etMensaje.setText(configuracionAnterior.getMensaje());
            etTiempoCaptura.setText(configuracionAnterior.getTiempoMas().toString());
        }

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Configuracion configuracion = null;
                        if(configuracionAnterior!=null)
                            configuracion = new Configuracion(configuracionAnterior.getId(),configuracionAnterior.getUrlApi(),etPara.getText().toString(),etcc.getText().toString(),etAsunto.getText().toString(),etMensaje.getText().toString(),Integer.parseInt(etTiempoCaptura.getText().toString()),configuracionAnterior.getTiempoMenos());
                       else{
                            configuracion = new Configuracion(null,"",etPara.getText().toString(),etcc.getText().toString(),etAsunto.getText().toString(),etMensaje.getText().toString(),Integer.parseInt(etTiempoCaptura.getText().toString()),0);
                        }
                        respuestaDialogListener.onDialogPositiveClickConfiguracion(configuracion,configuracionAnterior);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog dialogoCorreo(final LayoutInflater layoutInflater,final AlertDialog.Builder builder) {
        View viewDialog = layoutInflater.inflate(R.layout.dialog_enviar_email,null);

        final TextView tvFechaInicio = (TextView) viewDialog.findViewById(R.id.tv_fecha_inicial_mail);
        final TextView tvFechaFinal = (TextView) viewDialog.findViewById(R.id.tv_fecha_final_mail);

        tvFechaInicio.setText(Complementos.obtenerFechaString(Complementos.getDateActual()));
        tvFechaFinal.setText(Complementos.obtenerFechaString(Complementos.getDateActual()));

        tvFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFecha(tvFechaInicio);
            }
        });

        tvFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFecha(tvFechaFinal);
            }
        });

        builder.setView(viewDialog)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Controlador.TiposError tiposError = controlador.enviarCorreo(tvFechaInicio.getText().toString(), tvFechaFinal.getText().toString());
                        Complementos.mensajesError(controlador.getActivity(),tiposError);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        return dialog;
    }

    public void getFecha(final TextView textView){
        // Get Current Date
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(controlador.getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String d = (dayOfMonth)+"";
                        if(d.length()<2)
                            d = "0"+(d);

                        String m = (monthOfYear+1)+"";
                        if(m.length()<2)
                            m = "0"+(m);

                        textView.setText(d + "/" + m + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}


