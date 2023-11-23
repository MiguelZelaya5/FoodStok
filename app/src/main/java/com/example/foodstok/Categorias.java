package com.example.foodstok;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Categorias extends AppCompatActivity {

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private mostrarCategorias adapter;
    TextView vista, nombreTextView, fabricacionTextView, cantidadTextView, caducidadTextView ;
    private RecyclerView recyclerView;
    EditText grabar;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    ImageView menu, imageView ;
    LinearLayout exit,about,categoria,Almacen,home,salir;
    String txtCategoria;

    private Button menuButton, btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageView = findViewById(R.id.imageView);
        salir=findViewById(R.id.salir);
        fab = findViewById(R.id.ItemAgregar);
        nombreTextView = findViewById(R.id.nombreTextView);
        fabricacionTextView = findViewById(R.id.fabricacionTextView);
        caducidadTextView = findViewById(R.id.caducidadTextView);
        cantidadTextView = findViewById(R.id.cantidadTextView);
        vista= findViewById(R.id.tvNarticulo);
        drawerLayout = findViewById(R.id.drawerLayout);
        grabar = findViewById(R.id.editTextBuscar);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);
        grabar= (EditText) findViewById(R.id.editTextBuscar);
        btnBuscar = findViewById(R.id.btnBuscar);
        ImageView imageView = findViewById(R.id.imageViewArticulo);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                int userId = getUserIdFromSharedPreferences();
                txtCategoria = grabar.getText().toString();
                String[] selectionArgs = new String[]{String.valueOf(userId), txtCategoria};

                Cursor cursor = db.rawQuery("SELECT foto, nombrearticulo, categoria, fechafabricacion, fechacaducidad, cantidad,idarticulo FROM articulos WHERE id_usuario = ? AND categoria LIKE ?", selectionArgs);
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

                    adapter = new mostrarCategorias(dataItems);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Categorias.this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                }

                db.close();
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
                redirectActivity(Categorias.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Categorias.this, Almacen.class);
            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Categorias.this, lista_agregar.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Categorias.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();

            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Categorias.this, agregar_producto.class);
                finish();
            }
        });

    }
    /*private void buscarEnBaseDeDatos(String categoria) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String query = "SELECT * FROM articulos WHERE categoria LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoria});

        StringBuilder resultado = new StringBuilder();

        imageViewArticulo = findViewById(R.id.imageViewArticulo);

        if (cursor.moveToFirst()) {
            do {
                // Obtén los valores de las columnas
                @SuppressLint("Range") String nombreArticulo = cursor.getString(cursor.getColumnIndex("nombrearticulo"));
                @SuppressLint("Range") String fechaFabricacion = cursor.getString(cursor.getColumnIndex("fechafabricacion"));
                @SuppressLint("Range") String fechacaducidad = cursor.getString(cursor.getColumnIndex("fechacaducidad"));
                @SuppressLint("Range") String cantidad = cursor.getString(cursor.getColumnIndex("cantidad"));
                @SuppressLint("Range") byte[] imagenBytes = cursor.getBlob(cursor.getColumnIndex("foto"));

                // Convierte el BLOB a un Bitmap
                Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);

                // Construye el resultado con formato
                resultado.append("Artículo: ").append(nombreArticulo).append("\n")
                        .append("Fecha de Fabricación: ").append(fechaFabricacion).append("\n")
                        .append("Fecha de Caducidad: ").append(fechacaducidad).append("\n")
                        .append("Cantidad: ").append(cantidad).append("\n\n");

                // Muestra la imagen en el ImageView
                if (imagenBitmap != null) {
                    // Crea un nuevo ImageView para cada imagen
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(imagenBitmap);
                    // Agrega el nuevo ImageView al diseño existente
                    LinearLayout linearLayout = findViewById(R.id.linear); // Cambia al ID de tu diseño contenedor
                    linearLayout.addView(imageView);

                    imageView.setVisibility(View.VISIBLE); // Asegura que el ImageView sea visible solo si hay una imagen
                }
            } while (cursor.moveToNext());
        } else {
            resultado.append("No se encontraron artículos con esta categoría");
        }

        // Muestra el resultado en el TextView
        TextView vista = findViewById(R.id.tvNarticulo);  // Ajusta según el ID de tu TextView
        vista.setText(resultado.toString());

        cursor.close();
        db.close();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    grabar.setText(strSpeech2Text);

                    // Llamar a la acción del botón después de reconocimiento de voz
                    btnBuscar.performClick();
                }
                break;
            default:
                break;
        }
    }

    private int getUserIdFromSharedPreferences() {
        return sharedPreferences.getInt("idusuarios", -1);
    }

    public void onClickImgBtnMicrofono(View v) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
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
    private void logout() {
        setLoggedIn(false);
        clearUserId();

        Intent intent = new Intent(Categorias.this, Sesion.class);
        startActivity(intent);
        finish();
    }
    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idusuarios");
        editor.apply();
    }
    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
