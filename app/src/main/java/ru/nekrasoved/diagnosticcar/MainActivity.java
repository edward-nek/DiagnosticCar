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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataDB dataDB;
    Cursor cursor;

    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Список данных: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));

        showList();
    }

    public void showList(){
        dataDB = new DataDB(this);
        dataDB.open();
        cursor = dataDB.getAllData();

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

}
