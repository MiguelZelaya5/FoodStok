package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class Almacen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    private SharedPreferences sharedPreferences;
    LinearLayout exit,about,categoria,Almacen,home,salir;
    private Button menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);
        salir=findViewById(R.id.salir);

        int userId = getUserIdFromSharedPreferences();
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, MainActivity.class);
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }

            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, Categorias.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Almacen.this, lista_agregar.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Almacen.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

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

    private void logout() {
        // Realizar aquí las tareas de cierre de sesión, como borrar datos de sesión, etc.
        setLoggedIn(false); // Establecer el estado de inicio de sesión como falso o cerrado
        clearUserId(); // Borrar el ID del usuario guardado en SharedPreferences

        // Redirigir a la pantalla de inicio de sesión (Login)
        Intent intent = new Intent(Almacen.this, Sesion.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual (Inicio)
    }
    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idusuarios");
        editor.apply();
    }
    private int getUserIdFromSharedPreferences() {
        return sharedPreferences.getInt("idusuarios", -1);
    }

    //TextView textViewNoArticulos = findViewById(R.id.textViewNoArticulos);

        /* Aqui hacer lo de la BD para hacer que se desaparezca cuando se agg un nuevo producto
        boolean productoAgregado;
        if () {
            textViewNoArticulos.setVisibility(View.GONE);
        } else {
            textViewNoArticulos.setVisibility(View.VISIBLE);
        }*/
}
