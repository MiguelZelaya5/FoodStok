package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info =(TextView) findViewById(R.id.info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuprincipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            info.setText("Se eligió la opción Agregar");
            return true;
        } else if (item.getItemId() == R.id.search) {
            info.setText("Se eligió la opción Buscar");
            return true;
        } else if (item.getItemId() == R.id.edit) {
            info.setText("Se eligió la opción Editar");
            return true;
        } else if (item.getItemId() == R.id.delete) {
            info.setText("Se eligió la opción Eliminar");
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            info.setText("Se eligió la opción Ajustes");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}