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

        setContentView(R.layout.activity_produccion);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        controlador = new Controlador(this);
        fragmentManager = getFragmentManager();
        setListenerBotones();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Implementar metodo de cierre de aplicacion si las fecha actual no concuerdan con la del la app
        Log.i("inicio", "onStart "+ Complementos.getDateTimeActual().toString() );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_produccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addPuestos:
                new CapturaDialogDialog(controlador, Produccion.this, null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_PUESTOS);
                break;
            case R.id.action_addActividades:
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_ACTIVIDADES);
                break;
            case R.id.action_addCajas:
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ADD_TAMANIO_CAJAS);
                break;
            case R.id.action_CambiarActividad:
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CAMBIO_ACTIVIDAD);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
            break;
            case R.id.action_cambiarPuestos:
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_CAPTURA_NUMERO_TRABAJADOR);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
                break;
            case R.id.action_finzalir:
                if(controlador.validarInicioSesion()== Controlador.TiposError.SESION_INICIADA){
                    new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_FINALIZAR_JORNADA);
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
                break;
            case R.id.action_exportar:
                new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ENVIAR_CORREO);
                //controlador.enviarCorreo();
                break;
            case R.id.action_configuracion:
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

                Log.i("sesion",controlador.validarInicioSesion().name());

                switch (view.getId()){
                    case R.id.btn_produccion:
                        if(controlador.validarInicioSesion() == Controlador.TiposError.SESION_INICIADA){
                            fragmentTransaction.replace(R.id.fragment,new captura_produccion(),"frag_produccion");
                            fragmentTransaction.commit();
                        }else{
                            Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                        }
                        break;
                    case R.id.btn_cuadrilla:
                        Cuadrilla c = new Cuadrilla();
                        c.setControlador(controlador);
                        fragmentTransaction.replace(R.id.fragment,c,"frag_cuadrilla");
                        fragmentTransaction.commit();
                        break;
                    case R.id.btn_reporte:
                        reporte r = new reporte();
                        r.setControlador(controlador);
                        fragmentTransaction.replace(R.id.fragment,r,"frag_reporte");
                        fragmentTransaction.commit();
                        break;
                    case R.id.btn_enviar:
                        new CapturaDialogDialog(controlador,Produccion.this,null, CapturaDialogDialog.tiposDialogos.DIALOG_ENVIAR_CORREO);
                        break;
                    case R.id.btn_finalizar:
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
        FileLog.e("produccion_cambio",consecutivo+" || "+catalogoPuestos.getDescripcion()+" || "+horaCambio);

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
        Controlador.TiposError tiposError = controlador.setCategoriaPuestos(new CatalogoPuestos(descripcion));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaTamanioCajas(String descripcion, Integer cantidad) {
        Controlador.TiposError tiposError = controlador.setCatalogoTamanioCajas(new CatalogoCajas(descripcion,cantidad));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturaActividad(String descripcion, CatalogoCajas tamanioCaja) {
        Controlador.TiposError tiposError = controlador.setCatalogoActividades(new CatalogoActividades(descripcion,tamanioCaja));
        Complementos.mensajesError(controlador.getActivity(),tiposError);
    }

    @Override
    public void onDialogPositiveClickCapturarProduccion(Producto producto,Boolean addProduccion){

        Controlador.TiposError tiposError = null;

        if(addProduccion)
             tiposError = controlador.addProduccion(producto);
        else
            tiposError = controlador.deleteProduccion(producto);


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

        }
    }

    @Override
    public void onDialogPositiveClickConfiguracion(Configuracion configuracion, Configuracion configuracionAnterior) {
        Controlador.TiposError tiposError = controlador.addConfiguracion(configuracion, configuracionAnterior);
        Log.i("EMAIL",controlador.getConfiguracion().toString());
        Complementos.mensajesError(this,tiposError);
    }
}
