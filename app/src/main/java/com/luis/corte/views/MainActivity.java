package com.luis.corte.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements InterfaceDialogs {
    ListaTrabajadorAdapter adaptadorTrabajadores;
    TextView tvAsistencia;
    ListView lvTrabajadores;
    private Controlador controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controlador = new Controlador(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileLog.i(Complementos.TAG_MAIN,"Agregar nuevo trabajador");
                new CapturaDialogDialog(controlador, MainActivity.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TRABAJADOR);
            }
        });

        tvAsistencia = (TextView) findViewById(R.id.tv_asistencia);
        lvTrabajadores =(ListView)findViewById(R.id.lvTrabajadores);

        adaptadorTrabajadores=new ListaTrabajadorAdapter(this,controlador,tvAsistencia,this.getClass());
        lvTrabajadores.setAdapter(adaptadorTrabajadores);

        tvAsistencia.setText("Asistencia: "+String.valueOf(adaptadorTrabajadores.getCount()));

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
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adaptadorTrabajadores.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores,Trabajadores trabajadoresAnterior) {
        FileLog.i(Complementos.TAG_MAIN,"guardar datos del trabajador");
        adaptadorTrabajadores.add(trabajadores);
        adaptadorTrabajadores.notifyDataSetChanged();
        tvAsistencia.setText(String.valueOf(adaptadorTrabajadores.getCount()));
    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {
        if(catalogoActividades!=null){
            FileLog.i(Complementos.TAG_MAIN,"Iniciar sesion para la captura de produccion");
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
