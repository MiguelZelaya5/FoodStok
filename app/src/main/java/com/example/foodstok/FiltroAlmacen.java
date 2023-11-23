package com.example.foodstok;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FiltroAlmacen extends AppCompatActivity {
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    redirectActivity(FiltroAlmacen.this, MainActivity.class);
                }
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(FiltroAlmacen.this, Almacen.class);
            }
        });

        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(FiltroAlmacen.this, Categorias.class);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FiltroAlmacen.this, "LogOut", Toast.LENGTH_SHORT).show();
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
                redirectActivity(FiltroAlmacen.this, lista_agregar.class);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(FiltroAlmacen.this, agregar_producto.class);
            }
        });


        String idArticulo = getIntent().getStringExtra("nombreAlmacen");
        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId), idArticulo};
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT foto, nombrearticulo, categoria, fechafabricacion, fechacaducidad, cantidad,idarticulo FROM articulos WHERE id_usuario = ? AND AlmacenM = ?", selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            List<DataItem> dataItems = new ArrayList<>();


            do {
                byte[] foto = cursor.getBlob(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(2);
                String fechaFabricacion = cursor.getString(3);
                String fechaCaducidad = cursor.getString(4);
                int cantidad = cursor.getInt(5);
                String idarticulo=cursor.getString(6);

                DataItem dataItem = new DataItem(foto, nombre, categoria, fechaFabricacion, fechaCaducidad, cantidad,idarticulo);

                dataItems.add(dataItem);

            } while (cursor.moveToNext());

            cursor.close();

            adapter = new RecyclerViewAdapter(dataItems);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, R.string.no_se_encontraron_datosF, Toast.LENGTH_SHORT).show();
        }

        db.close();
    }



    private void logout() {
        setLoggedIn(false);
        clearUserId();

        Intent intent = new Intent(FiltroAlmacen.this, Sesion.class);
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
