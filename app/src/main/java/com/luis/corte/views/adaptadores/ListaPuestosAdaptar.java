package com.luis.corte.views.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luis.corte.R;
import com.luis.corte.models.Puestos;
import com.luis.corte.views.adaptadores.holders.LongClickListener;
import com.luis.corte.views.adaptadores.holders.PuestosViewHolder;
import com.luis.corte.views.adaptadores.holders.TrabajadoresViewHolder;

import java.util.ArrayList;

public class ListaPuestosAdaptar extends ArrayAdapter<Puestos> {
    private  Context context;
    private ArrayList<Puestos> puestos;
    Integer posicion1;
    private Puestos puestoSeleccionado;
    private TextView tv_puesto;
    private TextView tv_horaInicio;
    private TextView tv_horaFin;



    public ListaPuestosAdaptar(@NonNull Context context, ArrayList<Puestos> puestos) {
        super(context, R.layout.item_lista_puestos,puestos);
        this.context = context;
        this.puestos = puestos;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.item_lista_puestos, parent, false);

        tv_puesto= (TextView) convertView.findViewById(R.id.tv_puesto_item);
        tv_horaInicio= (TextView) convertView.findViewById(R.id.tv_horaInicio_item);
        tv_horaFin= (TextView) convertView.findViewById(R.id.tv_horaFin_item);

        final Puestos puesto = this.puestos.get(position);
        tv_puesto.setText(puesto.getPuestos().getDescripcion());
        tv_horaInicio.setText(puesto.getHoraInicioString());
        tv_horaFin.setText(puesto.getHoraFinalString());

        return convertView;
    }


    public void updatePuesto(Puestos p){
        for (int i=0;i<this.puestos.size();i++) {
            if(p.getId()==this.puestos.get(i).getId()){
                this.puestos.set(i,p);
            }
        }
        notifyDataSetChanged();
    }
}
