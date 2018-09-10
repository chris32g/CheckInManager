package com.example.christian.buttontest;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class getEntries extends AppCompatActivity {


    ArrayList<String>lista;
    ArrayList<String>matriculas;
    ArrayList<String>modelo;
    ListView listViewCoches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_entries);
        listViewCoches = findViewById(R.id.listView);
        EditText day = findViewById(R.id.edtFecha);
        day.setText(getDay());
        onClickSelectDate();
        //FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        //consultarlista();
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listViewCoches.setAdapter(adaptador);
    }

    private void consultarlista(){
        String dia = '"' + getDay() +'"';
        lista = new ArrayList<String>();
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT matricula, modelo, NumeroVehiculo, NumeroDeContrato," +
                " HoraEntrada, KilometrosEntrada, NivelDeCombustible, NuevosDaños, Comentarios, FechaEntrada FROM DatosChekins WHERE FechaEntrada = ? ",new String[]{dia});
        int cursorLenght = cursor.getCount();
        cursor.moveToFirst();
            for(int i=0; i<cursorLenght;i++)
            {
                lista.add(cursor.getString(0) +"\n"
                        + cursor.getString(1)  + "   " + cursor.getString(4) + "    " + cursor.getString(5) + "    " + cursor.getString(6)+ "/8   " + cursor.getString(2));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            }

    public void onClickSelectDate() {
        EditText selectDate = findViewById(R.id.edtFecha);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.edtFecha:
                        showDatePickerDialog();
                        break;
                }
            }
        });
    }

    private void showDatePickerDialog() {
        final EditText etPlannedDate = findViewById(R.id.edtFecha);
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                etPlannedDate.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }
}
