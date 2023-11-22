package com.example.foodstok;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

public class agregar_producto extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home,addP,salir;
    private EditText etProductName, etQuantity;
    private TextView tvManufacturingDate, tvExpirationDate;
    private Spinner spincategoria;
    private String imageFilePath = "";
    private ImageView imageView;
    private Button btnDecrease, btnIncrease, btnScanBarcode, btnAddProduct;

    private int mYear, mMonth, mDay;
    public String categoriatexto;

    // Constante para la solicitud de captura de imagen
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // Constante para la solicitud de permiso de cámara
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 3;
    private static final int PERMISSION_REQUEST_STORAGE = 4;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        sharedPreferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        etProductName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        tvManufacturingDate = findViewById(R.id.tvManufacturingDate);
        tvExpirationDate = findViewById(R.id.tvExpirationDate);
        imageView = findViewById(R.id.ivProductImage);
        btnDecrease = findViewById(R.id.btnDecrease);
        salir=findViewById(R.id.salir);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        etQuantity.setText("0");
        spincategoria=findViewById(R.id.etCategory);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);

        fab = findViewById(R.id.addProducto);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionOptions();
            }
        });

        tvManufacturingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvManufacturingDate);
            }
        });

        tvExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvExpirationDate);
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(agregar_producto.this, MainActivity.class);
                finish();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(agregar_producto.this, Categorias.class);
                finish();
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(agregar_producto.this, Almacen.class);
                finish();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(agregar_producto.this, lista_agregar.class);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(agregar_producto.this, "LogOut", Toast.LENGTH_SHORT).show();
                logout();
            }
        });

    }

    private void showDatePicker(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(agregar_producto.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        textView.setText(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void decreaseQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            if (quantity > 0) {
                quantity--;
                etQuantity.setText(String.valueOf(quantity));
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción aquí, por ejemplo, mostrar un mensaje de error
            Toast.makeText(this, R.string.valor_no_v_lido, Toast.LENGTH_SHORT).show();
        }
    }
    private void increaseQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            quantity++;
            etQuantity.setText(String.valueOf(quantity));
        } catch (NumberFormatException e) {

            Toast.makeText(this, R.string.valor_no_v_lido, Toast.LENGTH_SHORT).show();
        }
    }
    //private void scanBarcode() {
     //   IntentIntegrator integrator = new IntentIntegrator(this);
     //   integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
      //  integrator.setPrompt("Escanea un código de barras");
      //  integrator.setCameraId(0);  // Cámara trasera por defecto
      //  integrator.initiateScan();
   // }
    private void addProduct() {

        this.categoriaspiner();
        String nombreproducto=etProductName.getText().toString();
        if (nombreproducto.trim().isEmpty()) {

            Toast.makeText(this, R.string.por_favor_introduce_un_nombre_de_producto_v_lido, Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(etQuantity.getText().toString());
        if (cantidad==0) {

            Toast.makeText(this, R.string.por_favor_introduce_la_cantidad, Toast.LENGTH_SHORT).show();
            return;
        }
        if (nombreproducto.trim().isEmpty()) {
            Toast.makeText(this, R.string.por_favor_introduce_un_nombre_de_producto_v_lido, Toast.LENGTH_SHORT).show();
            return;
        }


        String fechafabricacion=tvManufacturingDate.getText().toString();
        String fechacaducidad=tvExpirationDate.getText().toString();

        if (fechafabricacion.trim().isEmpty()) {
            // Manejar el caso en que la fecha de fabricación esté vacía
            Toast.makeText(this, "Ingresa la fecha de fabricación", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fechacaducidad.trim().isEmpty()) {
            // Manejar el caso en que la fecha de caducidad esté vacía
            Toast.makeText(this, "Ingresa la fecha de caducidad", Toast.LENGTH_SHORT).show();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate fechaFabricacion = LocalDate.parse(fechafabricacion, formatter);
            LocalDate fechaCaducidad = LocalDate.parse(fechacaducidad, formatter);

            if (fechaCaducidad.isBefore(fechaFabricacion)) {
                Toast.makeText(this, "Fecha de caducidad no válida", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (DateTimeParseException e) {
            // Manejar la excepción si las fechas no pueden ser parseadas
            e.printStackTrace();
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }


        String categoria=categoriatexto;
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        byte[] imagenCategoria = obtenerBytesDeImagen();
        int userId = getUserIdFromSharedPreferences();
        ContentValues values = new ContentValues();
        values.put("categoria", categoria);
        values.put("nombrearticulo", nombreproducto);
        values.put("fechafabricacion", fechafabricacion);
        values.put("fechacaducidad", fechacaducidad);
        values.put("cantidad", cantidad);
        values.put("foto", imagenCategoria);
        values.put("id_usuario", userId);
        long resultado = database.insert("articulos", null, values);
        if (resultado != -1) {
            Toast.makeText(this, R.string.producto_agregado_correctamente, Toast.LENGTH_SHORT).show();
            etProductName.setText("");
            etQuantity.setText("0");
            tvManufacturingDate.setText("");
            tvExpirationDate.setText("");
            imageView.setImageResource(R.drawable.foto);
        } else {
            Toast.makeText(this, R.string.error_al_agregar_el_producto, Toast.LENGTH_SHORT).show();
        }

        database.close();
    }



    public void categoriaspiner(){
        String escala=spincategoria.getSelectedItem().toString();
        if (escala.equals(getString(R.string.carnes1))) {
            categoriatexto=getString(R.string.carnes1);

        } else if (escala.equals(getString(R.string.lacteos1))) {

            categoriatexto=getString(R.string.lacteos1);

        } else if (escala.equals(getString(R.string.vegetales1))) {

            categoriatexto=getString(R.string.vegetales1);

        } else if (escala.equals(getString(R.string.fruta1))) {

            categoriatexto=getString(R.string.fruta1);

        }else if(escala.equals(getString(R.string.aderezo1))){
            categoriatexto=getString(R.string.aderezo1);
        }else if(escala.equals(getString(R.string.salsas1))){
        categoriatexto=getString(R.string.salsas1);
        }else if(escala.equals(getString(R.string.productos_enlatados1))){
            categoriatexto=getString(R.string.productos_enlatados1);
        }else if(escala.equals(getString(R.string.bebidas1))){
            categoriatexto=getString(R.string.bebidas1);
        }else if(escala.equals(getString(R.string.harinas1))){
            categoriatexto=getString(R.string.harinas1);
        } else if (escala.equals(getString(R.string.otros1))) {
            categoriatexto=getString(R.string.otros1);
        }
    }
    private void showImageSelectionOptions() {
        String[] options = {getString(R.string.tomar_fotograf_a)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.seleccionar_imagen);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    checkCameraPermission();
                } else if (which == 1) {
                    checkStoragePermission();
                }
            }
        });
        builder.show();
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        } else {
            dispatchTakePictureIntent();
        }
    }
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        } else {
            dispatchPickImageIntent();
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            showSelectedImage(imageBitmap);
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                showSelectedImage(selectedImageUri);
            }
        }
    }
    private void showSelectedImage(Bitmap imageBitmap) {
        imageView.setImageBitmap(imageBitmap);
    }

    private void showSelectedImage(Uri selectedImageUri) {
        Glide.with(this)
                .load(selectedImageUri)
                .into(imageView);
    }
    private byte[] obtenerBytesDeImagen() {
        Bitmap bitmap = null;
        try {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickImageIntent();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
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
    private int getUserIdFromSharedPreferences() {
        return sharedPreferences.getInt("idusuarios", -1);
    }
    private void logout() {
        // Realizar aquí las tareas de cierre de sesión, como borrar datos de sesión, etc.
        setLoggedIn(false); // Establecer el estado de inicio de sesión como falso o cerrado
        clearUserId(); // Borrar el ID del usuario guardado en SharedPreferences

        // Redirigir a la pantalla de inicio de sesión (Login)
        Intent intent = new Intent(agregar_producto.this, Sesion.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual (Inicio)
    }
    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idusuarios");
        editor.apply();
    }
    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

}