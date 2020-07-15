package com.luis.corte.views.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.luis.corte.Controlers.Controlador;
import com.luis.corte.R;
import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.models.ReporteCajasTotales;
import com.luis.corte.views.adaptadores.ListaReporteAdapter;
import com.luis.corte.views.dialogForm.DialogDetalleProduccion;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class reporte extends Fragment {
    private TextView tvTotalPrimera;
    private TextView tvTotalSegunda;
    private TextView tvTotalAgranel;
    private TextView tvTotralTrabajadores;
    private ListView lvReporte;
    private LinearLayout llTotalProduccion;
    private ListaReporteAdapter adapter;
    private Controlador controlador;

    public reporte() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reporte, container, false);
        this.tvTotalPrimera =  view.findViewById(R.id.tv_totalPrimera_reporte);
        this.tvTotalSegunda =  view.findViewById(R.id.tv_totalSegunda_reporte);
        this.tvTotalAgranel =  view.findViewById(R.id.tv_totalAgranel_reporte);
        this.tvTotralTrabajadores =  view.findViewById(R.id.tv_total_trabajadores);
        this.lvReporte =  view.findViewById(R.id.lv_reporte);
        this.llTotalProduccion = view.findViewById(R.id.ll_TotalProduccion);


        TreeMap<String, Object> reporteDiaTrabajadores = controlador.getReporteDiaTrabajadores();
        ReporteCajasTotales t = (ReporteCajasTotales) reporteDiaTrabajadores.get("totales");

        if(t!=null){
            tvTotalPrimera.setText(Controlador.calcularCajas(t.getCajasPrimera(),t.getVasquetesPrimera(),true));
            tvTotalSegunda.setText(Controlador.calcularCajas(t.getCajasSegunda(),t.getVasquetesSegunda(),true));
            tvTotalAgranel.setText(Controlador.calcularCajas(t.getCajasAgranel(),t.getVasquetesAgranel(),false));
            tvTotralTrabajadores.setText("Trabajadores: " + t.getTotalTrabajadores().toString());
        }else{
            tvTotalPrimera.setText("");
            tvTotalSegunda.setText("");
            tvTotalAgranel.setText("");
        }

        this.adapter = new ListaReporteAdapter(this.controlador.getActivity(),(ArrayList<ReporteCajasTotales>) reporteDiaTrabajadores.get("trabajadores"));
        this.lvReporte.setAdapter(this.adapter);
        this.lvReporte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileLog.i(Complementos.TAG_REPORTE,"mostrar detalle de produccion");
                DialogDetalleProduccion detalleProduccion = new DialogDetalleProduccion(controlador.getActivity());
                detalleProduccion.setControlador(reporte.this.controlador,adapter.getItem(position));
                detalleProduccion.show();
            }
        });

        this.llTotalProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.i(Complementos.TAG_REPORTE,"mostrar detalle de produccion general");
                DialogDetalleProduccion detalleProduccion = new DialogDetalleProduccion(controlador.getActivity());
                detalleProduccion.setControlador(reporte.this.controlador,null);
                detalleProduccion.show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void setControlador(Controlador controlador){
        this.controlador = controlador;
    }
}
