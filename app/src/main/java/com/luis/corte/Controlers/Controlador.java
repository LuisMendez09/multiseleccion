package com.luis.corte.Controlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.luis.corte.Controlers.handlers.DBHandler;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.models.Puestos;
import com.luis.corte.models.ReporteCajasTotales;
import com.luis.corte.models.ReporteProduccion;
import com.luis.corte.models.Settings;
import com.luis.corte.models.Trabajadores;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class Controlador {
    private Activity activity=null;
    private static Settings settings=null;
    private static Configuracion configuracion=null;
    private static ArrayList<Trabajadores> listaTrabajadores = new ArrayList<>();
    private static ArrayList<CatalogoPuestos> listaPuestos = new ArrayList<>();
    private static ArrayList<CatalogoActividades> listaActividades = new ArrayList<>();
    private static ArrayList<CatalogoCajas> listaTamaiosCajas = new ArrayList<>();
    private static DBHandler dbHandlerl=null;

    public static enum TiposError {
        ERROR_SETTING_NULL,
        ERROR_DB,
        ERROR_CAMPOS_VACIOS,
        ERROR_SIN_TRABAJADORES,
        ERROR_VALOR_NO_ENCONTRADO,
        ERROR_CONVERSION_FECHA,
        ERROR_HORA_FINAL,
        ERROR_SELECCION_PUESTO,
        ERROR_SELECCION_HORA,
        ERROR_PUESTO_NO_VALIDO,
        ERROR_HORA_CAPTURA_PRODUCCION,
        ERROR_TIEMPO_LIMITE,
        ERROR_TRABAJADOR_NO_VALIDO,
        ERROR_CREACION_ARCHIVO,
        ERROR_ENVIO_CORREO,
        ERROR_FECHA_SISTEMA_INVALIDA,
        SIN_CAMBIOS,
        SIN_CAMBIO_DE_PUESTO,
        SESION_NO_VALIDA,
        SESION_NO_FINALIZADA,
        SESION_FINALIZADA,
        SESION_INICIADA,
        SESION_REINIADA,
        SESION_REINICIAR,
        EXITOSO
    };

    public Controlador(Activity activity) {
        this.activity = activity;
        dbHandlerl = new DBHandler(this.activity);
        configuracion = dbHandlerl.getConfiguracion(Long.valueOf(1));
        if(configuracion==null){
            TiposError tiposError = addConfiguracion(new Configuracion("", "", "", "Produccion ", "", 0, 0), null);
            Log.i("captura",tiposError.name());
            if(tiposError==TiposError.EXITOSO){
                configuracion = getConfiguracion();
            }
        }
        getListaPuestos();
        getListaTamaiosCajas();
        getListaActividades();
        getSetting();
        getListaTrabajadores();
    }

    /////////////////////////////////METODOS ACTIVITY//////////////////////////////
    public Activity getActivity(){
        return this.activity;
    }
    //actualizar activity
    public void setActivity(Activity activity){
        this.activity =activity;
    }
    /////////////////////////////////METODOS CONFIGURACION//////////////////////////////
    public Configuracion getConfiguracion(){
        if(configuracion==null)
            configuracion = dbHandlerl.getConfiguracion(Long.valueOf(1));

        return configuracion;
    }

    public TiposError addConfiguracion(Configuracion c,Configuracion cAnterior){
        TiposError tiposError = validarCampo(c,cAnterior);

        if(tiposError==TiposError.EXITOSO){
            long respuesta = 0;
            if(c.getId()==null){
                respuesta  = dbHandlerl.addConfiguracion(c);
            }else{
                int i = dbHandlerl.updateConfiguracion(c);
                respuesta = i;
            }

            if(respuesta!=-1){
                configuracion=null;
                configuracion = c;
                tiposError = TiposError.EXITOSO;
            }else{
                tiposError = TiposError.ERROR_DB;
            }


        }

        return tiposError;
    }


    private TiposError validarCampo(Configuracion configuracion,Configuracion configuracionAnterior){
        TiposError tiposError = TiposError.EXITOSO;
        //if(!configuracion.getPara().equals("")&&!configuracion.getAsunto().equals("")){
            if(configuracionAnterior!=null){
                if(!configuracion.getPara().equals(configuracionAnterior.getPara())||!configuracion.getCc().equals(configuracionAnterior.getCc())
                        ||!configuracion.getAsunto().equals(configuracionAnterior.getAsunto())||!configuracion.getMensaje().equals(configuracionAnterior.getMensaje())
                        ||configuracion.getTiempoMas()!=configuracionAnterior.getTiempoMas()){

                }else{
                    tiposError = TiposError.SIN_CAMBIOS;
                }
            }
        /*}else{
            tiposError = TiposError.ERROR_CAMPOS_VACIOS;
        }*/

        return tiposError;
    }

    /////////////////////////////////METODOS DE SETTING//////////////////////////////
    public Settings getSetting(){
        settings = dbHandlerl.getSetting(Long.valueOf(1));
        if(settings==null){
            settings = new Settings(0,0,1,0,0,-1,listaActividades.get(0));
            dbHandlerl.addSettings(settings);
        }
        return settings;
    }

    public TiposError setSetting(Settings settings){
        TiposError r= TiposError.ERROR_DB;
        if(settings!=null) {
            Long i = dbHandlerl.addSettings(settings);
            if(i!=Long.valueOf(-1)){
                this.settings = settings;
                r = TiposError.EXITOSO;
            }
        }else{
            r = TiposError.ERROR_SETTING_NULL;
        }
        return r;
    }

    public TiposError updateSetting(Settings settings){
        TiposError r= TiposError.ERROR_DB;
        if(settings!=null) {
           int i = dbHandlerl.updateSetting(settings);
           if(i!=-1){
               this.settings = settings;
               r = TiposError.EXITOSO;
           }

        }else{
            r = TiposError.ERROR_SETTING_NULL;
        }
        return r;
    }

    public TiposError ReiniciarSession(){
        //dbHandlerl.recrearTablaTrabajadores();
        listaTrabajadores.clear();
        listaTrabajadores = null;
        listaTrabajadores = dbHandlerl.getTrabajdores();

        settings.setFechaInicio(Complementos.getDateTimeActual());
        settings.setFechaFinal(Long.valueOf(0));
        settings.setInicioJornada(1);
        settings.setFinJornada(0);
        settings.setCapturaTrabaajdor(0);
        settings.setActividades(listaActividades.get(0));
        TiposError tiposError = updateSetting(settings);
        return  tiposError;
    }

    public TiposError iniciarSession(CatalogoActividades catalogoActividades){
        settings.setCapturaTrabaajdor(listaTrabajadores.size());
        settings.setActividades(catalogoActividades);
        TiposError tiposError = updateSetting(settings);

        return  tiposError;
    }

    public TiposError finalizarSesion(){
        TiposError tiposError = TiposError.EXITOSO;

        //validar hora final
        Long dateFin = Complementos.getDateTimeActual();
        if(!settings.getFechaString().equals(Complementos.obtenerFechaString(Complementos.getDateActual()))) {
            try {
                dateFin = Complementos.convertirStringAlong(settings.getFechaString(),"20:00:00:0000");
            } catch (ParseException e) {
                e.printStackTrace();
                tiposError = TiposError.ERROR_CONVERSION_FECHA;
            }
        }

        if(tiposError==TiposError.EXITOSO){
            settings.setInicioJornada(0);
            settings.setFechaFinal(dateFin);
            settings.setFinJornada(1);
            settings.setCapturaTrabaajdor(-1);
            tiposError = updateSetting(settings);

            //crear puestos trabajadores
            if(tiposError==TiposError.EXITOSO){
                ArrayList<Trabajadores> listaTrabajadores = dbHandlerl.getTrabajdores();

                for (Trabajadores t :listaTrabajadores) {
                    Puestos puesto = dbHandlerl.getUltimoPuesto(settings.getFechaString(),t);
                    if(puesto==null){
                        puesto= new Puestos(settings.getFechaInicio(),settings.getFechaFinal(),t,0,t.getPuestosActual());
                        Long aLong = dbHandlerl.addPuesto(puesto);
                        tiposError = aLong==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
                    }
                    else{
                        puesto.setDateFin(settings.getFechaFinal());
                        int i = dbHandlerl.updatePuestos(puesto);
                        tiposError = i==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
                    }
                }
            }
        }

        return tiposError;
    }


    public TiposError validarInicioSesion(){//al finalizar poner en 0 iniciojornada en 1 finalizarjornada y 0 totaltrabajadores
        TiposError r = TiposError.SESION_NO_VALIDA;
        Log.i("inicio",settings.toString());
        if(settings.getInicioJornada()==0 && settings.getCapturaTrabaajdor()==-1 && settings.getFinJornada()==1 && !settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){//continuar  nueva sesion
            r = TiposError.SESION_REINICIAR;//sesion finalizada en diferente dia ---> reiniciar sesion y continuar a capturar trabajadores
        }else if(settings.getInicioJornada()==1 && settings.getCapturaTrabaajdor()>0 && settings.getFinJornada()==0 && settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){
            r = TiposError.SESION_INICIADA; // sesion no finalizada y en el mismo dia --> continuar
        }else if(settings.getInicioJornada()==1  && settings.getCapturaTrabaajdor()>0 && settings.getFinJornada()==0 && !settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){
            r = TiposError.SESION_NO_FINALIZADA; // sesion no finalizada y en dias diferentes --> finalizar sesion, reiniciar y continuar
        }else if(settings.getInicioJornada()==0  && settings.getCapturaTrabaajdor()==-1 && settings.getFinJornada()==1 && settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){
            r = TiposError.SESION_FINALIZADA; // sesion finalizada y en el mismo dia --> bloquear edicion , mostrar solo reportes
        }else if(settings.getInicioJornada()==1  && settings.getCapturaTrabaajdor()==0 && settings.getFinJornada()==0 && settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){
            r = TiposError.SESION_REINIADA; // sesion reiniciada sin captura de trabajadores --> ir a captura trabajadores
        }else if(settings.getInicioJornada()==1  && settings.getCapturaTrabaajdor()==0 && settings.getFinJornada()==0 && !settings.getFechaString().equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()))){
            r = TiposError.SESION_REINICIAR; // sesion reiniciada sin captura de trabajadores --> ir a captura trabajadores
        }

        return r;
    }

    public TiposError validarFechaSesion(){
        if(settings.getFechaString().equals(Complementos.obtenerFechaString(Complementos.getDateActual()))){
            return TiposError.EXITOSO;
        }else if(settings.getFechaInicio()>Complementos.getDateTimeActual()){
            return TiposError.ERROR_FECHA_SISTEMA_INVALIDA;
        }else{
            return TiposError.SESION_NO_FINALIZADA;
        }
    }

    /////////////////////////////////METODOS DE LISTA DE PUESTOS//////////////////////////////
    public ArrayList<CatalogoPuestos> getListaPuestos(){
        if(listaActividades.size()==0){
            listaPuestos = dbHandlerl.getCatalogoPuestos();
            if(listaPuestos.size()==0){
                CatalogoPuestos catalogoPuestos = new CatalogoPuestos("SELECCIONE PUESTO");
                dbHandlerl.addCatalogoPuestos(catalogoPuestos);
                listaPuestos.add(catalogoPuestos);
                catalogoPuestos = new CatalogoPuestos("NO TRABAJO");
                dbHandlerl.addCatalogoPuestos(catalogoPuestos);
                listaPuestos.add(catalogoPuestos);
            }
        }

        return listaPuestos;
    }

    public CatalogoPuestos getListaPuestos(Long id){
        CatalogoPuestos catalogoPuestos = null;
        for (CatalogoPuestos p : listaPuestos ) {
            if(p.getId()==id){
                System.out.println("Puestos encontrado "+p.toString());
                return  p;
            }
        }

        catalogoPuestos = dbHandlerl.getCatalogoPuestos(id);
        System.out.println("buscar en db "+catalogoPuestos);

        return catalogoPuestos;
    }

    public TiposError setCategoriaPuestos(CatalogoPuestos catalogoPuestos){
        TiposError r= TiposError.ERROR_DB;
        if(settings==null) {
            Log.e("MSN",catalogoPuestos.validarCamposVacios()+"  ----");
            r = TiposError.ERROR_SETTING_NULL;
        }else{
            if(catalogoPuestos.validarCamposVacios()){
                Long i = dbHandlerl.addCatalogoPuestos(catalogoPuestos);
                if(i!=Long.valueOf(-1)){
                    //catalogoPuestos.setId(i);
                    this.listaPuestos.add(catalogoPuestos);
                    r = TiposError.EXITOSO;
                }
            }else{
                r = TiposError.ERROR_CAMPOS_VACIOS;
            }
        }
        return r;
    }
/////////////////////////////////METODOS DE PUESTOS DE TRABAJADORES//////////////////////////////

    public TiposError cambiarPuesto(Integer consecutivo,CatalogoPuestos catalogoPuesto,String horaCambio){
        TiposError r= TiposError.ERROR_DB;

        for (Trabajadores t: listaTrabajadores) {
            if(t.getConsecutivo()==consecutivo){
                if(catalogoPuesto.getId()>1){
                    if(t.getPuestosActual().getId() != catalogoPuesto.getId()){
                        try {
                            long cambio = horaCambio.equals("")?new Date().getTime(): Complementos.convertirStringAlong(Complementos.obtenerFechaString(new Date()),horaCambio);
                            Puestos p = dbHandlerl.getUltimoPuesto(settings.getFechaString(),t);

                            if(p!=null){
                                if(p.getDateInicio()<cambio){
                                    p.setDateFin(cambio);
                                    int i = dbHandlerl.updatePuestos(p);
                                    if(i!= -1 ){
                                        r = TiposError.EXITOSO;
                                    }
                                }else{
                                    r =TiposError.ERROR_HORA_FINAL;
                                }

                            }else{
                                if(settings.getFechaInicio()<cambio){
                                    p = new Puestos(settings.getFechaInicio(),cambio,t,0,t.getPuestosActual());
                                    Long i = dbHandlerl.addPuesto(p);
                                    p.setId(i);

                                    if(i!=-1){
                                        r = TiposError.EXITOSO;
                                    }
                                }else{
                                    r =TiposError.ERROR_HORA_FINAL;
                                }
                            }

                            if(r==TiposError.EXITOSO){

                                Trabajadores tanterior = new Trabajadores(t);
                                t.setPuestosActual(catalogoPuesto);

                                Puestos p1 = new Puestos(p.getDateFin(),Long.valueOf(0),t,0,catalogoPuesto);
                                Long j = dbHandlerl.addPuesto(p1);

                                if(j==-1){
                                    r = TiposError.ERROR_DB;
                                }

                                if(r==TiposError.EXITOSO){
                                    r = updateTrabajadores(t,tanterior);
                                }
                            }

                        } catch (ParseException e) {
                            r = TiposError.ERROR_CONVERSION_FECHA;
                        }

                    }else{
                        r = TiposError.SIN_CAMBIOS;
                    }
                }else{
                    r = TiposError.ERROR_SELECCION_PUESTO;
                }
                break;
            }
        }

        return r;
    }

    public TiposError actualizarUltimoPuesto(Trabajadores trabajador){
        TiposError e = TiposError.EXITOSO;

        Puestos puestosTrabajador = dbHandlerl.getUltimoPuesto(settings.getFechaString(), trabajador);

        if(puestosTrabajador!=null){
            puestosTrabajador.setPuestosActual(trabajador.getPuestosActual());
            int i = dbHandlerl.updatePuestos(puestosTrabajador);

            if(i==-1)
                e = TiposError.ERROR_DB;
        }else{
            e = TiposError.SIN_CAMBIO_DE_PUESTO;
        }

        return e;
    }

    public ArrayList<Puestos> getPuestosTrabajador(Trabajadores trabajador){
        ArrayList<Puestos> puestosTrabajador = dbHandlerl.getPuestosTrabajador(settings.getFechaString(), trabajador);

        if(puestosTrabajador.size()==0){
            puestosTrabajador.add(new Puestos(settings.getFechaInicio(),settings.getFechaFinal(),trabajador,0,trabajador.getPuestosActual()));
        }

        return puestosTrabajador;
    }

    public TiposError updatePuesto(Puestos cambioPuesto, Puestos puestoAnterio){
        TiposError respuesta = TiposError.EXITOSO;
        Long oi;
        Long of;
        Long hi;
        Long hf;

        if(!cambioPuesto.getPuestos().getDescripcion().equals(puestoAnterio.getPuestos().getDescripcion())
                || !cambioPuesto.getHoraInicioString().equals(puestoAnterio.getHoraInicioString())
                || !cambioPuesto.getHoraFinalString().equals(puestoAnterio.getHoraFinalString())){

            if(!cambioPuesto.getHoraInicioString().equals(puestoAnterio.getHoraInicioString()) || !cambioPuesto.getHoraFinalString().equals(puestoAnterio.getHoraFinalString())){
                Long horaActual = new Date().getTime();

                hi = cambioPuesto.getDateInicio();
                hf = cambioPuesto.getDateFin()==0?horaActual:cambioPuesto.getDateFin();


                if(hi<hf){//hora inicio del puesto no puede ser mayor o igual a la hora final del puesto
                    //hora de inicio del puesto no puede ser menor que la hora de inicio de jornada y la hora final del puesto no debe ser mayor que la hora actual
                    if(hi>=settings.getFechaInicio() && hf<=horaActual ){
                        for (Puestos o : getPuestosTrabajador(cambioPuesto.getTrabajadorObjec())) {
                            oi = o.getDateInicio();
                            of = o.getDateFin()==0?horaActual:o.getDateFin();


                            if(o.getId()!=cambioPuesto.getId()){//no se debe de comparar el mismo puesto
                                Log.i("verificacionHora",o.toString());
                                if(hi<=oi && hf > oi){  //hi|------|oi|------|hf|------|of
                                    //hora final nueva interviene en otro puesto
                                    respuesta = TiposError.ERROR_SELECCION_HORA;
                                    Log.i("verificacionHora","hora final nueva interviene en otro puesto "+ hi+" <= "+oi+"=="+(hi<=oi) +" YY "+ hf +" > "+ oi+"=="+(hf>oi));
                                    break;
                                }else if(hi<of && hf>= of){   //oi|------|hi|------|of|------|hf
                                    //hora inicial nueva interviene en otro puesto
                                    respuesta = TiposError.ERROR_SELECCION_HORA;
                                    Log.i("verificacionHora","hora inicial nueva interviene en otro puesto "+ hi+" < "+of+"=="+(hi<of) +" YY "+ hf +" >= "+ of+"=="+(hf>=of));
                                    break;
                                }else if(hi>=oi && hf<=of){//oi|------|hi|------|hf|------|of
                                    //hora inicial y final nuevas intervien en otro puesto
                                    respuesta = TiposError.ERROR_SELECCION_HORA;
                                    Log.i("verificacionHora","hora inicial y final nuevas intervien en otro puesto "+hi+" >= "+oi+"=="+(hi>=oi) +" YY "+hf +" <= "+ of+"=="+(hf<=of));
                                    break;
                                }else if(hi<=oi && hf>=of){//hi|------|oi|------|of|------|hf
                                    //hora inicio y final nuevas afecta a otro puesto
                                    respuesta = TiposError.ERROR_SELECCION_HORA;
                                    Log.i("verificacionHora","hora inicio y final nuevas afecta a otro puesto "+hi+"<="+oi+"=="+(hi<=oi)+" YY "+hf+">="+of+"=="+(hf>=of));
                                    break;
                                }
                            }
                        }
                    }else{
                        Log.i("verificacionHora","hora inicio no mayor que inicio sesion y hora fin no es menor que hora actual "
                                + hi+">="+settings.getFechaInicio() +"=="+ (hi>=settings.getFechaInicio())
                                +" yy "+ hf+"<="+horaActual +"=="+ (hf<=horaActual));
                        respuesta = TiposError.ERROR_SELECCION_HORA;
                    }
                }else{
                    Log.i("verificacionHora","hora inicio no es menor que hora final "+hi+" < "+hf + "=="+ (hi < hf ));
                    respuesta = TiposError.ERROR_SELECCION_HORA;
                }
            }
        }else{
            respuesta = TiposError.SIN_CAMBIOS;
        }

        if(respuesta == TiposError.EXITOSO){
            Log.i("verificacionHora","cambios en el puesto "+cambioPuesto.toString());

            if(cambioPuesto.getId()==null){
                Log.i("verificacionHora","actualziar solo el puesto actual del trabajador");
                Trabajadores trabajador = new Trabajadores(cambioPuesto.getTrabajadorObjec());
                trabajador.setPuestosActual(cambioPuesto.getPuestos());

                Trabajadores trabajadorA = new Trabajadores(puestoAnterio.getTrabajadorObjec());
                trabajador.setPuestosActual(cambioPuesto.getPuestos());
                //cambioPuesto.setPuestosActual(cambioPuesto.getPuestos());
                //puestoAnterio.setPuestosActual(puestoAnterio.getPuestos());
                respuesta = updateTrabajadores(trabajador,trabajadorA);
                trabajador = null;
                trabajadorA = null;


            }else if(cambioPuesto.getDateFin()==0){
                Log.i("verificacionHora","actualziar el puesto actual y puestos del trabajador");
                respuesta = dbHandlerl.updatePuestos(cambioPuesto)==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
                if(respuesta == TiposError.EXITOSO){
                    Trabajadores trabajador = new Trabajadores(cambioPuesto.getTrabajadorObjec());
                    trabajador.setPuestosActual(cambioPuesto.getPuestos());

                    Trabajadores trabajadorA = new Trabajadores(puestoAnterio.getTrabajadorObjec());
                    trabajador.setPuestosActual(cambioPuesto.getPuestos());
                    //cambioPuesto.setPuestosActual(cambioPuesto.getPuestos());
                    //puestoAnterio.setPuestosActual(puestoAnterio.getPuestos());
                    respuesta = updateTrabajadores(trabajador,trabajadorA);
                    trabajador = null;
                    trabajadorA = null;

                }
            }else{
                respuesta = dbHandlerl.updatePuestos(cambioPuesto)==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
            }
        }

        return respuesta;
    }

    public TiposError updateNombreTrabajadorPuesto(Trabajadores trabajadores){

        int i = dbHandlerl.updateNombreTrabajadorPuestos(trabajadores);

        TiposError respuesta = i==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
        /*ArrayList<Puestos> list = getPuestosTrabajador(trabajadores);
        Log.i("lista",list.size()+" ");

        for (Puestos p:list) {
            if(p.getId()!=null){
                Log.i("lista",p.toString()+" ");
                p.setNombre(trabajadores.getNombre());
                p.setApellidoPaterno(trabajadores.getApellidoPaterno());
                p.setApellidoMaterno(trabajadores.getApellidoMaterno());

                int i = dbHandlerl.updatePuestos(p);
                Log.i("lista",i+" ");
                respuesta = i==-1?TiposError.ERROR_DB:TiposError.EXITOSO;
                if(respuesta == TiposError.ERROR_DB)
                    break;
            }
        }*/

        return respuesta;
    }

    public TiposError updateNombreTrabajadorProduccion(Trabajadores trabajador){
        TiposError respuesta;
        int i = dbHandlerl.updateNombreTrabajadorProduccion(trabajador);

        respuesta = i==-1?TiposError.ERROR_DB:TiposError.EXITOSO;

        return respuesta;
    }

/////////////////////////////////METODOS DE LISTA DE TAMAÑOS DE CAJAS//////////////////////
    public ArrayList<CatalogoCajas> getListaTamaiosCajas(){

        if(listaTamaiosCajas.size()==0){
             listaTamaiosCajas = dbHandlerl.getCatalogoTamaniosCajas();
            if(listaTamaiosCajas.size()==0){
                CatalogoCajas catalogoCajas = new CatalogoCajas("SELECCIONE TAMAÑO DE CAJA",0);
                dbHandlerl.addCatalogoCajas(catalogoCajas);
                listaTamaiosCajas.add(catalogoCajas);
            }
        }
        return listaTamaiosCajas;
    }

    public CatalogoCajas getListaTamaiosCajas(Long id){
        CatalogoCajas catalogoCajas = null;
        for (CatalogoCajas a : listaTamaiosCajas ) {
            if(a.getId()==id){
                return a;
            }
        }

        catalogoCajas = dbHandlerl.getCatalogoTamaniosCajas(id);

        return catalogoCajas;
    }

    public CatalogoCajas getListaTamaiosCajas(int posicion){
        CatalogoCajas catalogoCajas = null;
        if(settings!=null) {
            if(listaTamaiosCajas.size()==0)
                getListaTamaiosCajas();

            catalogoCajas = listaTamaiosCajas.get(posicion);
        }

        return catalogoCajas;
    }

    public CharSequence[] getListaTamaiosCajasSequences(){
        CharSequence[] cajas = new CharSequence[listaTamaiosCajas.size()];

        for (int i=0;i<listaTamaiosCajas.size();i++) {
            cajas[i] = listaTamaiosCajas.get(i).toString();
        }

        return  cajas;
    }

    public int getIndexListaTamanioCajas(CatalogoCajas catalogoCajas){
        int index = 0;

        for (index=0;index<listaTamaiosCajas.size();index++) {
            if(listaTamaiosCajas.get(index).getId()==catalogoCajas.getId())
                break;
        }

        return  index;
    }

    public TiposError setCatalogoTamanioCajas(CatalogoCajas catalogoCajas){
        TiposError r= TiposError.ERROR_DB;
        if(settings!=null) {
            if(catalogoCajas.validarCamposVacios()){
                Long i = dbHandlerl.addCatalogoCajas(catalogoCajas);
                if(i!=Long.valueOf(-1)){
                    //catalogoPuestos.setId(i);
                    this.getListaTamaiosCajas().add(catalogoCajas);
                    r = TiposError.EXITOSO;
                }
            }else{
                r = TiposError.ERROR_CAMPOS_VACIOS;
            }

        }else{
            r = TiposError.ERROR_SETTING_NULL;
        }
        return r;
    }
    ////////////////////////////////METODOS DE ACTIVIDADES/////////////////////////////////////
    public ArrayList<CatalogoActividades> getListaActividades(){

         if(listaActividades.size()==0){
             listaActividades = dbHandlerl.getCatalogoActividades();

             if(listaActividades.size()==0){
                 CatalogoActividades catalogoActividades = new CatalogoActividades("SELECCIONE ACTIVIDAD",listaTamaiosCajas.get(0));

                 dbHandlerl.addCatalogoActividades(catalogoActividades);
                 listaActividades.add(catalogoActividades);
             }
         }
         return listaActividades;
    }

    public CatalogoActividades getListaActividades(Long id){
        CatalogoActividades catalogoActividades = null;
        for (CatalogoActividades a : listaActividades ) {
            if(a.getId()==id){
                return a;
            }
        }

        catalogoActividades = dbHandlerl.getcatalogoActividades(id);

        return catalogoActividades;
    }

    public CatalogoActividades getListaActividades(int posicion){
        CatalogoActividades catalogoActividades = null;
        if(settings!=null) {
            if(listaActividades.size()==0)
                getListaActividades();

            catalogoActividades = listaActividades.get(posicion);
        }

        return catalogoActividades;
    }

    public CharSequence[] getListaActividadesArray(){
        CharSequence[] actividades = new CharSequence[listaActividades.size()];

        for (int i=0;i<listaActividades.size();i++) {
            actividades[i] = listaActividades.get(i).toString();
        }

        return  actividades;
    }

    public int getIndexListaActividad(CatalogoActividades actividades){
        int index = 0;

        for (index=0;index<listaActividades.size();index++) {
            if(listaActividades.get(index).getId()==actividades.getId())
                break;
        }

        return  index;
    }

    public TiposError setCatalogoActividades(CatalogoActividades catalogoActividades){
        TiposError r= TiposError.ERROR_DB;
        if(settings!=null) {
            if(catalogoActividades.validarCamposVacios()){
                Long i = dbHandlerl.addCatalogoActividades(catalogoActividades);
                if(i!=Long.valueOf(-1)){
                    //catalogoPuestos.setId(i);
                    this.getListaActividades().add(catalogoActividades);
                    r = TiposError.EXITOSO;
                }
            }else{
                r = TiposError.ERROR_CAMPOS_VACIOS;
            }

        }else{
            r = TiposError.ERROR_SETTING_NULL;
        }
        return r;
    }

    public TiposError cambiarActividad(CatalogoActividades catalogoActividades){
        TiposError r= TiposError.ERROR_DB;
        settings.setActividades(catalogoActividades);
        r = updateSetting(settings);
        return r;
    }

    public String getActividadActual(){
        String r= "";

        if(settings!=null)
            r = settings.getActividades().getDescripcion();
        else
            r ="SIN ACTIVIDAD";

        return r;
    }

    public CatalogoActividades getActividad(){
        CatalogoActividades r= null;

        if(settings!=null)
            r = settings.getActividades();

        return r;
    }
/////////////////////////////////METODOS DEL TRABAJADOR//////////////////////////////
    public ArrayList<Trabajadores> getListaTrabajadores(){
        Trabajadores r=null;
        if(settings!=null) {
            if(listaTrabajadores.size()==0)
                listaTrabajadores = dbHandlerl.getTrabajdores();

        }

        return listaTrabajadores;
    }

    public TiposError setTrabajador(Trabajadores trabajadores){

        Log.e("new trabajador","controlador /*//**-/-*/-*/*-/*-/*-/*-/-");
        TiposError r= TiposError.ERROR_DB;
        if(settings!=null) {
            if(trabajadores.validarCamposVacios()){
                Long i = dbHandlerl.addTrabajadores(trabajadores);
                Log.e("new trabajador","ya se guardo en la db "+i);
                if(i!=Long.valueOf(-1)){
                    Log.e("new trabajador","actualizar arraylist" +trabajadores.toString());
                    //trabajadores.setIdTrabajdor(i);
                    listaTrabajadores.add(trabajadores);//se agrega al arrayList
                    r = TiposError.EXITOSO;
                }
            }else{
                r = TiposError.ERROR_CAMPOS_VACIOS;
            }
        }else{
            r = TiposError.ERROR_SETTING_NULL;
        }
        return r;
    }

    public Trabajadores getTrabajador(Long id){
        Trabajadores r=null;
        if(settings!=null) {
            r = dbHandlerl.getTrabajdores(id);
        }
        return r;
    }

    public Trabajadores getTrabajador(String consecutivo){
        Trabajadores r=null;
        Log.i("captura","---------------"+consecutivo+"--------");
        Log.i("captura",settings.toString());
        if(settings!=null) {
            if(!consecutivo.equals("")){
                Integer c = Integer.parseInt(consecutivo);
                Log.i("captura",c.toString());
                for (Trabajadores t :
                        listaTrabajadores) {
                    if (t.getConsecutivo() == c){
                        r = t;
                        break;
                    }
                }

                if(r==null){
                    r = dbHandlerl.getTrabajdores(c);
                }
            }
        }
        return r;
    }

    public Trabajadores getTrabajador(int posicion){
        Trabajadores r=null;

        if(settings!=null) {
            if(listaTrabajadores.size()==0)
                getListaTrabajadores();

            r = listaTrabajadores.get(posicion);


        }

        return r;
    }

    public TiposError updateTrabajadores(Trabajadores trabajadores,Trabajadores trabajadorAnterior){
        Log.i("verificacionHora",trabajadores.getIdTrabajdor()+"--"+trabajadores.getPuestosActual().getDescripcion()+"---"+trabajadorAnterior.getPuestosActual().getDescripcion());
        TiposError r= TiposError.ERROR_DB;
        if(trabajadores.validarCamposVacios()){
            if(trabajadorAnterior.validarCambios(trabajadores)){
                if(settings!=null) {
                    int i = dbHandlerl.updateTrabajdores(trabajadores);
                    if(i!=-1){

                        for (int index=0 ; index<listaTrabajadores.size();index++) {
                            if(trabajadores.getIdTrabajdor()==listaTrabajadores.get(index).getIdTrabajdor()){
                                listaTrabajadores.set(index,trabajadores);
                                r = TiposError.EXITOSO;
                                break;
                            }
                        }

                        //r = actualizarUltimoPuesto(trabajadores);
                    }
                }else{
                    r = TiposError.ERROR_SETTING_NULL;
                }
            }else{
             r = TiposError.SIN_CAMBIOS;
            }
        }else{
            r = TiposError.ERROR_CAMPOS_VACIOS;
        }

        return r;
    }

    public String totalAsistencia(){
        Integer i = listaTrabajadores.size();

        for (Trabajadores t : listaTrabajadores) {
            if(t.getPuestosActual().getId()==2)
                i--;
        }

        return i.toString();
    }
/////////////////////////////////METODOS DE PRODUCCION//////////////////////////////
    public TiposError addProduccion(Producto produccion){
        TiposError resultado = TiposError.EXITOSO;
        if(produccion!=null){
            resultado = validarCapturaProduccion(produccion);
            Log.i("EMAIL",resultado.name());
            if(resultado==TiposError.EXITOSO){
                Long r = dbHandlerl.addProduccion(produccion);
                if(r==-1)
                    resultado = TiposError.ERROR_DB;
            }
        }else{
            resultado = TiposError.ERROR_CAMPOS_VACIOS;
        }
        return resultado;
    }

    public TiposError deleteProduccion(Producto producto) {
        TiposError tiposError = TiposError.EXITOSO;
        Boolean update = false;

        ArrayList<Producto> produccion = getRegistrosTrabajador(producto,settings.getFechaString());


            if(produccion.size()>0){
                for (Producto p : produccion) {
                    if(producto.getAgranel()>0 || producto.getSegunda()>0 || producto.getPrimera()>0){
                        if(p.getPrimera()>0 && producto.getPrimera()>0){
                            Integer[] r = quitarProduccion(p.getPrimera(),producto.getPrimera());
                            p.setPrimera(r[0]);
                            producto.setPrimera(r[1]);
                            update = true;
                        }
                        if(p.getSegunda()>0 && producto.getSegunda()>0){
                            Integer[] r = quitarProduccion(p.getSegunda(),producto.getSegunda());
                            p.setSegunda(r[0]);
                            producto.setSegunda(r[1]);
                            update = true;
                        }
                        if(p.getAgranel()>0 && producto.getAgranel()>0){
                            Integer[] r = quitarProduccion(p.getAgranel(),producto.getAgranel());
                            p.setAgranel(r[0]);
                            producto.setAgranel(r[1]);
                            update = true;
                        }

                        if(update){
                            int i = dbHandlerl.updateProduccion(p);
                            if(i==-1){
                                tiposError = TiposError.ERROR_DB;
                                break;
                            }else{
                                update =false;
                            }

                        }
                    }else{
                        break;
                    }
                }
            }

        return tiposError;
    }

        private Integer[] quitarProduccion(Integer totalModificar,Integer totalQuitar){
        Integer respuesta[] = new Integer[2];
        int compare = Integer.compare(totalModificar, totalQuitar);
        if(compare==0){
            respuesta[0] =0;
            respuesta[1] =0;
        }else if (compare<0){
            respuesta[0] =0;
            respuesta[1] =totalQuitar-totalModificar;
        }else if(compare>0){
            respuesta[0] =totalModificar- totalQuitar;
            respuesta[1] =0;
        }

        return respuesta;
    }

    public ArrayList<Producto> getRegistrosTrabajador(Trabajadores trabajador,String fecha){
        ArrayList<Producto> registros = new ArrayList<Producto>();
        registros = dbHandlerl.getResgistrosProduccion(trabajador,fecha);

        return registros;
    }

    public ReporteProduccion getTotalProduccion(Trabajadores trabajador, CatalogoActividades actividad){
        if(trabajador!=null)
            return dbHandlerl.getTotalProduccionTrabajador(trabajador,actividad,settings.getFechaString());
        else
            return null;
    }

    public TiposError validarCapturaProduccion(Producto producto){
        TiposError resultado = TiposError.EXITOSO;

        if(producto.getPrimera()<=0 &&producto.getSegunda()<=0 && producto.getAgranel()<=0)
            resultado = TiposError.ERROR_CAMPOS_VACIOS;

        return resultado;
    }

    public Producto getUltimaProduccion(Trabajadores trabajadores){
        Producto producto = dbHandlerl.getUltimoResgistroProduccion(trabajadores,settings.getFechaString());

        return  producto;
    }

    public TiposError validarPuestoProduccion(Trabajadores trabajadores){
        TiposError resultado = TiposError.EXITOSO;

        if(trabajadores!=null){
            if(trabajadores.getPuestosActual().getId()!=3){
                resultado = TiposError.ERROR_PUESTO_NO_VALIDO;
            }
        }else{
            resultado = TiposError.ERROR_TRABAJADOR_NO_VALIDO;
        }

        return resultado;
    }

    public TiposError validarPrecaptura(Trabajadores trabajadores,Long time){
        TiposError resultado = TiposError.EXITOSO;
Log.i("captura",getConfiguracion().toString());
        if(trabajadores!=null){
            resultado = validarPuestoProduccion(trabajadores);
            if(resultado==TiposError.EXITOSO){
                Producto ultimoProducto = getUltimaProduccion(trabajadores);

                if(ultimoProducto!=null){
                    Long dif =time-ultimoProducto.getDate();
                    Long segundos =(dif / 1000);
                    if(dif>=0){
                        if(segundos < getConfiguracion().getTiempoMas()){
                            resultado = TiposError.ERROR_TIEMPO_LIMITE;
                        }
                    }else{
                        resultado = TiposError.ERROR_HORA_CAPTURA_PRODUCCION;
                    }
                }
            }
        }else{
            resultado = TiposError.ERROR_TRABAJADOR_NO_VALIDO;
        }

        return resultado;
    }
/////////////////////////////////METODOS DE REPORTE//////////////////////////////
    public TreeMap<String,Object> getReporteDiaTrabajadores(){
        ArrayList<ReporteProduccion> reporteGeneral = dbHandlerl.getReporte(settings.getFechaString(), null);
        TreeMap<String,Object> reporte = new TreeMap<>();
        ArrayList<ReporteCajasTotales> reporteCajas = new  ArrayList<ReporteCajasTotales>();
        //ArrayList<ReporteCajasTotales> reporteTotal = new  ArrayList<ReporteCajasTotales>();
        Long trabajadorActual = Long.valueOf(0);

        ReporteCajasTotales registro = null;
        ReporteCajasTotales registroTotal = null;

        for (ReporteProduccion r : reporteGeneral) {


            if(r.getIdTrabajdor()==trabajadorActual){
                registro = reporteCajas.get(reporteCajas.size()-1);
            }else{
                registro = new ReporteCajasTotales(r);
                reporteCajas.add(registro);
                trabajadorActual = registro.getIdTrabajdor();
            }

            registro.setCajas(r.getCajasPrimera(),r.getCajasSegunda(),r.getCajasAgranel());
            registro.setVasquetes(r.getVazquetesPrimera(),r.getVazquetesSegunda(),r.getVazquetesAgranel());

            if(registroTotal==null){
                registroTotal = new ReporteCajasTotales(r);
            }

            registroTotal.setCajas(r.getCajasPrimera(),r.getCajasSegunda(),r.getCajasAgranel());
            registroTotal.setVasquetes(r.getVazquetesPrimera(),r.getVazquetesSegunda(),r.getVazquetesAgranel());
        }

        reporte.put("trabajadores",reporteCajas);
        reporte.put("totales",registroTotal);
        return reporte;
    }

    public  ArrayList<ReporteProduccion> getReporteDetalleTrabajador(Trabajadores trabajador){
        ArrayList<ReporteProduccion> reporte = dbHandlerl.getReporte(settings.getFechaString(),trabajador);
        return reporte;
    }

    public  ArrayList<ReporteProduccion> getReporte(Long fechaInicial,Long fechaFinal){
        ArrayList<Trabajadores> arrayListTrabajadores = dbHandlerl.getTrabajdores();
        ArrayList<ReporteProduccion> reporte = new ArrayList<ReporteProduccion>();

        for (Trabajadores t : arrayListTrabajadores) {
            ArrayList<Puestos> puestosTrabajador = dbHandlerl.getPuestos(t,fechaInicial,fechaFinal);
            for (Puestos p : puestosTrabajador) {
                if(p.getPuestos().getId()==3){
                    ArrayList<ReporteProduccion> r = dbHandlerl.getResgistrosProduccion(t,p);
                    if(r.size()==0){
                        ReporteProduccion produccion = new ReporteProduccion(t);
                        produccion.setPuestos(p);
                        reporte.add(produccion);
                    }

                    for (ReporteProduccion rp : r) {
                        reporte.add(rp);
                    }
                }else{
                    ReporteProduccion produccion = new ReporteProduccion(t);
                    produccion.setPuestos(p);
                    reporte.add(produccion);
                }

            }
        }


        return reporte;
    }

    public static final String calcularCajas(Integer cajas, Integer vasquetes, Boolean tipoCaja){

        String cajasTotales = "";
        String leyendaCaja;
        String letendaVasquetes;

        if(tipoCaja){
            leyendaCaja = " caja";
            letendaVasquetes = " basquet";
        }
        else{
            leyendaCaja = " jarra";
            letendaVasquetes = "";
        }
        if(cajas>0){
            cajasTotales = cajas +leyendaCaja;//" caja";
            if(cajas>1){
                cajasTotales= cajasTotales+"s";
            }
        }

        if(cajas>0 && vasquetes>0)
            cajasTotales= cajasTotales+" y ";

        if(vasquetes>0){
            cajasTotales= cajasTotales+vasquetes+letendaVasquetes;//" basquet";
            if (vasquetes>1){
                cajasTotales= cajasTotales+"s";
            }
        }
        //Log.i("reporte",cajasTotales+"----"+cajas+"---"+vasquetes);
        return cajasTotales;
    }

    public static final float getCajas(Integer totalVasquetes,Integer vasqetesXcajas){
        return  ((float)totalVasquetes/(float)vasqetesXcajas);
    }

    public TiposError enviarCorreo(String fechaInicio,String fechaFinal){
        File file = null;
        Log.i("EMAIL",fechaInicio+"----"+fechaFinal);
        TiposError error = TiposError.EXITOSO;
        if(!fechaFinal.equals("")&&!fechaInicio.equals("")){
            try {
                file = crearArchivoXlsx(fechaInicio,fechaFinal);

                if(file!=null){
                    Uri uri = Uri.fromFile(file);

                    String [] to = {configuracion.getPara()};
                    String [] cc = {configuracion.getCc()};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("application/vnd.ms-excel");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
                    emailIntent.putExtra(Intent.EXTRA_CC,cc);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, configuracion.getAsunto()+" de "+ fechaInicio+" a "+fechaFinal);
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,configuracion.getMensaje());
                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    try {
                        if (emailIntent.resolveActivity(this.activity.getPackageManager()) != null) {
                            this.activity.startActivity(Intent.createChooser(emailIntent, "Enviar email."));
                            Log.i("EMAIL", "Enviando email...");
                        }else{
                            Log.i("EMAIL", "ERRRO email...");
                        }
                    }
                    catch (android.content.ActivityNotFoundException e) {
                        //Toast.makeText(this, "NO existe ningún cliente de email instalado!.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.i("EMAIL",e.getMessage());
                        error = TiposError.ERROR_ENVIO_CORREO;
                    }
                }else{
                    error = TiposError.ERROR_CREACION_ARCHIVO;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                error = TiposError.ERROR_CONVERSION_FECHA;
            }
        }else{
            error = TiposError.ERROR_CONVERSION_FECHA;
        }
        return error;
    }

    private File crearArchivoXlsx(String fechaInicio,String fechaFinal) throws ParseException {
        File archivoXls = null;
        int numeroRenglon = 0;
        int numeroCelda = 0;
        Integer sumPrimera=0;
        Integer sumSegunda=0;

        Float sumCajasPrimera=0.0f;
        Float sumCajasSegunda=0.0f;
        Float sumCajasAgranel=0.0f;

        Long fi = Complementos.convertirStringAlong(fechaInicio,"00:00:00:0000");
        Long ff = Complementos.convertirStringAlong(fechaFinal,"23:59:59:0000");
        String key ="";

        HSSFWorkbook  workbook = new HSSFWorkbook ();
        HSSFSheet  sheet = workbook.createSheet("Produccion");

        String [] header = new String[]{"Fecha","Consecutivo","Trabajador","Puesto","Actividad","Total horas","Basquet primera","Basquet segunda","Primera cajas","segunda cajas","Agranel"};


        ArrayList<ReporteProduccion> reporte = getReporte(fi,ff);

        Row row = sheet.createRow(numeroRenglon++);

        for (String obj : header) {
            Cell cell = row.createCell(numeroCelda++);
            cell.setCellValue(obj);
        }

        for (ReporteProduccion rp : reporte) {

            Object[] array = rp.getArray();
            /*if(key.equals("")){
               key =  rp.getPuestos().getFechaString()+""+rp.getConsecutivo();
            }

            if(!key.equals(rp.getPuestos().getFechaString()+""+rp.getConsecutivo())){
                row = sheet.createRow(numeroRenglon++);
                numeroCelda = 0;

                Cell c = row.createCell(numeroCelda++);
                c.setCellValue(rp.getPuestos().getFechaString());
                c = row.createCell(numeroCelda++);
                c.setCellValue("TOTAL:");
                c = row.createCell(numeroCelda++);
                c.setCellValue("");
                c = row.createCell(numeroCelda++);
                c.setCellValue("");
                c = row.createCell(numeroCelda++);
                c.setCellValue("");
                c = row.createCell(numeroCelda++);
                c.setCellValue("");

                c = row.createCell(numeroCelda++);
                c.setCellValue(sumPrimera);
                c = row.createCell(numeroCelda++);
                c.setCellValue(sumSegunda);

                c = row.createCell(numeroCelda++);
                c.setCellValue(sumCajasPrimera);
                c = row.createCell(numeroCelda++);
                c.setCellValue(sumCajasSegunda);
                c = row.createCell(numeroCelda++);
                c.setCellValue(sumCajasAgranel);

                key =  rp.getPuestos().getFechaString()+""+rp.getConsecutivo();

                sumPrimera =0;
                sumSegunda=0;

                sumCajasPrimera= Float.valueOf(0);
                sumCajasSegunda=Float.valueOf(0);
                sumCajasAgranel=Float.valueOf(0);
            }else{
                sumPrimera = sumPrimera + ((Integer) array[6]);
                sumSegunda = sumSegunda + ((Integer) array[7]);

                sumCajasPrimera = sumCajasPrimera + ((Float) array[8]);;
                sumCajasSegunda = sumCajasSegunda + ((Float) array[9]);;
                sumCajasAgranel = sumCajasAgranel + ((Float) array[10]);
            }*/


            row = sheet.createRow(numeroRenglon++);
            numeroCelda = 0;
            for (Object o : array) {
                Cell cell = row.createCell(numeroCelda++);
                if(o instanceof String)
                    cell.setCellValue((String) o);
                else if(o instanceof Integer)
                    cell.setCellValue((Integer) o);
                else if(o instanceof Long)
                    cell.setCellValue((Long) o);
                else if(o instanceof Float)
                    cell.setCellValue((Float) o);


            }


        }

        row = sheet.createRow(numeroRenglon++);
        numeroCelda = 0;
        try {
            //Se genera el documento
            archivoXls = new File(Complementos.rutaAlmacenamiento(this.activity).getAbsolutePath()+File.separator+"doc"+File.separator+"Produccion.xls");

            if (!archivoXls.getParentFile().exists()) {
                archivoXls.getParentFile().mkdirs();
            }

            FileOutputStream out = new FileOutputStream(archivoXls);
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return archivoXls;
    }

    public static void openLog(Context context,String TAG){

        File storageDir = Complementos.rutaAlmacenamiento(context);


        String name = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());

        String logFile = storageDir.getAbsolutePath() + File.separator+ "Log" + File.separator + name + ".log";
        File result = new File(logFile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        FileLog.open(logFile, Log.VERBOSE,3000000);
        FileLog.v(TAG,"INICIALIZAR EL LOG");

    }

}
