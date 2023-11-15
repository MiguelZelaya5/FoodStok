package com.example.foodstok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LlenarProducto extends AppCompatActivity {

    private ImageView imageView;
    private Button btnDelete;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home;

    private TextView nombreTextView, categoriaTextView, fabricacionTextView, caducidadTextView, cantidadTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenar_producto);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        btnDelete=findViewById(R.id.btnEliminar);
        // Obtén referencias a los elementos de la vista en la actividad LlenarProducto
        imageView = findViewById(R.id.ivProductImage);
        nombreTextView = findViewById(R.id.etProductName);
        categoriaTextView = findViewById(R.id.etCategory);
        fabricacionTextView = findViewById(R.id.tvManufacturingDate);
        caducidadTextView = findViewById(R.id.tvExpirationDate);
        cantidadTextView = findViewById(R.id.etQuantity);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);

        // Obtén el Id del artículo de la intent
        String idArticulo = getIntent().getStringExtra("Id");

        // Crea una instancia de tu base de datos
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT foto, nombrearticulo, fechafabricacion, fechacaducidad, cantidad ,categoria FROM articulos WHERE idarticulo = ? LIMIT 1", new String[]{idArticulo});

            if (cursor != null && cursor.moveToFirst()) {
                // Obtén los valores de cada columna en el cursor
                byte[] foto = cursor.getBlob(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(5);
                String fechaFabricacion = cursor.getString(2);
                String fechaCaducidad = cursor.getString(3);
                int cantidad = cursor.getInt(4);



                imageView.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
                nombreTextView.setText("Nombre de producto: "+nombre);
                categoriaTextView.setText("Categoria: "+categoria);
                fabricacionTextView.setText("Fecha fabricación: "+fechaFabricacion);
                caducidadTextView.setText("Fecha Caducidad: "+fechaCaducidad);
                String cantidads;
                cantidads=String.valueOf(cantidad);
                cantidadTextView.setText("Cantidad: "+cantidads);
            } else {
                Log.e("LlenarProducto", "No se encontraron resultados para el ID del artículo: " + idArticulo);
            }
        } catch (Exception e) {
            Log.e("LlenarProducto", "Error al acceder a la base de datos: " + e.getMessage());
        } finally {

            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LlenarProducto.this);
                builder.setMessage("¿Desea eliminar este contacto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminar();
                                Intent intent = new Intent(LlenarProducto.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LlenarProducto.this, "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(LlenarProducto.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    redirectActivity(LlenarProducto.this, Almacen.class);
                }

            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(LlenarProducto.this, Categorias.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LlenarProducto.this, "LogOut", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    public void eliminar() {
        String idArticulo = getIntent().getStringExtra("Id");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String selection = "idarticulo = ?";
        String[] selectionArgs = {String.valueOf(idArticulo)};

        int rowsDeleted = database.delete("articulos", selection, selectionArgs);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }




}
