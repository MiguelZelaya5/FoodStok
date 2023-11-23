package com.example.foodstok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Almacen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    private AlmcacenData adapter;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    LinearLayout exit,about,categoria,Almacen,home,salir;
    private Button btnBuscar2;
    private FloatingActionButton btnAgregarAl;
    private EditText editTextAlmacen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawerLayout);
        databaseHelper = new DatabaseHelper(this);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);
        salir=findViewById(R.id.salir);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }

            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, Categorias.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, lista_agregar.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Almacen.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        obtenerAlmacenes();



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

    private void logout() {
        setLoggedIn(false);
        clearUserId();

        Intent intent = new Intent(Almacen.this, Sesion.class);
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

    //TextView textViewNoArticulos = findViewById(R.id.textViewNoArticulos);

        /* Aqui hacer lo de la BD para hacer que se desaparezca cuando se agg un nuevo producto
        boolean productoAgregado;
        if () {
            textViewNoArticulos.setVisibility(View.GONE);
        } else {
            textViewNoArticulos.setVisibility(View.VISIBLE);
        }*/

    /*private void mostrarCuadroDialogo() {
        LayoutInflater inflater = LayoutInflater.from(this);

        View dialogView = inflater.inflate(R.layout.layout_cuadro_dialogo, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Ingrese el nombre del almacén")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Obtiene el texto ingresado por el usuario
                        EditText editTextAlmacen = dialogView.findViewById(R.id.editTextAlmacen);
                        String nombreAlmacen = editTextAlmacen.getText().toString();

                        guardarNombreAlmacenEnBD(nombreAlmacen);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/
    private void guardarNombreAlmacenEnBD(String nombreAlmacen) {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);


        SQLiteDatabase database = databaseHelper.getWritableDatabase();


        int userId = getUserIdFromSharedPreferences();

        try {
            ContentValues values = new ContentValues();

            values.put("AlmacenM", nombreAlmacen);


            long resultado = database.insert("articulos", null, values);

            if (resultado != -1) {
                Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                editTextAlmacen.setText("");
            } else {
                Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } finally {
            database.close();
        }


    }

    public void obtenerAlmacenes() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        Cursor cursor = db.rawQuery("SELECT DISTINCT AlmacenM FROM articulos WHERE id_usuario = ?", selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            List<DataAlmacen> dataalmacen = new ArrayList<>();

            do {
                String AlmacenM = cursor.getString(0);

                DataAlmacen dataalmace = new DataAlmacen( AlmacenM);

                dataalmacen.add(dataalmace);

            } while (cursor.moveToNext());

            cursor.close();

            adapter = new AlmcacenData(dataalmacen);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, R.string.no_se_encontraron_datossss, Toast.LENGTH_SHORT).show();
        }

        db.close();
    }



}
