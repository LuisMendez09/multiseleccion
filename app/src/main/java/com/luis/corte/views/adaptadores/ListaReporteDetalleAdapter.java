package com.luis.corte.views.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.models.ReporteProduccion;

import java.util.ArrayList;
import java.util.Date;


public class ListaReporteDetalleAdapter extends ArrayAdapter<ReporteProduccion> {
    private TextView tvActividad;
    private TextView tvPrimera;
    private TextView tvSegunda;
    private TextView tvAgranel;
    private TextView tvHoraCaptura;
    private Context context;

    private ArrayList<ReporteProduccion> reporteProduccion;


    public ListaReporteDetalleAdapter(@android.support.annotation.NonNull Context context, ArrayList<ReporteProduccion> reporteProduccion) {
        super(context,  R.layout.item_lista_detalle_reporte, reporteProduccion);
        this.reporteProduccion = reporteProduccion;
        this.context = context;
    }

    @Override
    public View getView(int position,@android.support.annotation.Nullable View convertView,@android.support.annotation.NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_lista_detalle_reporte, parent, false);

        this.tvActividad = (TextView) convertView.findViewById(R.id.tv_actividad_itemDetalleReporte);
        this.tvHoraCaptura = (TextView) convertView.findViewById(R.id.tv_horaCaptura_itemDetalleReporte);
        this.tvPrimera = (TextView) convertView.findViewById(R.id.tv_cajasPrimera_itemDetalleReporte);
        this.tvSegunda = (TextView) convertView.findViewById(R.id.tv_cajasSegunda_itemDetalleReporte);
        this.tvAgranel = (TextView) convertView.findViewById(R.id.tv_cajasAgranel_itemDetalleReporte);

        ReporteProduccion cajas = reporteProduccion.get(position);
        this.tvActividad.setText(cajas.getActividad().getDescripcion());
        this.tvHoraCaptura.setText("Hora: "+ Complementos.obtenerHoraString(new Date(cajas.getHora())));
        this.tvPrimera.setText(Controlador.calcularCajas(cajas.getCajasPrimera(),cajas.getVazquetesPrimera(),true));
        this.tvSegunda.setText(Controlador.calcularCajas(cajas.getCajasSegunda(),cajas.getVazquetesSegunda(),true));
        this.tvAgranel.setText(Controlador.calcularCajas(cajas.getCajasAgranel(),cajas.getVazquetesAgranel(),true));

        return convertView;

    }


}
