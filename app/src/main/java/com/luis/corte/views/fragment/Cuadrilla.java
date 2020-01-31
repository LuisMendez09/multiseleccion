package com.luis.corte.views.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.adaptadores.ListaTrabajadorAdapter;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;
import com.luis.corte.views.dialogForm.InterfaceDialogs;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cuadrilla extends Fragment implements InterfaceDialogs {
    private Controlador controlador;
    private static final String TAG = "Cuadrilla";

    ListaTrabajadorAdapter adaptadorTrabajadores;
    public TextView tvAsistencia;
    ListView lvTrabajadores;

    public Cuadrilla(){}


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuadrilla, container, false);

        tvAsistencia = (TextView) view.findViewById(R.id.tv_asistencia);
        lvTrabajadores =(ListView)view.findViewById(R.id.lvTrabajadores);
        ((Button) view.findViewById(R.id.btnGuardarTrabajadores)).setVisibility(View.INVISIBLE);

        adaptadorTrabajadores=new ListaTrabajadorAdapter(this.getActivity(),this.controlador,tvAsistencia,this.getClass());
        lvTrabajadores.setAdapter(adaptadorTrabajadores);


        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador, Cuadrilla.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
            }
        });

        return view;
    }



    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("menuItem",item.toString());
        adaptadorTrabajadores.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

    public void actualizarLista(){
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
    }


    @Override
    public void onDialogPositiveClickCapturaPuestos(String descripcion) {

    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {

    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCaja) {

    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores, Trabajadores trabajadorAnterior) {
        adaptadorTrabajadores.add(trabajadores);
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {

    }

    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto, Boolean addProduccion) {

    }

    @Override
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos, String horaCambio) {

    }

    @Override
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades) {

    }

    @Override
    public void onDialogPositiveClickFinalizarJornada() {
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {

    }
}
