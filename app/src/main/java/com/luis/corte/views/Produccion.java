package com.luis.corte.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.views.dialogForm.CapturaDialogDialog;
import com.luis.corte.views.dialogForm.InterfaceDialogs;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.fragment.Cuadrilla;
import com.luis.corte.views.fragment.captura_produccion;
import com.luis.corte.views.fragment.reporte;

public class Produccion extends AppCompatActivity implements InterfaceDialogs {

    private Button btnproduccion;
    private Button btnCuadrilla;
    private Button btnReporte;
    private Button btnEnviar;
    private Button btnFinalizar;
    private FragmentManager fragmentManager;

    private Controlador controlador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FileLog.i(Complementos.TAG_PRODUCCION,"inicia ventana de produccion");
        setContentView(R.layout.activity_produccion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controlador = new Controlador(this);
        fragmentManager = getFragmentManager();
        setListenerBotones();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_produccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Cuadrilla.cerrarModal();
        switch (item.getItemId()){
            case R.id.action_addPuestos:
                FileLog.i(Complementos.TAG_PRODUCCION,"Agregar nuevo puesto");
                new CapturaDialogDialog(controlador, Produccion.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_PUESTOS);
                break;
            case R.id.action_addActividades:
                FileLog.i(Complementos.TAG_PRODUCCION,"Agregar nueva actividad");
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_ACTIVIDADES);
                break;
            case R.id.action_addCajas:
                FileLog.i(Complementos.TAG_PRODUCCION,"Agregar nueva tipo de caja");
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TAMANIO_CAJAS);
                break;
            case R.id.action_CambiarActividad:
                FileLog.i(Complementos.TAG_PRODUCCION,"Agregar cambio de actividad");
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CAMBIO_ACTIVIDAD);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
            break;
            case R.id.action_cambiarPuestos:
                FileLog.i(Complementos.TAG_PRODUCCION,"Agregar cambio de puesto");
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CAPTURA_NUMERO_TRABAJADOR);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
                break;
            case R.id.action_finzalir:
                FileLog.i(Complementos.TAG_PRODUCCION,"Inicia cierre de sesion");
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_FINALIZAR_JORNADA);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
                break;
            case R.id.action_exportar:
                FileLog.i(Complementos.TAG_PRODUCCION,"inicia exportacion de datos");
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ENVIAR_CORREO);
                //controlador.enviarCorreo();
                break;
            case R.id.action_configuracion:
                FileLog.i(Complementos.TAG_PRODUCCION,"inicia configuracion de la app");
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CONFIGURACION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListenerBotones() {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Cuadrilla.cerrarModal();
                switch (view.getId()){
                    case R.id.btn_produccion:
                        FileLog.i(Complementos.TAG_PRODUCCION,"inicia fragment produccion");
                        if(controlador.validarInicioSesion() == Controlador.TiposError.SESION_INICIADA){
                            fragmentTransaction.replace(R.id.fragment,new captura_produccion(),"frag_produccion");
                            fragmentTransaction.commit();
                        }else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                        }
                        break;
                    case R.id.btn_cuadrilla:
                        FileLog.i(Complementos.TAG_PRODUCCION,"inicia fragment cuadrilla");
                        Cuadrilla c = new Cuadrilla();
                        c.setControlador(controlador);
                        fragmentTransaction.replace(R.id.fragment,c,"frag_cuadrilla");
                        fragmentTransaction.commit();
                        break;
                    case R.id.btn_reporte:
                        FileLog.i(Complementos.TAG_PRODUCCION,"inicia fragment reporte");
                        reporte r = new reporte();
                        r.setControlador(controlador);
                        fragmentTransaction.replace(R.id.fragment,r,"frag_reporte");
                        fragmentTransaction.commit();
                        break;
                    case R.id.btn_enviar:
                        FileLog.i(Complementos.TAG_PRODUCCION,"inicia exportacion de datos");
                        new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ENVIAR_CORREO);
                        break;
                    case R.id.btn_finalizar:
                        FileLog.i(Complementos.TAG_PRODUCCION,"Inicia cierre de sesion");
                        if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                            new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_FINALIZAR_JORNADA);
                        }else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                        }
                        break;
                }
            }
        };

        btnproduccion = (Button) findViewById(R.id.btn_produccion);
        btnproduccion.setOnClickListener(clickListener);
        btnCuadrilla = (Button) findViewById(R.id.btn_cuadrilla);
        btnCuadrilla.setOnClickListener(clickListener);
        btnReporte = (Button) findViewById(R.id.btn_reporte);
        btnReporte.setOnClickListener(clickListener);
        btnEnviar = (Button) findViewById(R.id.btn_enviar);
        btnEnviar.setOnClickListener(clickListener);
        btnFinalizar = (Button) findViewById(R.id.btn_finalizar);
        btnFinalizar.setOnClickListener(clickListener);
    }

    @Override
    public void onDialogPositiveClickCapturaTrabajadores(Trabajadores trabajadores, Trabajadores trabajadorAnterior) {
        //no se implementa
    }

    @Override
    public void onDialogPositiveClickSeleccionActividad(CatalogoActividades catalogoActividades) {
        //no se implementa
    }

    @Override
    public void onDialogPositiveClickCambiarPuesto(Integer consecutivo, CatalogoPuestos catalogoPuestos, String horaCambio) {
        FileLog.e(Complementos.TAG_PRODUCCION,"agregar cambio de puesto: "+consecutivo+"--"+catalogoPuestos.getDescripcion()+"--"+horaCambio);
        Controlador.TiposError tiposError = controlador.cambiarPuesto(consecutivo, catalogoPuestos, horaCambio);

        if(tiposError==Controlador.TiposError.EXITOSO){
            Cuadrilla frag_cuadrilla = (Cuadrilla)fragmentManager.findFragmentByTag("frag_cuadrilla");
            if(frag_cuadrilla!=null)
                frag_cuadrilla.actualizarLista();
        }else{
            Complementos.mensajesError(controlador.getActivity(),tiposError);
        }
    }

    @Override
    public void onDialogPositiveClickCambiarActividad(CatalogoActividades catalogoActividades) {
        FileLog.i(Complementos.TAG_PRODUCCION,"inicia cambio de actividad:"+catalogoActividades.toString());
        Controlador.TiposError tiposError = controlador.cambiarActividad(catalogoActividades);
        if(tiposError == Controlador.TiposError.EXITOSO){
            Fragment fragment = fragmentManager.findFragmentByTag("frag_produccion");
            if(fragment!=null){
                ((captura_produccion) fragment).actualizarTvActividadActiva(catalogoActividades.getDescripcion());
            }

        }
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaPuestos(String descripcion) {
        FileLog.i(Complementos.TAG_PRODUCCION,"guardar los datos del nuevo puesto");
        Controlador.TiposError tiposError = controlador.setCategoriaPuestos(new CatalogoPuestos(descripcion));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos de la nueva caja");
        Controlador.TiposError tiposError = controlador.setCatalogoTamanioCajas(new CatalogoCajas(descripcion,cantidad));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCaja) {
        FileLog.i(Complementos.TAG_MAIN,"guardar los datos de la nueva actividad");
        Controlador.TiposError tiposError = controlador.setCatalogoActividades(new CatalogoActividades(descripcion,tamanioCaja));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto,Boolean addProduccion){

        Controlador.TiposError tiposError = null;

        if(addProduccion){
            FileLog.i(Complementos.TAG_MAIN,"inicia guardado de produccion");
            tiposError = controlador.addProduccion(producto);
        }else{
            FileLog.i(Complementos.TAG_MAIN,"inicia resta de produccion");
            tiposError = controlador.deleteProduccion(producto);
        }

        if(tiposError == Controlador.TiposError.EXITOSO){
            Fragment fragment = fragmentManager.findFragmentByTag("frag_produccion");
            if(fragment!=null){
                ((captura_produccion) fragment).actualizarDatos(producto);
            }
        }else{
            Complementos.mensajesError(this,tiposError);
        }
    }

    @Override
    public void onDialogPositiveClickFinalizarJornada() {
        FileLog.i(Complementos.TAG_MAIN,"inicia petinicion de finalizacion de jornada");
        Controlador.TiposError tiposError = controlador.finalizarSesion();

        if(tiposError == Controlador.TiposError.EXITOSO){
            Fragment fragment = fragmentManager.findFragmentByTag("frag_produccion");
            if(fragment!=null){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                reporte r = new reporte();
                r.setControlador(controlador);
                fragmentTransaction.replace(R.id.fragment,r,"frag_reporte");
                fragmentTransaction.commit();
            }
        }else{
            Complementos.mensajesError(controlador.getActivity(),tiposError);
        }
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {
        FileLog.i(Complementos.TAG_PRODUCCION,"inicia peticion de guardado de configuraciones");
        Controlador.TiposError tiposError = controlador.addConfiguracion(configuracion, configuracionAnterior);
        Complementos.mensajesError(this,tiposError);
    }
}
