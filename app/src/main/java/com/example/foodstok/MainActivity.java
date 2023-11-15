package com.example.foodstok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private TextView info;
    TableLayout tbArticulo;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private RecyclerViewAdapter adapter;
    FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home,listCompras,cerrar,salir;
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        salir=findViewById(R.id.salir);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);

        fab = findViewById(R.id.addProducto);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Aquí debes obtener los datos de la base de datos y pasarlos al adaptador
        // Aquí hay un ejemplo de cómo obtener los datos de una consulta a la base de datos:

        // Crea una instancia de tu base de datos
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)}; // Convertir el int a String

        Cursor cursor = db.rawQuery("SELECT foto, nombrearticulo, categoria, fechafabricacion, fechacaducidad, cantidad,idarticulo FROM articulos WHERE id_usuario = ?", selectionArgs);
        // Comprueba si hay resultados en el cursor
        if (cursor != null && cursor.moveToFirst()) {
            // Crea una lista para almacenar los datos obtenidos
            List<DataItem> dataItems = new ArrayList<>();


            do {
                // Obtén los valores de cada columna en el cursor
                byte[] foto = cursor.getBlob(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(2);
                String fechaFabricacion = cursor.getString(3);
                String fechaCaducidad = cursor.getString(4);
                int cantidad = cursor.getInt(5);
                String idarticulo=cursor.getString(6);

                // Crea un objeto DataItem con los datos obtenidos
                DataItem dataItem = new DataItem(foto, nombre, categoria, fechaFabricacion, fechaCaducidad, cantidad,idarticulo);

                // Agrega el objeto a la lista
                dataItems.add(dataItem);

            } while (cursor.moveToNext());

            // Cierra el cursor después de usarlo
            cursor.close();

            // Crea un adaptador y configúralo en el RecyclerView
            adapter = new RecyclerViewAdapter(dataItems);
            recyclerView.setAdapter(adapter);
        } else {
            // No se encontraron datos en la base de datos
            Toast.makeText(this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión a la base de datos después de usarla
        db.close();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(MainActivity.this, Almacen.class);
            }
        });

        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(MainActivity.this, Categorias.class);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MainActivity.this, lista.class);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MainActivity.this, agregar_producto.class);
                finish();
            }
        });



    }
    private void logout() {
        // Realizar aquí las tareas de cierre de sesión, como borrar datos de sesión, etc.
        setLoggedIn(false); // Establecer el estado de inicio de sesión como falso o cerrado
        clearUserId(); // Borrar el ID del usuario guardado en SharedPreferences

        // Redirigir a la pantalla de inicio de sesión (Login)
        Intent intent = new Intent(MainActivity.this, Sesion.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual (Inicio)
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuprincipal, menu);
        return true;
    }*/



}