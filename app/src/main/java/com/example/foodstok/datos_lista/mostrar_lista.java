package com.example.foodstok.datos_lista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodstok.DatabaseHelper;
import com.example.foodstok.R;
import com.example.foodstok.lista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class mostrar_lista extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    ArrayList<com.example.foodstok.datos_lista.datos> datos;
    int id = 1;

    FloatingActionButton fabEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lista);

        fabEliminar = findViewById(R.id.fabEliminar);

        final DBdatos list = new DBdatos(mostrar_lista.this);
        datos = list.mostrarDatos(id);

        fabEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mostrar_lista.this);
                builder.setMessage("¿Desea eliminar este contacto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(list.elimardatos(id)){
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    /*public void eliminarContacto(View view) {
        final DBdatos list = new DBdatos(mostrar_lista.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea eliminar este contacto?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(list.elimardatos(id)){
                            lista();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al seleccionar "No"
                    }
                }).show();
    }*/

    private void lista(){
        Intent intent = new Intent(this, lista.class);
        startActivity(intent);
    }
}