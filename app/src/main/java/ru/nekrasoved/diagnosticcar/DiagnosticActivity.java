package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class DiagnosticActivity extends AppCompatActivity {

    int N = 8; //количество характеристик у записи

    boolean[] checkLogs;
    float[][] diapasonLogs;

    DataDB dataDB;
    Cursor cursor;

    ImageView ivCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Результат диагностики: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkLogs = new boolean[N];
        diapasonLogs = new float[N][2];

        for (int i = 0; i < N; i++){
            checkLogs[i] = false;
        }
        setDiapason();

        dataDB = new DataDB(this);
        getDiagnostic();

        setCheck();


    }

    public void getDiagnostic() {
        dataDB.open();
        cursor = dataDB.getAllData();
        if (cursor.moveToFirst()){
            int oborotyDvsIndex = cursor.getColumnIndex(DataDB.KEY_OBOROTY_DVS);
            int pressureWheelsIndex = cursor.getColumnIndex(DataDB.KEY_PRESSURE_WHEELS);
            int voltageIndex = cursor.getColumnIndex(DataDB.KEY_VOLTAGE);
            int temperatureIndex = cursor.getColumnIndex(DataDB.KEY_TEMPERATURE);
            int gasConsumptionIndex = cursor.getColumnIndex(DataDB.KEY_GAS_CONSUMPTION);
            int pressureOilIndex = cursor.getColumnIndex(DataDB.KEY_PRESSURE_OIL);
            int bienieRulyaIndex = cursor.getColumnIndex(DataDB.KEY_BIENIE_RULYA);
            int carShocksIndex = cursor.getColumnIndex(DataDB.KEY_CAR_SHOCKS);

            do {
                if ((cursor.getInt(oborotyDvsIndex) < diapasonLogs[0][0])||(cursor.getInt(oborotyDvsIndex) > diapasonLogs[0][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(pressureWheelsIndex) < diapasonLogs[1][0])||(cursor.getInt(pressureWheelsIndex) > diapasonLogs[1][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(voltageIndex) < diapasonLogs[2][0])||(cursor.getInt(voltageIndex) > diapasonLogs[2][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(temperatureIndex) < diapasonLogs[3][0])||(cursor.getInt(temperatureIndex) > diapasonLogs[3][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(gasConsumptionIndex) < diapasonLogs[4][0])||(cursor.getInt(gasConsumptionIndex) > diapasonLogs[4][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(pressureOilIndex) < diapasonLogs[5][0])||(cursor.getInt(pressureOilIndex) > diapasonLogs[5][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(bienieRulyaIndex) < diapasonLogs[6][0])||(cursor.getInt(oborotyDvsIndex) > diapasonLogs[6][1])){
                    checkLogs[0] = true;
                }
                if ((cursor.getInt(carShocksIndex) < diapasonLogs[7][0])||(cursor.getInt(carShocksIndex) > diapasonLogs[7][1])){
                    checkLogs[0] = true;
                }
            } while (cursor.moveToNext());
        }

        for (int i = 0; i < N; i++){
            Log.d("checkLogs", checkLogs[i] + " ");
        }

    }

    public void setDiapason() {
        diapasonLogs[0][0] = (float) 650;
        diapasonLogs[0][1] = (float) 800;
        diapasonLogs[1][0] = (float) 2.0;
        diapasonLogs[1][1] = (float) 2.2;
        diapasonLogs[2][0] = (float) 13.5;
        diapasonLogs[2][1] = (float) 14.5;
        diapasonLogs[3][0] = (float) 0;
        diapasonLogs[3][1] = (float) 100;
        diapasonLogs[4][0] = (float) 0;
        diapasonLogs[4][1] = (float) 16;
        diapasonLogs[5][0] = (float) 3.5;
        diapasonLogs[5][1] = (float) 7;
        diapasonLogs[6][0] = (float) 0;
        diapasonLogs[6][1] = (float) 0;
        diapasonLogs[7][0] = (float) 0;
        diapasonLogs[7][1] = (float) 0;
    }

    public void setCheck() {
        ivCheck = (ImageView) findViewById(R.id.ivCheck0);
        if (checkLogs[0]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck1);
        if (checkLogs[1]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck2);
        if (checkLogs[2]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck3);
        if (checkLogs[3]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck4);
        if (checkLogs[4]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck5);
        if (checkLogs[5]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck6);
        if (checkLogs[6]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }

        ivCheck = (ImageView) findViewById(R.id.ivCheck7);
        if (checkLogs[7]){
            ivCheck.setImageResource(R.drawable.check_red);
        }
        else{
            ivCheck.setImageResource(R.drawable.check_green);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(DiagnosticActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        return true;
    }

}
