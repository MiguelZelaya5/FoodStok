package com.example.foodstok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class primerapantalla extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laout_primerapantalla);
        int DELAY_TIME=3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Crea un intent para abrir la segunda actividad
                Intent intent = new Intent(primerapantalla.this, Sesion.class);
                startActivity(intent);

                // Cierra la actividad actual para que el usuario no pueda volver a ella presionando el botón de retroceso
                finish();
            }
        }, DELAY_TIME);
    }
}
