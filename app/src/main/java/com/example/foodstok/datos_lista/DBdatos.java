package com.example.foodstok.datos_lista;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.foodstok.DatabaseHelper;
import com.example.foodstok.lista_agregar;

import java.util.ArrayList;

public class DBdatos extends DatabaseHelper {

    Context context;

    public DBdatos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    
    public ArrayList<datos> mostrarContactos() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ArrayList<datos> listaDatos = new ArrayList<>();
        datos contacto;
        Cursor cursordatos;


        cursordatos = database.rawQuery("SELECT * FROM " + "productos" + " ORDER BY idproducto ASC", null);

        if (cursordatos.moveToFirst()) {
            do {
                contacto = new datos();
                contacto.setId(cursordatos.getInt(0));
                contacto.setNombre(cursordatos.getString(1));
                contacto.setCantidad(cursordatos.getString(2));
                listaDatos.add(contacto);
            } while (cursordatos.moveToNext());
        }

        cursordatos.close();

        return listaDatos;
    }

    /*public datos mostrarDatostt(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        datos contacto = null;
        Cursor cursordatos;


        cursordatos = database.rawQuery("SELECT * FROM " + "productos" + " WHERE idproducto =  " + id + " LIMIT 1", null);

        if (cursordatos.moveToFirst()) {
                contacto = new datos();
                contacto.setNombre(cursordatos.getString(0));
                contacto.setCantidad(cursordatos.getString(1));
                contacto.setId(cursordatos.getInt(2));
        }

        cursordatos.close();

        return contacto;
    }*/

    public ArrayList<datos> mostrarDatos(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ArrayList<datos> m_Datos = new ArrayList<>();
        datos contacto;
        Cursor cursordatos;


        cursordatos = database.rawQuery("SELECT * FROM " + "productos" + " WHERE idproducto =  " + id, null);

        if (cursordatos.moveToFirst()) {
            do {
                contacto = new datos();
                contacto.setId(cursordatos.getInt(0));
                contacto.setNombre(cursordatos.getString(1));
                contacto.setCantidad(cursordatos.getString(2));

                m_Datos.add(contacto);
            } while (cursordatos.moveToNext());
        }

        cursordatos.close();

        return m_Datos;
    }


    public boolean elimardatos(int id){
        boolean datos = false;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        try {
            database.execSQL("DELETE FROM " + "productos" + " WHERE idproducto = '" + id + "'");
            datos = true;
        }catch (Exception exception){
            exception.toString();
            datos = false;
        }finally {
            database.close();
        }

        return datos;
    }
}
