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

        //btnAgregarAl=findViewById(R.id.ItemAgregar);
        //editTextAlmacen=findViewById(R.id.editTextAlmacen);

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
        // Realizar aquí las tareas de cierre de sesión, como borrar datos de sesión, etc.
        setLoggedIn(false); // Establecer el estado de inicio de sesión como falso o cerrado
        clearUserId(); // Borrar el ID del usuario guardado en SharedPreferences

        // Redirigir a la pantalla de inicio de sesión (Login)
        Intent intent = new Intent(Almacen.this, Sesion.class);
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

        // Obtén la instancia de la base de datos en modo escritura
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // Obtén el ID del usuario de las preferencias compartidas
        int userId = getUserIdFromSharedPreferences();

        // Obtén el nombre del almacén del EditText
        //String almacen = editTextAlmacen.getText().toString();

        try {
            ContentValues values = new ContentValues();

            // No es necesario establecer valores nulos para las otras columnas
            // Solo establece el valor para la columna "AlmacenM"
            values.put("AlmacenM", nombreAlmacen);


            // Inserta el registro en la tabla "articulos"
            long resultado = database.insert("articulos", null, values);

            if (resultado != -1) {
                Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                editTextAlmacen.setText("");
            } else {
                Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir durante la inserción
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } finally {
            // Asegúrate de cerrar la base de datos para liberar recursos
            database.close();
        }


    }

    public void obtenerAlmacenes() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)}; // Convertir el int a String

        Cursor cursor = db.rawQuery("SELECT DISTINCT AlmacenM FROM articulos WHERE id_usuario = ?", selectionArgs);

        // Comprueba si hay resultados en el cursor
        if (cursor != null && cursor.moveToFirst()) {
            // Crea una lista para almacenar los datos obtenidos
            List<DataAlmacen> dataalmacen = new ArrayList<>();


            do {
                // Obtén los valores de cada columna en el cursor

                String AlmacenM = cursor.getString(0);


                // Crea un objeto DataItem con los datos obtenidos
                DataAlmacen dataalmace = new DataAlmacen( AlmacenM);

                // Agrega el objeto a la lista
                dataalmacen.add(dataalmace);

            } while (cursor.moveToNext());

            // Cierra el cursor después de usarlo
            cursor.close();

            // Crea un adaptador y configúralo en el RecyclerView
            adapter = new AlmcacenData(dataalmacen);
            recyclerView.setAdapter(adapter);
        } else {
            // No se encontraron datos en la base de datos
            Toast.makeText(this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión a la base de datos después de usarla
        db.close();
    }



}
