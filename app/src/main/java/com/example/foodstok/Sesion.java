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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        correoEditText = findViewById(R.id.Correo);
        contraEditText = findViewById(R.id.contra);
        databaseHelper = new DatabaseHelper(this);
        // Inicializar instancia de SharedPreferences
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if (isUserLoggedIn()) {
            redirectToHome(Sesion.this, MainActivity.class); // Redirigir a la actividad de inicio directamente
        }
        iniciarSesionButton = findViewById(R.id.btn_iniciar);

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoEditText.getText().toString();
                String password = contraEditText.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Sesion.this, "Por favor, ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar las credenciales en la base de datos
                    if (checkCredentials(email, password)) {
                        // Iniciar sesión exitosa
                        int userId = getUserId(email); // Obtener el ID del usuario desde la base de datos
                        setLoggedIn(true); // Guardar el estado de inicio de sesión en SharedPreferences
                        saveUserId(userId); // Guardar el ID del usuario en SharedPreferences
                        redirectToHome(Sesion.this, MainActivity.class); // Redirigir a la actividad de inicio
                    } else {
                        // Credenciales incorrectas, mostrar mensaje de error
                        Toast.makeText(Sesion.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }
    @SuppressLint("Range")
    private int getUserId(String email) {
        int userId = -1; // Valor predeterminado si no se encuentra el usuario

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT idusuarios FROM usuarios WHERE correoelectronico = ?", new String[]{email});

            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("idusuarios"));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, "Error al obtener el ID del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return userId;
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
            Toast.makeText(Sesion.this, "Error al guardar el estado de inicio de sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private boolean checkCredentials(String email, String password) {
        boolean result = false;

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            // Realizar una consulta a la base de datos para verificar las credenciales
            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE correoelectronico = ? AND contrasena = ?", new String[]{email, password});

            result = cursor.getCount() > 0;

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, "Error al verificar las credenciales: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return result;
    }
    private void saveUserId(int userId) {
        try {
            sharedPreferences.edit().putInt("idusuarios", userId).apply();
        } catch (Exception e) {
            Toast.makeText(Sesion.this, "Error al guardar el ID del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
