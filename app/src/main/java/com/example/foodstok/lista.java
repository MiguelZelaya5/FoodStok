package com.example.foodstok;

import static com.example.foodstok.lista_agregar.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodstok.R;
import com.example.foodstok.datos_lista.DBdatos;
import com.example.foodstok.datos_lista.datos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.foodstok.datos_lista.datos;
import com.example.foodstok.datos_lista.list_adapter;
import com.example.foodstok.lista_agregar;

import java.util.ArrayList;
import java.util.List;

public class lista extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home,salir;
    private TextView textViewnombre, textViewcantidad;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    FloatingActionButton fab, fabEliminar;

    RecyclerView listadatos;

    ArrayList<datos> listaArraydatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        /*drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);
        salir=findViewById(R.id.salir);*/

        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)};


        DBdatos list = new DBdatos(lista.this);
        listadatos = findViewById(R.id.listadatos);
        listadatos.setLayoutManager(new LinearLayoutManager(this));
        listaArraydatos = new ArrayList<>();
        list_adapter adapter1 = new list_adapter(list.mostrarContactos());
        listadatos.setAdapter(adapter1);

        fab = findViewById(R.id.favNuevo);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista.this, lista_agregar.class);
            }
        });



        /*menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista.this, Almacen.class);

            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista.this, Categorias.class);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(lista.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });*/


        /*// Obtén referencias a los elementos de la vista en la actividad
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
        }*/

    }
    /*public static void openDrawer(DrawerLayout drawerLayout){
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
    }*/

    private void logout() {
        setLoggedIn(false);
        clearUserId();

        Intent intent = new Intent(lista.this, Sesion.class);
        startActivity(intent);
        finish();
    }
    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idusuarios");
        editor.apply();
    }
    private int getUserIdFromSharedPreferences() {
        return sharedPreferences.getInt("idusuarios", -1);
    }

}