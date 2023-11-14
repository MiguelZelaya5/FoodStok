package com.example.foodstok;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {

    private EditText nombre, apellidos, correo, contrasena;
    private Button btn_registro;

    private TextView inises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.ape);
        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.contra);
        btn_registro = findViewById(R.id.btn_iniciar);

        inises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombree = nombre.getText().toString();
                String apellido = apellidos.getText().toString();
                String email = correo.getText().toString();
                String contra = contrasena.getText().toString();

                if (nombree.isEmpty() || apellido.isEmpty() || email.isEmpty() || contra.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realizar el registro en la base de datos (aquí debes implementar tu lógica de base de datos)
                boolean exitoRegistro = registrarUsuario(nombree, apellido, email, contra);

                if (exitoRegistro) {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    // Aquí puedes redirigir al usuario a otra actividad o realizar alguna acción adicional
                    Intent intent = new Intent(Registro.this, Sesion.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Registro.this, "Error en el registro. Inténtalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean registrarUsuario(String nombree, String apellido, String email, String contra) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombree);
        values.put("correoelectronico", email);
        values.put("contrasena", contra);
        values.put("apellido", apellido);

        long result = db.insert("usuarios", null, values);

        db.close();

        return result != -1;
    }

    private void siguiente(){
        Intent intent = new Intent(this, Sesion.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Registro.this, Sesion.class);
        startActivity(intent);
        finish();
    }

}