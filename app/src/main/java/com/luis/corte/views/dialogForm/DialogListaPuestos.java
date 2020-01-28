package com.luis.corte.views.dialogForm;

import android.app.AlertDialog;
//import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.MenuItem;
import android.view.View;
//import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.Puestos;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.adaptadores.ListaPuestosAdaptar;
import com.luis.corte.views.adaptadores.ListaTrabajadorAdapter;

import java.util.ArrayList;


public class DialogListaPuestos extends AlertDialog {
    private ArrayList<Puestos> puestos;
    private Controlador controlador;
    private int posicion1;
    private Trabajadores trabajadore;
    private ListaPuestosAdaptar listaPuestosAdaptar;
    private ListaTrabajadorAdapter listaTrabajadorAdapter;

    public DialogListaPuestos(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detalle_puestos);


        final ListView lv = (ListView) findViewById(R.id.lv_lista_puestos);
        listaPuestosAdaptar =new ListaPuestosAdaptar(this.getContext(),puestos);
        lv.setAdapter(listaPuestosAdaptar);

        TextView t = (TextView) findViewById(R.id.tv_trabajador_dialog);
        t.setText(trabajadore.getTrabajador());

        TextView c = (TextView) findViewById(R.id.tv_consecutivo_dialog);
        c.setText("Consecutivo: "+trabajadore.getConsecutivo());

        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(controlador.validarInicioSesion()!= Controlador.TiposError.SESION_FINALIZADA){
                    posicion1 = position;
                    DialogModificacionPuestos dmp = new DialogModificacionPuestos(DialogListaPuestos.this.getContext());
                    dmp.setControlador(controlador,puestos.get(position),listaPuestosAdaptar,listaTrabajadorAdapter);

                    dmp.show();
                }else{
                    Complementos.mensajesError(controlador.getActivity(),Controlador.TiposError.SESION_FINALIZADA);
                }
                //openContextMenu(view);
            }
        });

        ((Button) findViewById(R.id.btn_salir_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListaPuestos.this.dismiss();
            }
        });

    }

    public void setPuestos(ArrayList<Puestos> puestos, Trabajadores trabajador, Controlador controlador,ListaTrabajadorAdapter listaTrabajadorAdapter){
        this.puestos = puestos;
        this.trabajadore = trabajador;
        this.controlador = controlador;
        this.listaTrabajadorAdapter = listaTrabajadorAdapter;
    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),0,"Editar Puesto");
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(featureId== Window.FEATURE_CONTEXT_MENU)
            return onContextItemSelected(item);
        else
            return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onContextItemSelected( MenuItem item) {
        switch (item.getTitle().toString()){
            case "Editar Puesto":

                break;
        }
        return super.onContextItemSelected(item);
    }
*/
}
