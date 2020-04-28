package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataDB dataDB;
    Cursor cursor;

    ListView listView;
    SimpleCursorAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Список данных: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));

        listView = (ListView) findViewById(R.id.lvDate);

        showList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddLog.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        return true;
    }

    public void showList() {
        dataDB = new DataDB(this);
        dataDB.open();
        cursor = dataDB.getAllData();

        // формируем столбцы сопоставления
        String[] from = new String[] { DataDB.KEY_ID, DataDB.KEY_DATE,  DataDB.KEY_OBOROTY_DVS,
                DataDB.KEY_PRESSURE_WHEELS, DataDB.KEY_VOLTAGE, DataDB.KEY_TEMPERATURE,
                DataDB.KEY_GAS_CONSUMPTION, DataDB.KEY_PRESSURE_OIL, DataDB.KEY_BIENIE_RULYA,
                DataDB.KEY_CAR_SHOCKS};
        int[] to = new int[] {R.id.tvName, R.id.tvDate, R.id.tvOboroty, R.id.tvKolesa, R.id.tvAkkum,
        R.id.tvTemper, R.id.tvBenzin, R.id.tvMaslo, R.id.tvRul, R.id.tvCar};

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);

        listView.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(listView);
    }

}
