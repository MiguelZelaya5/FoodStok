package com.example.foodstok;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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

import java.util.ArrayList;

public class Categorias extends AppCompatActivity {

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    TextView vista;
    EditText grabar;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home;

    private Button menuButton, btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        vista= findViewById(R.id.textViewNoArticulos);
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

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoriaBuscada = grabar.getText().toString();
                buscarEnBaseDeDatos(categoriaBuscada);
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
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Categorias.this, "LogOut", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void buscarEnBaseDeDatos(String categoria) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String query = "SELECT * FROM articulos WHERE categoria = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoria});

        StringBuilder resultado = new StringBuilder();

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
                        .append("Cantidad: ").append(cantidad).append("\n");

                // Muestra la imagen en el TextView usando ImageSpan
                if (imagenBitmap != null) {
                    ImageSpan imageSpan = new ImageSpan(this, imagenBitmap);
                    SpannableString spannableString = new SpannableString(" ");
                    spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    resultado.append(spannableString).append("\n\n");
                }
            } while (cursor.moveToNext());
        } else {
            resultado.append("No se encontraron artículos con esta categoría");
        }

        // Muestra el resultado en el TextView
        TextView vista = findViewById(R.id.textViewNoArticulos);  // Ajusta según el ID de tu TextView
        vista.setText(resultado.toString());

        cursor.close();
        db.close();
    }


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
                }
                break;
            default:
                break;
        }
    }

    public void onClickImgBtnMicrofono(View v) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
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
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
