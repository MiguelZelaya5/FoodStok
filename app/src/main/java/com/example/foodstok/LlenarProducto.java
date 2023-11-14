package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class LlenarProducto extends AppCompatActivity {

    private ImageView imageView;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    private TextView nombreTextView, categoriaTextView, fabricacionTextView, caducidadTextView, cantidadTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenar_producto);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        // Obtén referencias a los elementos de la vista en la actividad LlenarProducto
        imageView = findViewById(R.id.ivProductImage);
        nombreTextView = findViewById(R.id.etProductName);
        categoriaTextView = findViewById(R.id.etCategory);
        fabricacionTextView = findViewById(R.id.tvManufacturingDate);
        caducidadTextView = findViewById(R.id.tvExpirationDate);
        cantidadTextView = findViewById(R.id.etQuantity);

        // Obtén el Id del artículo de la intent
        String idArticulo = getIntent().getStringExtra("Id");

        // Crea una instancia de tu base de datos
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            // Realiza una nueva consulta para obtener detalles del artículo específico
            cursor = db.rawQuery("SELECT foto, nombrearticulo, categoria, fechafabricacion, fechacaducidad, cantidad, idarticulo FROM articulos WHERE idarticulo = ?", new String[]{idArticulo});

            // Comprueba si hay resultados en el cursor
            if (cursor != null && cursor.moveToFirst()) {
                // Obtén los valores de cada columna en el cursor
                byte[] foto = cursor.getBlob(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(2);
                String fechaFabricacion = cursor.getString(3);
                String fechaCaducidad = cursor.getString(4);
                int cantidad = cursor.getInt(5);

                // Actualiza los elementos de la vista con la información obtenida
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
                nombreTextView.setText("Nombre de producto:"+nombre);
                categoriaTextView.setText("Categoria: "+categoria);
                fabricacionTextView.setText("Fecha fabricación"+fechaFabricacion);
                caducidadTextView.setText("Fecha Caducidad"+fechaCaducidad);
                String cantidads;
                cantidads=String.valueOf(cantidad);
                cantidadTextView.setText("Cantidad: "+cantidads);
            } else {
                Log.e("LlenarProducto", "No se encontraron resultados para el ID del artículo: " + idArticulo);
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
