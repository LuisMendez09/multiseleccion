package com.luis.corte.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;

import java.util.Timer;
import java.util.TimerTask;

public class Cortina extends AppCompatActivity {
    Controlador controlador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cortina);
        Controlador.openLog(this,Complementos.TAG_INICIO);
        FileLog.i(Complementos.TAG_INICIO,"iniciar la app");
        controlador = new Controlador(this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = null;
                // Comienza la proxima actividad
                Controlador.TiposError validacionSesion = controlador.validarInicioSesion();

                if(validacionSesion==Controlador.TiposError.SESION_INICIADA || validacionSesion==Controlador.TiposError.SESION_FINALIZADA)
                    mainIntent = new Intent().setClass(Cortina.this, Produccion.class);
                else if (validacionSesion==Controlador.TiposError.SESION_NO_FINALIZADA){
                    controlador.finalizarSesion();
                    controlador.ReiniciarSession();
                    mainIntent = new Intent().setClass(Cortina.this, MainActivity.class);
                }else if (validacionSesion==Controlador.TiposError.SESION_REINICIAR){
                    controlador.ReiniciarSession();
                    mainIntent = new Intent().setClass(Cortina.this, MainActivity.class);
                }else if (validacionSesion==Controlador.TiposError.SESION_REINIADA){
                    mainIntent = new Intent().setClass(Cortina.this, MainActivity.class);
                }

                if(validacionSesion!=Controlador.TiposError.SESION_NO_VALIDA){
                    startActivityForResult(mainIntent,100);
                    // cierra la actividad
                    finish();
                }else{
                    Complementos.mensajesError(Cortina.this,validacionSesion);
                }

            }
        };

        // Tiempo de espera en el splash screen
        Timer timer = new Timer();
        timer.schedule(task, 1500);
    }
}
