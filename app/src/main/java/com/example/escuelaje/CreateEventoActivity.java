package com.example.escuelaje;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.escuelaje.entidades.Nota;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEventoActivity extends AppCompatActivity {

    boolean fechaModificada = false;
    private EditText editTextTituloPrincipal;
    private EditText editTextTituloSecundario;
    private EditText editTextDescripcion;
    private TextView textViewFechaUsuario;
    private Button btnAddFecha;
    private Button btnAddNota;
    private Button btnCancel;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String dia, anio, mes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_evento);

        findViews();

        setListeners();
    }

    private void findViews() {
        editTextTituloPrincipal = findViewById(R.id.titulo_principal_edit_text);
        editTextTituloSecundario = findViewById(R.id.titulo_secundario_edit_text);
        editTextDescripcion = findViewById(R.id.descripcion_edit_text);

        textViewFechaUsuario = findViewById(R.id.fecha_usuario_text_view);

        btnAddFecha = findViewById(R.id.add_fecha_button);

        btnAddNota = findViewById(R.id.add_nota_button);

        btnCancel = findViewById(R.id.cancel_button);

    }

    private void setListeners() {
        setBotonAddFechaListener();
        setBotonAddNotaListener();
        setBotonCancelListener();
    }

    private void setBotonAddFechaListener() {
        btnAddFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Para controlar que la version sea minimo la 24
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    calendar = Calendar.getInstance();

                    int d = calendar.get(Calendar.DAY_OF_MONTH);
                    int m = calendar.get(Calendar.MONTH);
                    int y = calendar.get(Calendar.YEAR);

                    datePickerDialog = new DatePickerDialog(CreateEventoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int myYear, int myMonth, int myDay) {
                            dia = String.valueOf(myDay);
                            anio = String.valueOf(myYear);
                            mes = String.valueOf(myMonth + 1);

                            textViewFechaUsuario.setText(dia + "/" + mes + "/" + anio);
                        }
                    }, y, m, d);
                    datePickerDialog.show();
                }
            }
        });
    }

    private void setBotonAddNotaListener() {
        btnAddNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateEventoActivity.this, ListonActivity.class);
                if (validateFields()) {
                    addNota();
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }
        });
    }

    private boolean validateFields() {
        boolean tituloPrincipalRelleno = false;
        boolean tituloSecundarioRelleno = false;
        boolean descripcionRellena = false;

        if (fechaModificada == false) {
            textViewFechaUsuario.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (editTextTituloPrincipal.getText().toString().isEmpty()) {
            editTextTituloPrincipal.setError("Este campo no puede estar vacío");
        } else {
            editTextTituloPrincipal.setError(null);
            tituloPrincipalRelleno = true;
        }

        if (editTextTituloSecundario.getText().toString().isEmpty()) {
            editTextTituloSecundario.setError("Este campo no puede estar vacío");
        } else {
            editTextTituloSecundario.setError(null);
            tituloSecundarioRelleno = true;
        }

        if (editTextDescripcion.getText().toString().isEmpty()) {
            editTextDescripcion.setError("Este campo no puede estar vacío");
        } else {
            editTextDescripcion.setError(null);
            descripcionRellena = true;
        }

        boolean noEstaPredeterminada = !textViewFechaUsuario.getText().toString().equalsIgnoreCase("DD-MM-YY");
        boolean noEstaVacia = !textViewFechaUsuario.getText().toString().isEmpty();
        if (noEstaPredeterminada && noEstaVacia) {
            fechaModificada = true;
        }

        return tituloPrincipalRelleno && tituloSecundarioRelleno && descripcionRellena && fechaModificada;
    }


    private void setBotonCancelListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEventoActivity.this, ListonActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    private void addNota(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(ListonActivity.uniqueID);
        Nota nota = new Nota();

        nota.setTituloPrincipal(editTextTituloPrincipal.getText().toString());
        nota.setTituloSecundario(editTextTituloSecundario.getText().toString());
        nota.setInformacion(editTextDescripcion.getText().toString());
        nota.setDay(dia);
        nota.setMonth(mes);
        nota.setYear(anio);
        nota.setDate(anio,mes,dia);

        dbRef.push().setValue(nota,new DatabaseReference.CompletionListener(){
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if(error == null)
                    Log.i("LOGTAG", "Operación OK");
                else
                    Log.e("LOGTAG", "Error: " + error.getMessage());
            }
        });
    }
}
