package com.luis.corte.views.adaptadores.holders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.luis.corte.R;
import com.luis.corte.models.Puestos;

public class PuestosViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener {
    LongClickListener longClickListener;
    private Puestos puesto;

    public TextView tv_puesto;
    public TextView tv_horaInicio;
    public TextView tv_horaFin;

    public PuestosViewHolder(View v) {

        tv_puesto= (TextView) v.findViewById(R.id.tv_puesto_item);
        tv_horaInicio= (TextView) v.findViewById(R.id.tv_horaInicio_item);
        tv_horaFin= (TextView) v.findViewById(R.id.tv_horaFin_item);

        v.setOnLongClickListener(this);
        v.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,0,"EDITAR");
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.OnItemLongClik();
        return false;
    }

    public void setLongClickListener(LongClickListener longClickListener,Puestos puesto){
        this.longClickListener = longClickListener;
        this.puesto = puesto;
    }


}
