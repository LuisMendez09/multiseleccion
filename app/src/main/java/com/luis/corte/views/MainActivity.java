package com.luis.corte.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.views.adaptadores.ListaTrabajadorAdapter;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;
import com.luis.corte.views.dialogForm.InterfaceDialogs;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Trabajadores;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements InterfaceDialogs {
    ListaTrabajadorAdapter adaptadorTrabajadores;
    TextView tvAsistencia;
    ListView lvTrabajadores;
    private Controlador controlador;
    AbsListView.MultiChoiceModeListener multiseleccion;
    public static ActionMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controlador = new Controlador(this);

        FileLog.i(Complementos.TAG_MAIN,"inicia pase de asistencia");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarModal();
                FileLog.i(Complementos.TAG_MAIN,"inicia captura nuevo trabajador");
                new CapturaDialogDialog(controlador, MainActivity.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
            }
        });

        tvAsistencia = (TextView) findViewById(R.id.tv_asistencia);
        lvTrabajadores =(ListView)findViewById(R.id.lvTrabajadores);

        adaptadorTrabajadores=new ListaTrabajadorAdapter(this,controlador,tvAsistencia,this.getClass());
        lvTrabajadores.setAdapter(adaptadorTrabajadores);

        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());

        Button btnGuardar = (Button) this.findViewById(R.id.btnGuardarTrabajadores);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileLog.i(Complementos.TAG_MAIN,"Inicioa proceso de guardar trabajadores "+adaptadorTrabajadores.getCount());
                if(adaptadorTrabajadores.getCount()>0){
                    new CapturaDialogDialog(controlador, MainActivity.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_SELECCION_ACTIVIDAD);
                }else{
                    FileLog.i(Complementos.TAG_MAIN,"Error al guardar la lista de trabajadores"+Controlador.TiposError.ERROR_SIN_TRABAJADORES);
                    Complementos.mensajesError(MainActivity.this,Controlador.TiposError.ERROR_SIN_TRABAJADORES);
                }
            }
        });

        lvTrabajadores.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        multiseleccion = multiseleccion();
        lvTrabajadores.setMultiChoiceModeListener(multiseleccion);

        lvTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Trabajadores trabajadores = adaptadorTrabajadores.getItem(position);

                AlertDialog.Builder menu = new AlertDialog.Builder(controlador.getActivity());
                ListView itemsMenu = new ListView(controlador.getActivity());
                String items [] = new String[]{"EDITAR TRABAJADOR"};

                ArrayAdapter<String> adapterMenu = new ArrayAdapter<>(controlador.getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,items);
                itemsMenu.setAdapter(adapterMenu);
                menu.setView(itemsMenu);
                final AlertDialog dialog = menu.create();
                dialog.show();

                itemsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            Controlador.TiposError tiposError = controlador.validarInicioSesion();
                            if(tiposError== Controlador.TiposError.SESION_INICIADA || tiposError== Controlador.TiposError.SESION_REINIADA){
                                FileLog.i(Complementos.TAG_MAIN,"inicia captura modificacion del trabajador: "+trabajadores.toString());
                                new CapturaDialogDialog(controlador, adaptadorTrabajadores,trabajadores, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
                            }else{
                                Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
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
                MainActivity.mode = mode;
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
                            if(trabajador.getPuestosActual().getId()>2){
                                Trabajadores trabajadorAnterior = new Trabajadores(trabajador);
                                trabajador.setPuestosActual(controlador.getListaPuestos(2L));

                                Controlador.TiposError tiposError = controlador.updateTrabajadores(trabajador, trabajadorAnterior);
                                if(tiposError== Controlador.TiposError.EXITOSO)
                                    tiposError = controlador.actualizarUltimoPuesto(trabajador);

                                if(tiposError!= Controlador.TiposError.EXITOSO)
                                    Complementos.mensajesError(controlador.getActivity(),tiposError);
                            }
                        }
                        break;
                    case R.id.menu_asistencia:
                        FileLog.i(Complementos.TAG_MAIN,"iniciar captura de asistencia multiseleccion");
                        for (Integer posicion : currentCheckedPosition) {
                            Trabajadores trabajador = adaptadorTrabajadores.getItem(posicion);
                            if(trabajador.getPuestosActual().getId()==2){
                                Trabajadores trabajadorAnterior = new Trabajadores(trabajador);
                                trabajador.setPuestosActual(controlador.getListaPuestos(3L));

                                Controlador.TiposError tiposError = controlador.updateTrabajadores(trabajador, trabajadorAnterior);
                                if(tiposError== Controlador.TiposError.EXITOSO)
                                    tiposError = controlador.actualizarUltimoPuesto(trabajador);

                                if(tiposError!= Controlador.TiposError.EXITOSO)
                                    Complementos.mensajesError(controlador.getActivity(),tiposError);
                            }
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
/*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //adaptadorTrabajadores.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        cerrarModal();
        switch (item.getItemId()){
            case R.id.action_addPuestos:
                FileLog.i(Complementos.TAG_MAIN,"Agregar nuevo puesto");
                new CapturaDialogDialog(controlador, MainActivity.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_PUESTOS);
                break;
            case R.id.action_addActividades:
                FileLog.i(Complementos.TAG_MAIN,"Agregar nueva actividad");
                new CapturaDialogDialog(controlador,MainActivity.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_ACTIVIDADES);
                break;
            case R.id.action_addCajas:
                FileLog.i(Complementos.TAG_MAIN,"Agregar nueva tipo de caja");
                new CapturaDialogDialog(controlador,MainActivity.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TAMANIO_CAJAS);
                break;
            case R.id.action_configuracion:
                FileLog.i(Complementos.TAG_MAIN,"Agregar o configurar las configuracion");
                new CapturaDialogDialog(controlador,MainActivity.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CONFIGURACION);
                break;
            case R.id.action_importar:
                FileLog.i(Complementos.TAG_MAIN,"iniciar la importacion de trabajadores");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/vnd.ms-excel");
                startActivityForResult(intent,42);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
            FileLog.i(Complementos.TAG_MAIN, "archivo seleccionado Uri: " + uri.getPath());
            Controlador.TiposError tiposError = this.controlador.importarTrabajadores(uri);
            tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
            adaptadorTrabajadores.notifyDataSetChanged();
            Complementos.mensajesError(this,tiposError);
        }
    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores,Trabajadores trabajadoresAnterior) {
        FileLog.i(Complementos.TAG_MAIN,"guardar datos del nuevo trabajador: "+trabajadores.toString());
        adaptadorTrabajadores.add(trabajadores);
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText("Asistencia: "+controlador.totalAsistencia());
    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {
        if(catalogoActividades!=null){
            FileLog.i(Complementos.TAG_MAIN,"actividad seleccionada: "+catalogoActividades.toString1());
            controlador.iniciarSession(catalogoActividades);
            Intent intent = new Intent(MainActivity.this, Produccion.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto, Boolean addProduccion) {
        //no implementar
    }

    @Override
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos, String horaCambio) {
        //no implementar
    }

    @Override
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades) {
        //no implementar
    }

    @Override
    public void onDialogPositiveClickCapturaPuestos(String descripcion) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos del nuevo puesto");
        Controlador.TiposError tiposError = controlador.setCategoriaPuestos(new CatalogoPuestos(descripcion));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCajas) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos de la nueva actividad");
        Controlador.TiposError tiposError = controlador.setCatalogoActividades(new CatalogoActividades(descripcion,tamanioCajas));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos de la nueva caja");
        Controlador.TiposError tiposError = controlador.setCatalogoTamanioCajas(new CatalogoCajas(descripcion,cantidad));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickFinalizarJornada() {
        //no implementar
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos de la configuracion");
        Controlador.TiposError tiposError = controlador.addConfiguracion(configuracion, configuracionAnterior);
        Complementos.mensajesError(this,tiposError);
    }
}
