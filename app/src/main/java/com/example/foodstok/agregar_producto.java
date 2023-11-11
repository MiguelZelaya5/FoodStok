package com.example.foodstok;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

import com.example.foodstok.inventario.datos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.journeyapps.barcodescanner.CaptureActivity;
import android.content.Intent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class agregar_producto extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout exit,about,categoria,Almacen,home,addP;
    private Spinner spinner_categorias;
    private categorias_adapter adapter;
    private EditText etProductName, etQuantity;
    private TextView tvManufacturingDate, tvExpirationDate;
    private ImageView ivProductImage;
    private Button btnDecrease, btnIncrease, btnScanBarcode, btnAddProduct;

    private int mYear, mMonth, mDay;

    // Constante para la solicitud de captura de imagen
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // Constante para la solicitud de permiso de cámara
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        etProductName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        tvManufacturingDate = findViewById(R.id.tvManufacturingDate);
        tvExpirationDate = findViewById(R.id.tvExpirationDate);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        etQuantity.setText("0");

        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        Almacen=findViewById(R.id.Almacen);
        categoria=findViewById(R.id.categoria);

        fab = findViewById(R.id.addProducto);

        ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(agregar_producto.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(agregar_producto.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {

                    dispatchTakePictureIntent();
                }
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

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        spinner_categorias = findViewById(R.id.spinn_categorias);
        adapter = new categorias_adapter(agregar_producto.this, datos.getCatList());
        spinner_categorias.setAdapter(adapter);

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
            }
        });

        Almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(agregar_producto.this, Almacen.class);
            }
        });

        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click on Almacen button");
                redirectActivity(agregar_producto.this, Categorias.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(agregar_producto.this, "LogOut", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Valor no válido", Toast.LENGTH_SHORT).show();
        }
    }
    private void increaseQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            quantity++;
            etQuantity.setText(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            // Manejar la excepción aquí, por ejemplo, mostrar un mensaje de error
            Toast.makeText(this, "Valor no válido", Toast.LENGTH_SHORT).show();
        }
    }
    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanea un código de barras");
        integrator.setCameraId(0);  // Cámara trasera por defecto
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                String scannedBarcode = result.getContents();
                Toast.makeText(this, "Código de barras escaneado: " + scannedBarcode, Toast.LENGTH_SHORT).show();
                // Aquí puedes realizar otras acciones con el código de barras, como asignarlo a un EditText o almacenarlo en una variable.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void addProduct() {
        String productName = etProductName.getText().toString();
        String manufacturingDate = tvManufacturingDate.getText().toString();
        String expirationDate = tvExpirationDate.getText().toString();
        int quantity = Integer.parseInt(etQuantity.getText().toString());
        // Implementa tu lógica para agregar el producto a la base de datos u otras acciones aquí
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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

}