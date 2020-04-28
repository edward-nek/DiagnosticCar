package ru.nekrasoved.diagnosticcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiagnosticActivity extends AppCompatActivity {

    int N = 8; //количество признаков
    int K = 11; //количество диагнозов

    boolean[] checkLogs;
    float[][] diapasonLogs;

    DataDB dataDB;
    Cursor cursor;

    ImageView ivCheck;

    Button btHelp;

    String[] diagnos;
    int[][] parametr;
    ArrayList<Integer> spisokProblem;

    String myDiagnos = "";


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
        setContentView(R.layout.activity_diagnostic);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Результат диагностики: ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB001B")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkLogs = new boolean[N];
        diapasonLogs = new float[N][2];

        diagnos = new String[K];
        parametr = new int[K][N];
        spisokProblem = new ArrayList<>();

        for (int i = 0; i < N; i++){
            checkLogs[i] = false;
        }

        setDiapason();

        dataDB = new DataDB(this);
        getDiagnostic();

        setCheck();

        btHelp = (Button) findViewById(R.id.btHelp);
        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiagnos();
                getDiagnos();
                Toast.makeText(DiagnosticActivity.this, myDiagnos, Toast.LENGTH_LONG).show();
                myDiagnos = "";
            }
        });

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

                if ((cursor.getFloat(pressureWheelsIndex) < diapasonLogs[1][0])||(cursor.getFloat(pressureWheelsIndex) > diapasonLogs[1][1])){
                    checkLogs[1] = true;
                }

                if ((cursor.getFloat(voltageIndex) < diapasonLogs[2][0])||(cursor.getFloat(voltageIndex) > diapasonLogs[2][1])){
                    checkLogs[2] = true;
                }

                if ((cursor.getInt(temperatureIndex) < diapasonLogs[3][0])||(cursor.getInt(temperatureIndex) > diapasonLogs[3][1])){
                    checkLogs[3] = true;
                }

                if ((cursor.getFloat(gasConsumptionIndex) < diapasonLogs[4][0])||(cursor.getFloat(gasConsumptionIndex) > diapasonLogs[4][1])){
                    checkLogs[4] = true;
                }
                if ((cursor.getFloat(pressureOilIndex) < diapasonLogs[5][0])||(cursor.getFloat(pressureOilIndex) > diapasonLogs[5][1])){
                    checkLogs[5] = true;
                }
                if ((cursor.getInt(bienieRulyaIndex) < diapasonLogs[6][0])||(cursor.getInt(bienieRulyaIndex) > diapasonLogs[6][1])){
                    checkLogs[6] = true;
                }
                if ((cursor.getInt(carShocksIndex) < diapasonLogs[7][0])||(cursor.getInt(carShocksIndex) > diapasonLogs[7][1])){
                    checkLogs[7] = true;
                }

            } while (cursor.moveToNext());
        }

        for (int i = 0; i < N; i++){
            Log.d("checkLogs", i + ") " +checkLogs[i] + " ");
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

    public void setDiagnos() {

        for (int i = 0; i < N; i++){
            if (checkLogs[i]){
                spisokProblem.add(i);
            }
        }

        for (int i = 0; i < K; i++ ){
            for (int j = 0; j < N; j++){
                parametr[i][j] = -1;
            }
        }

        diagnos[0] = "Исправен";
        diagnos[1] = "Неисправен датчик положения дроссельной заслонки";
        parametr[1][0] = 0;
        diagnos[2] = "Спущенные колёса";
        parametr[2][0] = 1;
        diagnos[3] = "Неисправна работа генератора";
        parametr[3][0] = 2;
        diagnos[4] = "Неисправен масляный насос";
        parametr[4][0] = 3;
        parametr[4][1] = 5;
        diagnos[5] = "Отсутствует нужный уровень масла в ДВС";
        parametr[5][0] = 5;
        diagnos[6] = "Отсутствует нужный уровень антифриза в радиаторе";
        parametr[6][0] = 3;
        diagnos[7] = "Неисправен датчик лямбда";
        parametr[7][0] = 4;
        diagnos[8] = "Неисправна дроссельная заслонка";
        parametr[8][0] = 0;
        parametr[8][1] = 4;
        diagnos[9] = "Не выставлен развал-схождение";
        parametr[9][0] = 6;
        diagnos[10] = "Неисправна работа АКПП";
        parametr[10][0] = 7;

    }
    public void getDiagnos() {
        int j = 0;
        Log.d("Spisok-Problem", spisokProblem.toString());
        boolean check = true;
        for (int i = 1; i < K; i++){
            while (parametr[i][j] != -1){
                if (!spisokProblem.contains(parametr[i][j])){
                    check = false;
                }
                j++;
            }
            if (check){
                myDiagnos += diagnos[i] + "\n";
            }
            check = true;
            j = 0;
        }

        if (myDiagnos.length() == 0){
            myDiagnos += diagnos[0];
        }
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
