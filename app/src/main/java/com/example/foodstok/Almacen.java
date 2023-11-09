package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class Almacen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);

        TextView textViewNoArticulos = findViewById(R.id.textViewNoArticulos);

        /* Aqui hacer lo de la BD para hacer que se desaparezca cuando se agg un nuevo producto
        boolean productoAgregado;
        if () {
            textViewNoArticulos.setVisibility(View.GONE);
        } else {
            textViewNoArticulos.setVisibility(View.VISIBLE);
        }*/

    }
}
