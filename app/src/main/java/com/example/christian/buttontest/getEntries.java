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
import android.widget.SearchView;
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
        SearchView buscador = findViewById(R.id.searchView);
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        consultarlista();
        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listViewCoches.setAdapter(adaptador);
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adaptador.getFilter().filter(s);
                return false;
            }
        });
    }

    private void consultarlista(){
        String dia = '"' + getDay() +'"';
        lista = new ArrayList<String>();
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry2.CAMPO1,
                FeedReaderContract.FeedEntry2.CAMPO2,
                FeedReaderContract.FeedEntry2.CAMPO4,
                FeedReaderContract.FeedEntry2.CAMPO7,
                FeedReaderContract.FeedEntry2.CAMPO8,
                FeedReaderContract.FeedEntry2.CAMPO9,
                FeedReaderContract.FeedEntry2.CAMPO6
        };
        String selection = "";//FeedReaderContract.FeedEntry1.CAMPO1 + " = ?";
        String[] selectionArgs = {dia};
        Cursor cursor = database.query(
                FeedReaderContract.FeedEntry2.TABLE2_NAME,                       // The table to query
                projection,                                                      // The array of columns to return (pass null to get all)
                selection,                                                      // The columns for the WHERE clause
                null,//selectionArgs,                              // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                FeedReaderContract.FeedEntry2.CAMPO6 + " DESC," +
                        FeedReaderContract.FeedEntry2.CAMPO7 + " DESC"          // The sort order
        );

        /*Cursor cursor = database.rawQuery("SELECT matricula, modelo, NumeroVehiculo, NumeroDeContrato," +
                " HoraEntrada, KilometrosEntrada, NivelDeCombustible, NuevosDaños, Comentarios, FechaEntrada FROM DatosChekins",
                null, null, FechaEntrada + "DESC");/*" WHERE FechaEntrada = ? ",new String[]{dia});*/
        int cursorLenght = cursor.getCount();
        cursor.moveToFirst();
            for(int i=0; i<cursorLenght;i++)
            {
                lista.add(cursor.getString(0) +"\n"
                        + cursor.getString(1)  + "   " + cursor.getString(2) + "    " + cursor.getString(3) + "    " + cursor.getString(4)+ "/8   " + cursor.getString(5)+ "    " +cursor.getString(6));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            }

        public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }
}
