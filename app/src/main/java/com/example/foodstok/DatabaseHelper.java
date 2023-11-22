package com.example.foodstok;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodstoks";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (idusuarios INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(45), correoelectronico VARCHAR(45), contrasena VARCHAR(45), apellido VARCHAR(45));");
        db.execSQL("CREATE TABLE productos (idproducto INTEGER PRIMARY KEY AUTOINCREMENT, nombreproducto VARCHAR(45), cantidad INTEGER, idusuario INTEGER, FOREIGN KEY (idusuario) REFERENCES usuarios(idusuarios));");
        db.execSQL("CREATE TABLE articulos (idarticulo INTEGER PRIMARY KEY AUTOINCREMENT, categoria VARCHAR(45), nombrearticulo VARCHAR(45), fechafabricacion DATE, fechacaducidad DATE, cantidad INTEGER, foto BLOB, id_usuario INTEGER, AlmacenM VARCHAR(45), FOREIGN KEY (id_usuario) REFERENCES usuarios(idusuarios));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



}
