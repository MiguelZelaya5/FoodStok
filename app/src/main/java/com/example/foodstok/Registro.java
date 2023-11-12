package com.example.foodstok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText nombre;
    EditText apellidos;
    EditText  numero;
    EditText  correo;
    EditText contrasena;
    Button btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombre=findViewById(R.id.nombre);
        apellidos=findViewById(R.id.ape);
        correo=findViewById(R.id.Correo);
        numero=findViewById(R.id.num);
        contrasena=findViewById(R.id.contra);
        btn_registro=findViewById(R.id.btn_iniciar);
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String name= nombre.getText().toString().toUpperCase();
        String ape= apellidos.getText().toString().toUpperCase();
        String number= numero.getText().toString().toUpperCase();
        String co= correo.getText().toString().toUpperCase();
        String pas= contrasena.getText().toString().toUpperCase();

        if(!name.isEmpty()&& !ape.isEmpty() && !number.isEmpty() && !co.isEmpty() && !pas.isEmpty()){
            if(isEmailValid(co)){
                if(pas.length()>= 5){
                    createUser(name, co, pas);
                }else{
                    Toast.makeText(Registro.this, "La contraseña debe ser mayor a 4 caracteres", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(Registro.this, "El correo no es valido", Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(Registro.this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUser(String name, String co, String pass) {

    }

    public boolean isEmailValid(String co){
        String expression ="/^w+@[a-zA-Z_]+?.[a-zA-Z] {2,3}$/";
        Pattern pattern= Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(co);
        return matcher.matches();
    }

    public void siguiente(View view){
        Intent siguiente = new Intent(this,Sesion.class);
        startActivity(siguiente);
    }

}