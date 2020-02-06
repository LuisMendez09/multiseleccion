package com.luis.corte.complementos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.Spinner;
import android.widget.Toast;

import com.luis.corte.Controlers.Controlador;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Complementos {

    //TAG
    public static final String TAG_MAIN="MainActivity";
    public static final String TAG_INICIO="Cortina";
    public static final String TAG_BDHANDLER = "DBHandler";
    public static final String TAG_DIALOG_MODIFICAION_PUESTO = "DialogModificacionPuestos";
    public static final String TAG_CONTROLADOR = "Controlador";

    @SuppressLint("ObsoleteSdkInt")
    public  static File rutaAlmacenamiento(Context context){
        File storageDir = null;
        File[] temp2 = null;
        File temp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            temp2 = context.getExternalMediaDirs();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                temp2 = context.getExternalFilesDirs("");
            } else{
                temp = context.getExternalFilesDir("");
                if(temp == null)
                    temp = context.getDatabasePath("");
            }
        }

        if(temp != null){
            storageDir = temp;
        }

        if(temp2 != null){
            if(temp2[temp2.length-1]==null){
                storageDir = temp2[0];
            }else{
                storageDir = temp2[temp2.length - 1];
            }
        }

        return storageDir;
    }

    public static int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    public static void mensajesError(Activity activity, Controlador.TiposError tiposError){

        Log.e("MSN",tiposError.name());
        if(tiposError == Controlador.TiposError.ERROR_DB){
            Toast t = Toast.makeText(activity, "ERRROR AL GAURDAR EN DB" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if (tiposError == Controlador.TiposError.ERROR_SETTING_NULL){
            Toast t = Toast.makeText(activity, "ERRROR SETTING VACIO" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_SIN_TRABAJADORES){
            Toast t = Toast.makeText(activity, "AGREGE TRABAJADORES" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.SESION_NO_VALIDA){
            Toast t = Toast.makeText(activity, "ERROR AL INICIAR SESION" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_VALOR_NO_ENCONTRADO){
            Toast t = Toast.makeText(activity, "VALOR NO VALIDO" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_CONVERSION_FECHA){
            Toast t = Toast.makeText(activity, "ERROR AL COMVERTIR LA FECHA" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_HORA_FINAL){
            Toast t = Toast.makeText(activity, "HORA FINAL INCORRECTA" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.SIN_CAMBIOS){
            Toast t = Toast.makeText(activity, "NO SE ENCONTRARON CAMBIOS" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_SELECCION_PUESTO){
            Toast t = Toast.makeText(activity, "PUESTO SELECCIONADO NO VALIDO" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_SELECCION_HORA){
            Toast t = Toast.makeText(activity, "HORA SELECCIONADA INCORRECTA" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_PUESTO_NO_VALIDO){
            Toast t = Toast.makeText(activity, "EL TRABAJADOR NO TIENE EL PUESTO CORRECTO" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_TRABAJADOR_NO_VALIDO){
            Toast t = Toast.makeText(activity, "EL TRABAJADOR NO ES VALIDO" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_HORA_CAPTURA_PRODUCCION){
            Toast t = Toast.makeText(activity, "HORA DEL SISTEMA NO VALIDA" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_TIEMPO_LIMITE){
            Toast t = Toast.makeText(activity, "EL TRABAJADOR NO A SUPERADO EL TIEMPO LIMITE" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.ERROR_CAMPOS_VACIOS){
            Toast t = Toast.makeText(activity, "HAY CAMPO NO VALIDOS" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else if(tiposError == Controlador.TiposError.SESION_FINALIZADA){
            Toast t = Toast.makeText(activity, "LA SESION YA ESTA FINALIZADA" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
    }

    public static Long getDateTimeActual(){
        return new Date().getTime();
    }
    public static Date getDateActual(){
        return new Date();
    }

    public static Long convertirStringAlong(String fecha,String hora) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");
        return (sdf.parse(fecha+" "+hora)).getTime();
    }
/*
    public static Long convertirStringSegundosAlong(String fecha,String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Long time = (sdf.parse(fecha+" "+hora)).getTime();
        return time;
    }*/

    /***
     * obtener la fecha en formato dd/MM/yyyy
     * @param fecha tipo date
     * @return string con el ofrmato de fecha
     */
    public static String obtenerFechaString(Date fecha){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(fecha);
    }
}
