package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataDB dataDB;
    Cursor cursor;

    ListView listView;
    SimpleCursorAdapter scAdapter;

    Button btDiagnos;

    private static final int CM_DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Список данных: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));

        listView = (ListView) findViewById(R.id.lvDate);
        dataDB = new DataDB(this);
        dataDB.open();

        showList();

        btDiagnos = (Button) findViewById(R.id.btDiagnos);

        btDiagnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagnosticActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

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

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            dataDB.delRec(acmi.id);
            // обновляем курсор
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }


}
