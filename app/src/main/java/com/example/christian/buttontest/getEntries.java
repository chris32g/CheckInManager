package com.example.christian.buttontest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_get_entries);
        longClickListener();
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
    public void longClickListener() {
        listViewCoches = findViewById(R.id.listView);
        listViewCoches.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
inflateList();
                return true;
            }
        });
    }

public void inflateList(){
    final String[] options = {"Modificar Entrada", "Eliminar Entrada", "Añadir a Lista de Transfers", "Añadir a llaves subidas", "Crear reporte de daños"};

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Elija una Opcion");
    builder.setItems(options, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
           /* switch(which){
                case 0:
                case 1:Intent trasnfI = new Intent(getEntries.this, Transfers.class);
                        startActivity(trasnfI);
                case 2:Intent listaLlavesI = new Intent(getEntries.this, ListaLlaves.class);
                    startActivity(listaLlavesI);
                case 3:picsErraser();
                    String subject = "parte " + getMatricula();
                    String bodyText = getDay() + " " + hora + "\n"
                            + getKm() + " km " + getFuel() + "/8" + "\n"
                            + getComents() + "\n" + "firmado";
                    Intent intnt = new Intent(Ingresos.this, DamagesActivity.class);
                    intnt.putExtra("subject",subject);
                    intnt.putExtra("bodyText",bodyText );
                    default:
            }*/
        }
    });
    builder.show();
}

    private void consultarlista(){
        String dia = '"' + getDay() +'"';
        lista = new ArrayList<String>();
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry2.CAMPO1, //matricula
                FeedReaderContract.FeedEntry2.CAMPO2, //modelo
                FeedReaderContract.FeedEntry2.CAMPO4, //numVh
                FeedReaderContract.FeedEntry2.CAMPO7, //hora
                FeedReaderContract.FeedEntry2.CAMPO8, //km
                FeedReaderContract.FeedEntry2.CAMPO9, //combustible
                FeedReaderContract.FeedEntry2.CAMPO6  //fecha
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

        int cursorLenght = cursor.getCount();
        cursor.moveToFirst();
            for(int i=0; i<cursorLenght;i++)
            {
                lista.add(cursor.getString(0) +"\n"
                        + "     " + cursor.getString(1)  + "    " + cursor.getString(2) + "    " + cursor.getString(4)+ "   " + cursor.getString(5)+ "/8  "+
                        "\n" + "     " + cursor.getString(3) +"   "  + cursor.getString(6));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            }

    public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }
/*
    public String getTransferData(){

        return getMatricula() + "," +
                numVh + "," +
                getContractNumber() + "," +
                getDay() + " " +
                getHour() + "," +
                getKm() + "," +
                destinacion + "," +
                getFuel() + "," +
                getDanos() + "," +
                getComents() + "," + "\n";
    }*/

    public void generateTransfer(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "TransfersHertz");
            String basicEntry =
                    "SEP=," + "\n" +
                            "Matricula" + "," +
                            "Numero" + "," +
                            "Contrato" + "," +
                            "Fecha" + "," +
                            "Hora" + "," +
                            "Kilometros" + "," +
                            "Destino" + "," +
                            "Combustible" + "," +
                            "Daños" + "," +
                            "Comentarios" + "," + "\n";
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            if (sFileName.length()==0 || gpxfile.length() == 0 || sFileName.isEmpty() || !gpxfile.exists()){
                FileWriter writer = new FileWriter(gpxfile, true);
                writer.append(basicEntry);
                writer.flush();
                writer.close();
            }
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDestinyAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getEntries.this);
        alertDialog.setTitle("Destino del Transfer");
        alertDialog.setMessage("Ingrese Destino del Transfer");
        final EditText input = new EditText(getEntries.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input); // uncomment this line
        alertDialog.setIcon(R.drawable.ic_my_location_black_24dp);
        input.setHint("Destino");
        alertDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String destino =input.getText().toString().trim();
                        if(destino.length()>=3){
                            Ingresos.destinacion=input.getText().toString().trim();
                            dialog.dismiss();
                        }else{}//
                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox checkTransfer = findViewById(R.id.checkTransfer);
                        checkTransfer.setChecked(false);
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
