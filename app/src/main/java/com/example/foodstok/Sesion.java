package com.example.foodstok;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sesion extends AppCompatActivity {

    private EditText correoEditText, contraEditText;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private Button iniciarSesionButton;

    private TextView inises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);


        inises = findViewById(R.id.inise);
        correoEditText = findViewById(R.id.Correo);
        contraEditText = findViewById(R.id.contra);
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("correo_usuario") && intent.hasExtra("contrasena_usuario")) {

            String correoUsuario = intent.getStringExtra("correo_usuario");
            String contrasenaUsuario = intent.getStringExtra("contrasena_usuario");


            correoEditText.setText(correoUsuario);
            contraEditText.setText(contrasenaUsuario);
        }

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if (isUserLoggedIn()) {
            redirectToHome(Sesion.this, MainActivity.class);
        }
        iniciarSesionButton = findViewById(R.id.btn_iniciar);

        inises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoEditText.getText().toString();
                String password = contraEditText.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Sesion.this, R.string.por_favor_ingresa_todos_los_campos, Toast.LENGTH_SHORT).show();
                } else {

                    if (checkCredentials(email, password)) {

                        int userId = getUserId(email);
                        setLoggedIn(true);
                        saveUserId(userId);
                        redirectToHome(Sesion.this, MainActivity.class);
                    } else {
                        // Credenciales incorrectas, mostrar mensaje de error
                        Toast.makeText(Sesion.this, R.string.credenciales_incorrectas, Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }
    @SuppressLint("Range")
    private int getUserId(String email) {
        int userId = -1;

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT idusuarios FROM usuarios WHERE correoelectronico = ?", new String[]{email});

            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("idusuarios"));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, getString(R.string.error_al_obtener_el_id_del_usuario) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return userId;
    }

    private void siguiente(){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
        finish();
    }


    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    private void setLoggedIn(boolean isLoggedIn) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", isLoggedIn);
            editor.apply();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, getString(R.string.error_al_guardar_el_estado_de_inicio_de_sesi_n) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private boolean checkCredentials(String email, String password) {
        boolean result = false;

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();


            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE correoelectronico = ? AND contrasena = ?", new String[]{email, password});

            result = cursor.getCount() > 0;

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, getString(R.string.error_al_verificar_las_credenciales) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return result;
    }
    private void saveUserId(int userId) {
        try {
            sharedPreferences.edit().putInt("idusuarios", userId).apply();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, getString(R.string.error_al_guardar_el_id_del_usuario) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void redirectToHome(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
