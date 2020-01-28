package com.luis.corte.views.adaptadores;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.models.ReporteCajasTotales;

import java.util.ArrayList;


public class ListaReporteAdapter extends ArrayAdapter<ReporteCajasTotales> {
    private ArrayList<ReporteCajasTotales> reporteProduccion;
    private Context context;
    private TextView tvConsecutivo;
    private TextView tvTrabajar;
    private TextView tvPrimera;
    private TextView tvSegunda;
    private TextView tvAgranel;


    private ReporteCajasTotales row;


    public ListaReporteAdapter(@NonNull Context context, ArrayList<ReporteCajasTotales> reporteProduccions) {
        super(context, R.layout.item_lista_reporte, reporteProduccions);
        this.reporteProduccion = reporteProduccions;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @android.support.annotation.Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.item_lista_reporte, parent, false);
        this.tvConsecutivo = (TextView) convertView.findViewById(R.id.tv_consecutivo_reporte);
        this.tvTrabajar =(TextView) convertView.findViewById(R.id.tv_trabajador_reporte);
        this.tvPrimera = (TextView) convertView.findViewById(R.id.tv_cajasPrimera_reporte);
        this.tvSegunda = (TextView) convertView.findViewById(R.id.tv_cajasSegunda_reporte);
        this.tvAgranel = (TextView) convertView.findViewById(R.id.tv_cajasAgranel_reporte);


        this.row = reporteProduccion.get(position);
        tvConsecutivo.setText(row.getConsecutivo().toString());
        tvTrabajar.setText(row.getTrabajador());
        tvPrimera.setText(Controlador.calcularCajas(row.getCajasPrimera(),row.getVasquetesPrimera(),true));
        tvSegunda.setText(Controlador.calcularCajas(row.getCajasSegunda(),row.getVasquetesSegunda(),true));
        tvAgranel.setText(Controlador.calcularCajas(row.getCajasAgranel(),row.getVasquetesAgranel(),false));

        if((position%2)==0)
            ((LinearLayout) convertView.findViewById(R.id.item_reporte)).setBackgroundColor(this.context.getResources().getColor(R.color.colorAccent));

        return convertView;
    }


}
