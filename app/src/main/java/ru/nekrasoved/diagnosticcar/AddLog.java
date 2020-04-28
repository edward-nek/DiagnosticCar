package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddLog extends AppCompatActivity {

    EditText etDate;
    EditText etTime;
    EditText etKolesa;
    EditText etMaslo;
    EditText etAkkum;
    EditText etOboroty;
    EditText etBenzin;
    EditText etTemper;

    Button btSave;


    Calendar dateAndTime=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        etDate = (EditText) findViewById(R.id.etDate);
        etTime = (EditText) findViewById(R.id.etTime);
        etKolesa = (EditText) findViewById(R.id.etKolesa);
        etMaslo = (EditText) findViewById(R.id.etMaslo);
        etAkkum = (EditText) findViewById(R.id.etAkkum);
        etOboroty = (EditText) findViewById(R.id.etOboroty);
        etBenzin = (EditText) findViewById(R.id.etBenzin);
        etTemper = (EditText) findViewById(R.id.etTemper);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        btSave = (Button) findViewById(R.id.btSave);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etDate.getText().toString().length() > 0)&&(etTime.getText().toString().length() > 0)&&
                        (etKolesa.getText().toString().length() > 0)&&(etMaslo.getText().toString().length() > 0)&&
                        (etAkkum.getText().toString().length() > 0) &&(etOboroty.getText().toString().length() > 0)&&
                        (etBenzin.getText().toString().length() > 0)&&(etTemper.getText().toString().length() > 0)) {

                }
                else {
                    Toast.makeText(AddLog.this,"Для начала заполните поля!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void setDate() {
        new DatePickerDialog(AddLog.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime() {
        new TimePickerDialog(AddLog.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            etTime.setText(hourOfDay + ":" + minute);
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            etDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        }
    };

}
