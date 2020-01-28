package com.luis.corte.views.adaptadores.holders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.luis.corte.R;
import com.luis.corte.models.Trabajadores;
import com.luis.corte.views.fragment.Cuadrilla;


public class TrabajadoresViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener {
    public TextView consecutivo;
    public TextView trabajador;
    public TextView puesto;

    private Class clase;
    private Trabajadores trabajadores;

    LongClickListener longClickListener;

    public TrabajadoresViewHolder(View v, Class c) {
        consecutivo = (TextView) v.findViewById(R.id.tv_consecutivo);
        trabajador = (TextView) v.findViewById(R.id.tv_trabajador);
        puesto = (TextView) v.findViewById(R.id.tv_puesto);


        v.setOnLongClickListener(this);
        v.setOnCreateContextMenuListener(this);

        this.clase = c;
    }

    public void setLongClickListener(LongClickListener longClickListener,Trabajadores trabajadores){
        this.longClickListener = longClickListener;
        this.trabajadores = trabajadores;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


        contextMenu.add(0,0,0,"EDITAR TRABAJADOR");

        if(this.clase.getSimpleName().equals(Cuadrilla.class.getSimpleName())){
            contextMenu.add(0,0,0,"PUESTOS REALIZADOS");
        }

        /*if(this.trabajadores.getPuestosActual().getId()!=2)
            contextMenu.add(0,0,0,"FALTA");*/
    }

    @Override
    public boolean onLongClick(View view) {
        this.longClickListener.OnItemLongClik();
        return false;
    }


}
