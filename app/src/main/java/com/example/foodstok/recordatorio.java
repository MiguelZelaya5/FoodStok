package com.example.foodstok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class recordatorio extends AppCompatActivity {

    private EditText editTextStartDate;
    private RadioButton radioButton155;
    private RadioButton radioButton311;
    private RadioButton radioButton77;
    private RadioGroup radioGroup;
    private TextView textViewInicioMes;
    private TextView textViewPlazoDias;
    private Calendar calendar;
    private EditText numero;
    private Handler handler;
    private Runnable showReminderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);

        editTextStartDate = findViewById(R.id.editTextStartDate);
        textViewInicioMes = findViewById(R.id.textViewInicioMes);
        textViewPlazoDias = findViewById(R.id.textViewPlazoDias);
        radioButton155 = findViewById(R.id.radioButton15);
        radioButton311 = findViewById(R.id.radioButton31);
        radioButton77 = findViewById(R.id.radioButton7);
        numero = findViewById(R.id.editTextNumber);
        ImageButton imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        radioGroup = findViewById(R.id.radioGroup);
        calendar = Calendar.getInstance();
        handler = new Handler();

        // Inicializar el Runnable para mostrar el recordatorio
        showReminderRunnable = new Runnable() {
            @Override
            public void run() {
                showReminderDialog();
            }
        };

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        numero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero1 = numero.getText().toString().trim();

                if (numero1.isEmpty()) {
                    radioButton155.setEnabled(true);
                    radioButton311.setEnabled(true);
                    radioButton77.setEnabled(true);
                } else {
                    radioButton155.setEnabled(false);
                    radioButton311.setEnabled(false);
                    radioButton77.setEnabled(false);
                }
            }
        });

        // Configurar el click listener del botón "Guardar preferencias"
        Button buttonGuardarPreferencias = findViewById(R.id.buttonGuardarPreferencias);
        buttonGuardarPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });

        // Obtener los valores de inicio de mes y plazo de días desde SharedPreferences
        getPreferencesFromSharedPreferences();
    }

    private void showDatePickerDialog() {
        // Crear el DatePickerDialog con la fecha actual como fecha inicial
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Actualizar la fecha seleccionada en el objeto Calendar
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Formatear la fecha seleccionada en el formato deseado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextStartDate.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private void savePreferences() {

        int plazoRegistros = -1;
        // Verificar si el EditText tiene un valor válido
        String editTextValue = numero.getText().toString().trim();
        if (!editTextValue.isEmpty()) {
            try {
                plazoRegistros = Integer.parseInt(editTextValue);
            } catch (NumberFormatException e) {
                // Manejar la excepción si el valor no es un número válido
                Toast.makeText(this, "Ingrese un valor válido en el campo de días", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton15) {
            plazoRegistros = 15;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton31) {
            plazoRegistros = 31;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton7) {
            plazoRegistros = 7;
        }

        String diaInicioMes = editTextStartDate.getText().toString();

        // Validar si se seleccionaron valores válidos
        if (plazoRegistros != -1 && !diaInicioMes.isEmpty()) {
            // Obtener la fecha actual
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);

            // Obtener la fecha seleccionada
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date selectedDate;
            try {
                selectedDate = dateFormat.parse(diaInicioMes);
            } catch (ParseException e) {
                selectedDate = null;
            }

            // Comparar las fechas
            if (selectedDate != null && !selectedDate.before(currentDate.getTime())) {
                // Guardar los valores en SharedPreferences en lugar de la base de datos
                savePreferencesInSharedPreferences(plazoRegistros, diaInicioMes);
                Toast.makeText(this, "Preferencias guardadas correctamente", Toast.LENGTH_SHORT).show();

                // Actualizar los TextView con los valores seleccionados
                textViewInicioMes.setText("Día de inicio: " + diaInicioMes);
                textViewPlazoDias.setText("Plazo de días: " + plazoRegistros);
                textViewInicioMes.setVisibility(View.VISIBLE);
                textViewPlazoDias.setVisibility(View.VISIBLE);

                // Programar la tarea para mostrar el recordatorio después del plazo de días
                if (plazoRegistros > 0) {
                    scheduleReminder(plazoRegistros, selectedDate);
                }
                Intent intent = new Intent(recordatorio.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "La fecha seleccionada debe ser igual o posterior a la fecha actual", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, selecciona valores válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder(int plazoRegistros, Date selectedDate) {
        if (selectedDate != null) {
            // Crear un objeto Calendar con la fecha seleccionada
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(selectedDate);

            // Sumar el plazo de días a la fecha de inicio
            startDate.add(Calendar.DAY_OF_MONTH, plazoRegistros);

            // Calcular la diferencia en milisegundos entre la fecha actual y la fecha calculada
            long timeDifference = startDate.getTimeInMillis() - System.currentTimeMillis();

            // Programar la tarea para mostrar el recordatorio después de la diferencia de tiempo
            handler.postDelayed(showReminderRunnable, timeDifference);
        }
    }

    private void savePreferencesInSharedPreferences(int plazoRegistros, String diaInicioMes) {
        // Guardar valores en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("plazo_registros", plazoRegistros);
        editor.putString("dia_inicio_mes", diaInicioMes);
        editor.apply();
    }

    private void getPreferencesFromSharedPreferences() {
        // Obtener valores desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE);
        int plazoRegistros = sharedPreferences.getInt("plazo_registros", -1);
        String diaInicioMes = sharedPreferences.getString("dia_inicio_mes", "");

        if (plazoRegistros != -1 && !diaInicioMes.isEmpty()) {
            // Actualizar los TextView con los valores seleccionados
            textViewInicioMes.setText("Día de inicio: " + diaInicioMes);
            textViewPlazoDias.setText("Plazo de días: " + plazoRegistros);
            textViewInicioMes.setVisibility(View.VISIBLE);
            textViewPlazoDias.setVisibility(View.VISIBLE);

            // Programar la tarea para mostrar el recordatorio después del plazo de días
            if (plazoRegistros > 0) {
                scheduleReminder(plazoRegistros, getDateFromString(diaInicioMes));
            }
        }
    }

    private Date getDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private void showReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Ya es día de compras!");
        builder.setMessage("¡Hoy es el día de hacer las compras que necesitas!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Puedes realizar acciones adicionales si es necesario
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        // Eliminar cualquier callback pendiente para evitar fugas de memoria
        handler.removeCallbacks(showReminderRunnable);
        super.onDestroy();
    }
}
