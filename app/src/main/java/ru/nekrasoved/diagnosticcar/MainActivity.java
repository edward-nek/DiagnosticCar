package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
        cursor = dataDB.getAllData();

        list = new ArrayList<>();

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DataDB.KEY_ID);
            int dateIndex = cursor.getColumnIndex(DataDB.KEY_DATE);


            do {
                list.add(idIndex,  cursor.getString(idIndex) +
                        ") " + cursor.getString(dateIndex) + " ");

            } while (cursor.moveToNext());
        }



    }

}
