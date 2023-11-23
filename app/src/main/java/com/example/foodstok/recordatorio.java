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

        Button buttonGuardarPreferencias = findViewById(R.id.buttonGuardarPreferencias);
        buttonGuardarPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });

        getPreferencesFromSharedPreferences();
    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextStartDate.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private void savePreferences() {

        int plazoRegistros = -1;
        String editTextValue = numero.getText().toString().trim();
        if (!editTextValue.isEmpty()) {
            try {
                plazoRegistros = Integer.parseInt(editTextValue);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.ingrese_un_valor_v_lido_en_el_campo_de_d_assss, Toast.LENGTH_SHORT).show();
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

        if (plazoRegistros != -1 && !diaInicioMes.isEmpty()) {
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date selectedDate;
            try {
                selectedDate = dateFormat.parse(diaInicioMes);
            } catch (ParseException e) {
                selectedDate = null;
            }

            if (selectedDate != null && !selectedDate.before(currentDate.getTime())) {
                savePreferencesInSharedPreferences(plazoRegistros, diaInicioMes);
                Toast.makeText(this, R.string.preferencias_guardadas_correctamentesss, Toast.LENGTH_SHORT).show();
                textViewInicioMes.setText(getString(R.string.d_a_de_inicio) + diaInicioMes);
                textViewPlazoDias.setText(getString(R.string.plazo_de_d_as) + plazoRegistros);
                textViewInicioMes.setVisibility(View.VISIBLE);
                textViewPlazoDias.setVisibility(View.VISIBLE);

                if (plazoRegistros > 0) {
                    scheduleReminder(plazoRegistros, selectedDate);
                }
                Intent intent = new Intent(recordatorio.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.la_fecha_seleccionada_debe_ser_igual_o_posterior_a_la_fecha_actual, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.por_favor_selecciona_valores_v_lidos, Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder(int plazoRegistros, Date selectedDate) {
        if (selectedDate != null) {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(selectedDate);

            startDate.add(Calendar.DAY_OF_MONTH, plazoRegistros);

            long timeDifference = startDate.getTimeInMillis() - System.currentTimeMillis();

            handler.postDelayed(showReminderRunnable, timeDifference);
        }
    }

    private void savePreferencesInSharedPreferences(int plazoRegistros, String diaInicioMes) {
        SharedPreferences sharedPreferences = getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("plazo_registros", plazoRegistros);
        editor.putString("dia_inicio_mes", diaInicioMes);
        editor.apply();
    }

    private void getPreferencesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE);
        int plazoRegistros = sharedPreferences.getInt("plazo_registros", -1);
        String diaInicioMes = sharedPreferences.getString("dia_inicio_mes", "");

        if (plazoRegistros != -1 && !diaInicioMes.isEmpty()) {
            textViewInicioMes.setText(getString(R.string.d_a_de_inicioss) + diaInicioMes);
            textViewPlazoDias.setText(getString(R.string.plazo_de_d_assss) + plazoRegistros);
            textViewInicioMes.setVisibility(View.VISIBLE);
            textViewPlazoDias.setVisibility(View.VISIBLE);

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
        builder.setTitle(R.string.ya_es_d_a_de_compras);
        builder.setMessage(R.string.hoy_es_el_d_a_de_hacer_las_compras_que_necesitasss);
        builder.setPositiveButton(R.string.oks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(showReminderRunnable);
        super.onDestroy();
    }
}
