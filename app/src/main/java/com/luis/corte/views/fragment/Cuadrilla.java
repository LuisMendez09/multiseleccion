package com.luis.corte.views.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.adaptadores.ListaTrabajadorAdapter;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;
import com.luis.corte.views.dialogForm.DialogListaPuestos;
import com.luis.corte.views.dialogForm.InterfaceDialogs;

import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cuadrilla extends Fragment implements InterfaceDialogs {
    private Controlador controlador;

    ListaTrabajadorAdapter adaptadorTrabajadores;
    public TextView tvAsistencia;
    ListView lvTrabajadores;
    Toolbar toolbar;
    AbsListView.MultiChoiceModeListener multiseleccion;

    public static ActionMode mode;
    public Cuadrilla(){}



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuadrilla, container, false);

        tvAsistencia = (TextView) view.findViewById(R.id.tv_asistencia);
        lvTrabajadores =(ListView)view.findViewById(R.id.lvTrabajadores);
        (view.findViewById(R.id.btnGuardarTrabajadores)).setVisibility(View.INVISIBLE);
        toolbar = this.getActivity().findViewById(R.id.toolbar);


        adaptadorTrabajadores=new ListaTrabajadorAdapter(this.getActivity(),this.controlador,tvAsistencia,this.getClass());
        lvTrabajadores.setAdapter(adaptadorTrabajadores);
        lvTrabajadores.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        multiseleccion = multiseleccion();
        lvTrabajadores.setMultiChoiceModeListener(multiseleccion);

        lvTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Trabajadores trabajadores = adaptadorTrabajadores.getItem(position);

                AlertDialog.Builder menu = new AlertDialog.Builder(controlador.getActivity());
                ListView itemsMenu = new ListView(controlador.getActivity());
                String items [] = new String[]{"EDITAR TRABAJADOR","PUESTOS REALIZADOS"};

                ArrayAdapter<String> adapterMenu = new ArrayAdapter<>(controlador.getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,items);
                itemsMenu.setAdapter(adapterMenu);
                menu.setView(itemsMenu);
                final AlertDialog dialog = menu.create();
                dialog.show();

                itemsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            FileLog.i(Complementos.TAG_CUADRILLA,"inicia dialogo de edicion de trabajador");
                            Controlador.TiposError tiposError = controlador.validarInicioSesion();
                            if(tiposError== Controlador.TiposError.SESION_INICIADA || tiposError== Controlador.TiposError.SESION_REINIADA){
                                new CapturaDialogDialog(controlador, adaptadorTrabajadores,trabajadores, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
                            }else{
                                Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                            }
                        }else{
                            FileLog.i(Complementos.TAG_CUADRILLA,"inicia dialogo de puestos realizados");
                            DialogListaPuestos dmp = new DialogListaPuestos(controlador.getActivity());
                            dmp.setPuestos(controlador.getPuestosTrabajador(trabajadores),trabajadores,Cuadrilla.this.controlador,adaptadorTrabajadores);
                            dmp.show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarModal();
                FileLog.i(Complementos.TAG_CUADRILLA,"inicia dialogo de captura de trabajador");
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador, Cuadrilla.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
            }
        });

        return view;
    }



    public static void cerrarModal(){
        if(mode!=null)
            mode.finish();
    }

    private AbsListView.MultiChoiceModeListener multiseleccion(){
        return  new AbsListView.MultiChoiceModeListener() {
            private int nr = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle("" + lvTrabajadores.getCheckedItemCount() + " items selected");
                if (checked) {
                    nr++;
                    adaptadorTrabajadores.setNewSelection(position, checked);
                } else {
                    nr--;
                    adaptadorTrabajadores.removeSelection(position);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.menu_contextual_actionbar, menu);
                Cuadrilla.this.mode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Set<Integer> currentCheckedPosition = adaptadorTrabajadores.getCurrentCheckedPosition();
                switch (item.getItemId()) {
                    case R.id.menu_falta:
                        FileLog.i(Complementos.TAG_MAIN,"iniciar captura de falta multiseleccion");
                        for (Integer posicion : currentCheckedPosition) {
                            Trabajadores trabajador = adaptadorTrabajadores.getItem(posicion);
                            Trabajadores trabajadorAnterior = new Trabajadores(trabajador);
                            trabajador.setPuestosActual(controlador.getListaPuestos(2L));

                            Controlador.TiposError tiposError = controlador.updateTrabajadores(trabajador, trabajadorAnterior);
                            if(tiposError== Controlador.TiposError.EXITOSO)
                                tiposError = controlador.actualizarUltimoPuesto(trabajador);

                            if(tiposError!= Controlador.TiposError.EXITOSO)
                                Complementos.mensajesError(controlador.getActivity(),tiposError);
                        }
                    break;
                    case R.id.menu_asistencia:
                        FileLog.i(Complementos.TAG_MAIN,"iniciar captura de asistencia multiseleccion");
                        for (Integer posicion : currentCheckedPosition) {
                            Trabajadores trabajador = adaptadorTrabajadores.getItem(posicion);
                            Trabajadores trabajadorAnterior = new Trabajadores(trabajador);
                            trabajador.setPuestosActual(controlador.getListaPuestos(3L));

                            Controlador.TiposError tiposError = controlador.updateTrabajadores(trabajador, trabajadorAnterior);
                            if(tiposError== Controlador.TiposError.EXITOSO)
                                tiposError = controlador.actualizarUltimoPuesto(trabajador);

                            if(tiposError!= Controlador.TiposError.EXITOSO)
                                Complementos.mensajesError(controlador.getActivity(),tiposError);
                        }
                        break;

                }
                nr = 0;
                adaptadorTrabajadores.clearSelection();
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                nr = 0;
                adaptadorTrabajadores.clearSelection();
            }


        };


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_configuracion) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adaptadorTrabajadores.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

    public void actualizarLista(){
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
    }


    @Override
    public void onDialogPositiveClickCapturaPuestos(String descripcion) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCaja) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores, Trabajadores trabajadorAnterior) {
        FileLog.i(Complementos.TAG_CUADRILLA,"iniciar guardado de trabajador nuevo");
        adaptadorTrabajadores.add(trabajadores);
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto, Boolean addProduccion) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos, String horaCambio) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades) {
//no implentar
    }

    @Override
    public void onDialogPositiveClickFinalizarJornada() {
        //no implentar
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {
//no implentar
    }
}
