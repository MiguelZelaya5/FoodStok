package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class lista_agregar extends AppCompatActivity {
    private EditText editTextProducto;
    private SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home,salir;
    private Button btn_lista, btn_agg_recordatorio;
    private EditText editTextCantidad; // Nuevo EditText para la cantidad
    private ListView listViewProductos;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productos;
    private ArrayList<Integer> cantidades; // Nuevo ArrayList para almacenar las cantidades

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agregar);

        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);
        salir=findViewById(R.id.salir);
        btn_agg_recordatorio=findViewById(R.id.btn_agg_rec);
        btn_lista = findViewById(R.id.btn_lista);
        editTextProducto = findViewById(R.id.editTextProduct);
        editTextCantidad = findViewById(R.id.editTextCantidad); // Enlazar con el EditText en el layout
        listViewProductos = findViewById(R.id.listViewProducto);
        productos = new ArrayList<>();
        cantidades = new ArrayList<>(); // Inicializar el ArrayList de cantidades

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, productos);
        listViewProductos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewProductos.setAdapter(adapter);

        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)};


        sharedPreferences = (lista_agregar.this).getSharedPreferences("session", Context.MODE_PRIVATE);

        btn_agg_recordatorio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lista_agregar.this, recordatorio.class);
                startActivity(intent);
            }
        });

        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista_agregar.this, lista.class);
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
                redirectActivity(lista_agregar.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista_agregar.this, Almacen.class);

            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista_agregar.this, Categorias.class);
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
                Toast.makeText(lista_agregar.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

    }

    public void agregarProducto(View view) {
        String nombreProducto = editTextProducto.getText().toString();
        String cantidadProducto = editTextCantidad.getText().toString(); // Obtener la cantidad ingresada

        if (!nombreProducto.isEmpty() && !cantidadProducto.isEmpty()) {
            productos.add(nombreProducto);
            cantidades.add(Integer.parseInt(cantidadProducto)); // Convertir cantidad a entero y agregarla al ArrayList
            adapter.notifyDataSetChanged();
            editTextProducto.setText("");
            editTextCantidad.setText(""); // Limpiar el EditText de cantidad
        }
    }

    public void eliminarProductos(View view) {
        SparseBooleanArray checkedItems = listViewProductos.getCheckedItemPositions();
        ArrayList<String> productosSeleccionados = new ArrayList<>();
        ArrayList<Integer> cantidadesSeleccionadas = new ArrayList<>(); // Nuevo ArrayList para almacenar las cantidades seleccionadas

        for (int i = 0; i < checkedItems.size(); i++) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                productosSeleccionados.add(productos.get(position));
                cantidadesSeleccionadas.add(cantidades.get(position)); // Agregar la cantidad correspondiente al producto seleccionado
            }
        }

        productos.removeAll(productosSeleccionados);
        cantidades.removeAll(cantidadesSeleccionadas); // Eliminar las cantidades seleccionadas
        listViewProductos.clearChoices();
        adapter.notifyDataSetChanged();
    }


    public void guardarLista(View view) {
        StringBuilder listaGuardada = new StringBuilder();
        StringBuilder listaGuardada_cant = new StringBuilder();

        for (int i = 0; i < productos.size(); i++) {
            String producto = productos.get(i);
            listaGuardada.append(producto).append("\n");
        }

        for (int i = 0; i < cantidades.size(); i++) {
            int cantidad = cantidades.get(i);
            listaGuardada_cant.append(cantidad).append("\n");
        }

        if (listaGuardada.toString().isEmpty()) {
            Toast.makeText(lista_agregar.this, "Ingrese al menos un producto", Toast.LENGTH_SHORT).show();
        }

        if (listaGuardada_cant.toString().isEmpty()) {
            Toast.makeText(lista_agregar.this, "Ingrese al menos uno", Toast.LENGTH_SHORT).show();
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(lista_agregar.this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int idusuarios = idusuariosFromSharedPreferences();

        ContentValues values = new ContentValues();
        values.put("nombreproducto", listaGuardada.toString());
        values.put("cantidad", listaGuardada_cant.toString());
        values.put("idusuario", idusuarios);

        long resultado = database.insert("productos", null, values);

        if (resultado != -1) {
            Toast.makeText(lista_agregar.this, "Lista agregada correctamente", Toast.LENGTH_SHORT).show();
            redirectToHome();
        } else {
            Toast.makeText(lista_agregar.this, "Error al agregar la lista", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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
        Intent intent = new Intent(lista_agregar.this, Sesion.class);
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


    private void redirectToHome() {
        Intent intent = new Intent((lista_agregar.this), MainActivity.class);
        startActivity(intent);
        (lista_agregar.this).finish();
    }

    private int idusuariosFromSharedPreferences() {
        return sharedPreferences.getInt("idusuarios", -1);
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}