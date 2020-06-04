package com.example.escuelaje;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.escuelaje.entidades.Nota;


public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    private View myView;

    private TextView txtViewTituloPrincipal;
    private TextView txtViewTituloSecundario;
    private TextView txtViewInformacion;
    private TextView txtViewFechaDia;
    private TextView txtViewFechaMes;
    private TextView txtViewFechaAnio;



    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;

        findViews();
    }

    private void findViews(){
        txtViewTituloPrincipal = myView.findViewById(R.id.titulo_principal_text_view);
        txtViewTituloSecundario = myView.findViewById(R.id.titulo_secundario_text_view);
        txtViewInformacion = myView.findViewById(R.id.informacion_text_view);
        txtViewFechaAnio = myView.findViewById(R.id.anio_text_view);
        txtViewFechaMes = myView.findViewById(R.id.mes_text_view);
        txtViewFechaDia = myView.findViewById(R.id.dia_text_view);
    }

    public void setDatos(Nota nota){
        txtViewTituloPrincipal.setText(nota.getTituloPrincipal());
        txtViewTituloSecundario.setText(nota.getTituloSecundario());
        txtViewInformacion.setText(nota.getInformacion());
        txtViewFechaAnio.setText(nota.getYear());
        txtViewFechaMes.setText(nota.getMonth());
        txtViewFechaDia.setText(nota.getDay());

    }
}