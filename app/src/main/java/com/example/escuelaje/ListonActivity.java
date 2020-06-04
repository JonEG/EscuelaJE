package com.example.escuelaje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.escuelaje.entidades.Nota;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListonActivity extends AppCompatActivity implements View.OnCreateContextMenuListener,
        ContextMenu.ContextMenuInfo {
    public static final String EXTRA_MESSAGE = "VAMONO_A_EDITAR";
    private static final String EXTRA_IDP_RESPONSE = "extra_idp_response";


    private FloatingActionButton fabAddEvento;

    private FirebaseRecyclerAdapter myAdapter;

    public static String uniqueID;
    private int elementoPulsado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liston);
        prepararToolbar();
        prepararfabAddEventoListener();

        recogerUsuarioFirebase();
        setearRecyclerView();
    }

    private void prepararToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                signOut();
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void prepararfabAddEventoListener() {
        fabAddEvento = findViewById(R.id.add_evento_floating_action_button);
        fabAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListonActivity.this, CreateEventoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    //TODO ---- FIREBASE RECYCLER -------

    private void setearRecyclerView() {
        DatabaseReference dbNotas = FirebaseDatabase.getInstance()
                .getReference().child(uniqueID);


        Query query = dbNotas.limitToLast(15).orderByChild("date");

        final RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Nota> options =
                new FirebaseRecyclerOptions.Builder<Nota>()
                        .setQuery(query, Nota.class)
                        .build();

        myAdapter = new FirebaseRecyclerAdapter(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {
                FirebaseViewHolder fvh = (FirebaseViewHolder) holder;
                fvh.setDatos((Nota) model);
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cajita_liston, parent, false);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        elementoPulsado = recyclerView.getChildAdapterPosition(v);
                        v.showContextMenu();
                        return true;
                    }
                });
                return new FirebaseViewHolder(view);
            }
        };


        recyclerView.setAdapter(myAdapter);
    }

    /**
     * Método necesario para el FirebaseRecyclerAdapter
     */
    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    /**
     * Método necesario para el FirebaseRecyclerAdapter
     */
    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }


    //TODO COSAS DE FIREBASE AUTH-------------------------------------------

    public void recogerUsuarioFirebase() {
        //if user is not valid
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(StartActivity.createIntent(this));
            finish();
            return;
        } else {
            uniqueID = currentUser.getUid();
        }
    }

    public static Intent createIntent(Context context, IdpResponse idpResponse) {
        Intent in = new Intent();
        in.putExtra(EXTRA_IDP_RESPONSE, idpResponse);
        in.setClass(context, ListonActivity.class);
        return in;
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(StartActivity.createIntent(ListonActivity.this));
                            finish();
                        } else {
                            // Signout failed
                        }
                    }
                });
    }

    //TODO Context MENU -----------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.my_recycler_view) {

            //Contiene informacion crema
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            //Le ponemos como título al menú Xcosa
            menu.setHeaderTitle("¿Qué desea? ");

            inflater.inflate(R.menu.my_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CtxLstEditar:
                String key = myAdapter.getRef(elementoPulsado).getKey();

                Intent intent = new Intent(ListonActivity.this, EditEventoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, key);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                return true;


            case R.id.CtxLstEliminar:
                deleteSelectedItem();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    //TODO  --- ELIMINAR ELEMENTOS ------------------------------------
    private void deleteSelectedItem() {
        myAdapter.getRef(elementoPulsado).removeValue();
    }
}
