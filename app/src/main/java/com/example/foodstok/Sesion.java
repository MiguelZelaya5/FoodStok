package com.example.foodstok;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sesion extends AppCompatActivity {

    private EditText correoEditText, contraEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        correoEditText = findViewById(R.id.Correo);
        contraEditText = findViewById(R.id.contra);
        Button iniciarSesionButton = findViewById(R.id.btn_iniciar);

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoEditText.getText().toString();
                String contrasena = contraEditText.getText().toString();

                // Ejecutar la tarea de inicio de sesión en segundo plano
                new IniciarSesionTask().execute(correo, contrasena);
            }
        });
    }

    private class IniciarSesionTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String correo = params[0];
            String contrasena = params[1];

            try {
                // Construir la URL del archivo PHP en tu servidor
                String url = "http://192.168.1.6/android_mysq/login.php?correo=" +
                        URLEncoder.encode(correo, "UTF-8") +
                        "&contrasena=" +
                        URLEncoder.encode(contrasena, "UTF-8");

                // Crear una instancia de URL y HttpURLConnection
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                // Leer la respuesta del servidor
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = reader.readLine();

                // Cerrar conexiones
                reader.close();
                connection.disconnect();

                // Verificar si el inicio de sesión fue exitoso
                return result != null && "success".equals(result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean loginExitoso) {
            if (loginExitoso) {
                // Inicio de sesión exitoso
                Log.d("Sesion", "Inicio de sesión exitoso");

                Toast.makeText(Sesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                redirectActivity(Sesion.this, MainActivity.class);
            } else {
                // Inicio de sesión fallido
                Log.d("Sesion", "Inicio de sesión fallido");

                Toast.makeText(Sesion.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
