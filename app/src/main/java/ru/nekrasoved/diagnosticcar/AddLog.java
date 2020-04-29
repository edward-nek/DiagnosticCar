package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

    TextView tvHelp;

    Button btSave;

    SwitchCompat swRul;
    SwitchCompat swCar;

    DataDB dataDB;
    Cursor cursor;


    Calendar dateAndTime=Calendar.getInstance();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //скрыть панель навигации начало

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);

        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });

        //скрыть панель навигации конец

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Добавление данных: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvHelp = (TextView) findViewById(R.id.tvHelp);

        etDate = (EditText) findViewById(R.id.etDate);
        etTime = (EditText) findViewById(R.id.etTime);

        etKolesa = (EditText) findViewById(R.id.etKolesa);
        etKolesa.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Давление в шинах - Норма [ 2.0 ; 2.2 ]");
                return false;
            }
        });

        etMaslo = (EditText) findViewById(R.id.etMaslo);
        etMaslo.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Давление масла - Норма [ 3.5 ; 7.0 ]");
                return false;
            }
        });


        etAkkum = (EditText) findViewById(R.id.etAkkum);
        etAkkum.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Напряжение в сети - Норма [ 13.5 ; 14.5 ]");
                return false;
            }
        });


        etOboroty = (EditText) findViewById(R.id.etOboroty);
        etOboroty.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Обороты двигателя - Норма [ 650 ; 800 ]");
                return false;
            }
        });


        etBenzin = (EditText) findViewById(R.id.etBenzin);
        etBenzin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Расход бензина - Норма [ 0.0 ; 16.0 ]");
                return false;
            }
        });

        etTemper = (EditText) findViewById(R.id.etTemper);
        etTemper.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvHelp.setText("Температура ДВС - Норма [ 0 ; 100 ]");
                return false;
            }
        });

        swRul = (SwitchCompat) findViewById(R.id.swRul);
        swCar = (SwitchCompat) findViewById(R.id.swCar);



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
                boolean check;


                if ((etDate.getText().toString().length() > 0)&&(etTime.getText().toString().length() > 0)&&
                        (etKolesa.getText().toString().length() > 0)&&(etMaslo.getText().toString().length() > 0)&&
                        (etAkkum.getText().toString().length() > 0) &&(etOboroty.getText().toString().length() > 0)&&
                        (etBenzin.getText().toString().length() > 0)&&(etTemper.getText().toString().length() > 0)) {

                    if (checkInput()){
                        saveLog();

                        Intent intent = new Intent(AddLog.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
                else {
                    Toast.makeText(AddLog.this,"Для начала заполните поля!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void saveLog(){
        dataDB = new DataDB(this);
        dataDB.open();

        String date = etDate.getText().toString() + " " + etTime.getText().toString();
        int oboroty_dvs = Integer.valueOf(etOboroty.getText().toString());
        float pressure_wheels = Float.valueOf(etKolesa.getText().toString());
        float voltage = Float.valueOf(etAkkum.getText().toString());
        int temperature = Integer.valueOf(etTemper.getText().toString());
        float gas_consumption = Float.valueOf(etBenzin.getText().toString());
        float pressure_oil = Float.valueOf(etMaslo.getText().toString());

        boolean bienie_rulya = swRul.isChecked();
        boolean car_shocks = swCar.isChecked();

        dataDB.addRec(date, oboroty_dvs, pressure_wheels, voltage, temperature, gas_consumption,
                pressure_oil, bienie_rulya, car_shocks);

        cursor = dataDB.getAllData();

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DataDB.KEY_ID);
            int dateIndex = cursor.getColumnIndex(DataDB.KEY_DATE);
            int oborotyDvsIndex = cursor.getColumnIndex(DataDB.KEY_OBOROTY_DVS);
            int pressureWheelsIndex = cursor.getColumnIndex(DataDB.KEY_PRESSURE_WHEELS);
            int voltageIndex = cursor.getColumnIndex(DataDB.KEY_VOLTAGE);
            int temperatureIndex = cursor.getColumnIndex(DataDB.KEY_TEMPERATURE);
            int gasConsumptionIndex = cursor.getColumnIndex(DataDB.KEY_GAS_CONSUMPTION);
            int pressureOilIndex = cursor.getColumnIndex(DataDB.KEY_PRESSURE_OIL);
            int bienieRulyaIndex = cursor.getColumnIndex(DataDB.KEY_BIENIE_RULYA);
            int carShocksIndex = cursor.getColumnIndex(DataDB.KEY_CAR_SHOCKS);

            do {
                Log.d("saveLog", "id = " + cursor.getInt(idIndex) + "  " +
                        "Дата = " + cursor.getInt(dateIndex) + "  " +
                        "Обороты ДВС = " + cursor.getInt(oborotyDvsIndex) + "  " +
                        "Давление колес = " + cursor.getInt(pressureWheelsIndex) + "  " +
                        "Напряжение аккумулятора = " + cursor.getInt(voltageIndex) + "  " +
                        "Температура двигателя = " + cursor.getInt(temperatureIndex) + "  " +
                        "Расход топлива = " + cursor.getInt(gasConsumptionIndex) + "  " +
                        "Давление масла = " + cursor.getInt(pressureOilIndex) + "  " +
                        "Биение руля = " + cursor.getInt(bienieRulyaIndex) + "  " +
                        "Толчки авто = " + cursor.getInt(carShocksIndex) + "  ");
            } while (cursor.moveToNext());
        }
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

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            etTime.setText(hourOfDay + ":" + minute);
        }
    };

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            etDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        }
    };

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(AddLog.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        return true;
    }

    public boolean checkInput(){
        boolean check = true;
        String errorLog = "Измените вводимые данные: \n";

        float oboroty = Float.valueOf(etOboroty.getText().toString());
        float kolesa = Float.valueOf(etKolesa.getText().toString());
        float akkum = Float.valueOf(etAkkum.getText().toString());
        float temper = Float.valueOf(etTemper.getText().toString());
        float benzin = Float.valueOf(etBenzin.getText().toString());
        float maslo = Float.valueOf(etMaslo.getText().toString());
        if (oboroty > 8000){
            check = false;
            errorLog += "Обороты двигателя <= 8000" + "\n";
        }
        if (kolesa > 3){
            check = false;
            errorLog += "Давление в шинах <= 3" + "\n";
        }
        if (akkum > 20){
            check = false;
            errorLog += "Напряжение в сети <= 20" + "\n";
        }
        if (temper > 150){
            check = false;
            errorLog += "Температура двигателя <= 150" + "\n";
        }
        if (benzin > 30){
            check = false;
            errorLog += "Расход бензина <= 30" + "\n";
        }
        if (maslo > 10){
            check = false;
            errorLog += "Давление масла < 10" + "\n";
        }

        if (!check){
            Toast.makeText(AddLog.this, errorLog, Toast.LENGTH_LONG).show();
        }
        return check;
    }

    //отслеживание нажатий на экран

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
