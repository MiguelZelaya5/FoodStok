package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class lista extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    private TextView textViewnombre, textViewcantidad;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        // Obtén referencias a los elementos de la vista en la actividad
        textViewnombre = findViewById(R.id.textViewnombre);
        textViewcantidad = findViewById(R.id.textViewcantidad);

        // Obtén el Id del artículo de la intent
        String idProducto = getIntent().getStringExtra("Id");

        // Crea una instancia de tu base de datos
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            // Realiza una nueva consulta para obtener detalles del artículo específico
            cursor = db.rawQuery("SELECT nombreproducto, cantidad, idproducto FROM productos WHERE idproducto = ?", new String[]{idProducto});

            // Comprueba si hay resultados en el cursor
            if (cursor != null && cursor.moveToFirst()) {
                // Obtén los valores de cada columna en el cursor
                String nombre = cursor.getString(0);
                int cantidad = cursor.getInt(1);

                // Actualiza los elementos de la vista con la información obtenida
                textViewnombre.setText(nombre);
                textViewcantidad.setText(String.valueOf(cantidad));
            } else {
                Log.e("LlenarProducto", "No se encontraron resultados para el ID del artículo: " + idProducto);
            }
        } catch (Exception e) {
            Log.e("LlenarProducto", "Error al acceder a la base de datos: " + e.getMessage());
        } finally {
            // Cierra el cursor después de usarlo
            if (cursor != null) {
                cursor.close();
            }

            // Cierra la conexión a la base de datos después de usarla
            db.close();
        }

    }
}