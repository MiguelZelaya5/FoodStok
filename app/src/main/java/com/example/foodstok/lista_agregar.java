package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class lista_agregar extends AppCompatActivity {
    private EditText editTextProducto;

    private Button btn_lista;
    private EditText editTextCantidad; // Nuevo EditText para la cantidad
    private ListView listViewProductos;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productos;
    private ArrayList<Integer> cantidades; // Nuevo ArrayList para almacenar las cantidades
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agregar);

        btn_lista = findViewById(R.id.btn_lista);
        editTextProducto = findViewById(R.id.editTextProduct);
        editTextCantidad = findViewById(R.id.editTextCantidad); // Enlazar con el EditText en el layout
        listViewProductos = findViewById(R.id.listViewProducto);
        productos = new ArrayList<>();
        cantidades = new ArrayList<>(); // Inicializar el ArrayList de cantidades

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, productos);
        listViewProductos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewProductos.setAdapter(adapter);


        sharedPreferences = (lista_agregar.this).getSharedPreferences("session", Context.MODE_PRIVATE);

        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(lista_agregar.this, lista.class);
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