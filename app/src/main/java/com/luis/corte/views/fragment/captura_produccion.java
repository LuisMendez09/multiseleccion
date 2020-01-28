package com.luis.corte.views.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.ReporteProduccion;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.Produccion;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class captura_produccion extends Fragment {

    private Controlador controlador;
    private Button btnMas;
    private Button btnMenos;
    private Button btnConsulta;
    private TextView tvActividad;
    private TextView tvTrabajador;
    private TextView tvConsecutivo;
    private TextView tvNombreTrabajador;
    private TextView tvPrimera;
    private TextView tvSegunda;
    private TextView tvAgranel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_captura_produccion, container, false);

        this.controlador = new Controlador(this.getActivity());

        this.tvActividad = (TextView) view.findViewById(R.id.tv_actividad);
        this.tvActividad.setText(this.controlador.getActividadActual());

        this.tvTrabajador = (TextView) view.findViewById(R.id.et_numeroTrabajador);
        this.tvTrabajador.setText("");

        this.tvConsecutivo = (TextView) view.findViewById(R.id.tv_consecutivo);
        this.tvConsecutivo.setText("");

        this.tvNombreTrabajador = (TextView) view.findViewById(R.id.tv_nombre);
        this.tvNombreTrabajador.setText("");

        this.tvPrimera = (TextView) view.findViewById(R.id.tv_primera);
        this.tvPrimera.setText("");

        this.tvSegunda = (TextView) view.findViewById(R.id.tv_segunda);
        this.tvSegunda.setText("");

        this.tvAgranel = (TextView) view.findViewById(R.id.tv_agranel);
        this.tvAgranel.setText("");

        setLisenerButtons(view);
        return view;
    }

    private void setLisenerButtons(View view){

        View.OnClickListener buttonLisener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_mas:
                        //agregar produccion
                        addProduccion();
                        limpiar();
                        break;
                    case R.id.btn_menos:
                        //quitar produccion
                        removeProduccion();
                        limpiar();
                        break;
                    case R.id.btn_consulta:
                        //consultar produccion

                        Trabajadores t = controlador.getTrabajador(tvTrabajador.getText().toString());
                        if(t!=null){
                            limpiar();
                            actualizarDatos(t);
                        }
                        else
                            Complementos.mensajesError(controlador.getActivity(), Controlador.TiposError.ERROR_TRABAJADOR_NO_VALIDO);
                        break;
                    case R.id.btn_0:
                        if(!tvTrabajador.getText().toString().equals(""))
                            tvTrabajador.setText(tvTrabajador.getText().toString()+"0");
                        break;
                    case R.id.btn_1:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"1");
                        break;
                    case R.id.btn_2:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"2");
                        break;
                    case R.id.btn_3:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"3");
                        break;
                    case R.id.btn_4:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"4");
                        break;
                    case R.id.btn_5:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"5");
                        break;
                    case R.id.btn_6:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"6");
                        break;
                    case R.id.btn_7:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"7");
                        break;
                    case R.id.btn_8:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"8");
                        break;
                    case R.id.btn_9:
                        tvTrabajador.setText(tvTrabajador.getText().toString()+"9");
                        break;
                    case R.id.btn_borrar:
                        limpiar();
                        break;

                }
            }
        };

        btnMas = (Button) view.findViewById(R.id.btn_mas);
        btnMas.setOnClickListener(buttonLisener);

        btnMenos = (Button) view.findViewById(R.id.btn_menos);
        btnMenos.setOnClickListener(buttonLisener);

        btnConsulta = (Button) view.findViewById(R.id.btn_consulta);
        btnConsulta.setOnClickListener(buttonLisener);

        Button btn = (Button) view.findViewById(R.id.btn_0);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_1);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_2);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_3);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_4);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_5);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_6);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_7);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_8);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_9);
        btn.setOnClickListener(buttonLisener);

        btn = (Button) view.findViewById(R.id.btn_borrar);
        btn.setOnClickListener(buttonLisener);
    }

    public void actualizarTvActividadActiva(String actividad){
        this.tvActividad.setText(actividad);
    }

    public void actualizarDatos(Trabajadores trabajador){
        ReporteProduccion reporteProduccion = controlador.getTotalProduccion(trabajador, controlador.getActividad());
        if(reporteProduccion!=null){
            this.tvTrabajador.setText("");
            this.tvConsecutivo.setText(reporteProduccion.getConsecutivo().toString());
            this.tvNombreTrabajador.setText(reporteProduccion.getTrabajador());
            this.tvPrimera.setText(Controlador.calcularCajas(reporteProduccion.getCajasPrimera(),reporteProduccion.getVazquetesPrimera(),true));
            this.tvSegunda.setText(Controlador.calcularCajas(reporteProduccion.getCajasSegunda(),reporteProduccion.getVazquetesSegunda(),true));
            this.tvAgranel.setText(Controlador.calcularCajas(reporteProduccion.getCajasAgranel(),reporteProduccion.getVazquetesAgranel(),false));
        }
    }

    private void limpiar(){
        tvTrabajador.setText("");
        this.tvConsecutivo.setText("");
        this.tvNombreTrabajador.setText("");
        this.tvPrimera.setText("");
        this.tvSegunda.setText("");
        this.tvAgranel.setText("");
    }

    private void addProduccion(){
        Log.i("captura",tvTrabajador.getText().toString());
        Trabajadores trabajador = controlador.getTrabajador(tvTrabajador.getText().toString());
        Controlador.TiposError tiposError = controlador.validarPrecaptura(trabajador, Complementos.getDateTimeActual());
        if(tiposError== Controlador.TiposError.EXITOSO){
            new CapturaDialogDialog(controlador, (Produccion) controlador.getActivity(), trabajador, CapturaDialogDialog.tiposDialogos.DIALOG_CAPTURA_PRODUCCION,true);
        }else{
            //mensaje error
            limpiar();
            Complementos.mensajesError(controlador.getActivity(),tiposError);
        }
    }

    private void removeProduccion(){
        Log.i("captura",tvTrabajador.getText().toString());
        Trabajadores trabajador = controlador.getTrabajador(tvTrabajador.getText().toString());
        Controlador.TiposError tiposError = controlador.validarPrecaptura(trabajador, Complementos.getDateTimeActual());
        if(tiposError== Controlador.TiposError.EXITOSO){
            new CapturaDialogDialog(controlador, (Produccion) controlador.getActivity(), trabajador, CapturaDialogDialog.tiposDialogos.DIALOG_CAPTURA_PRODUCCION,false);
        }else{
            //mensaje error
            limpiar();
            Complementos.mensajesError(controlador.getActivity(),tiposError);
        }
    }
}
