package com.example.christian.buttontest;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ImageButton;
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

import static com.example.christian.buttontest.FeedReaderContract.FeedEntry2.TABLE2_NAME;

public class getEntries extends AppCompatActivity {


    ArrayList<String>lista;
    ArrayList<String>matriculas;
    ArrayList<String>modelo;
    ListView listViewCoches;
    String destinacion, numVh, contractNumber, day, hour, km, fuel, danos, coments;
    String matriculaItem ="";
    String seguntoTramo  ="";
    String tercerTramo   ="";
    String modelovh        ="";
    String numero        ="";
    String combustible   ="";
    String hora          ="";
    String fecha         ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_get_entries);
        backBtnListener();
        listViewCoches = findViewById(R.id.listView);
        SearchView buscador = findViewById(R.id.searchViewSubidas);
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        consultarlista();
        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listViewCoches.setAdapter(adaptador);
        longClickListener(adaptador);
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
    public void longClickListener(final ArrayAdapter adaptador) {
        listViewCoches = findViewById(R.id.listView);
        listViewCoches.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String itemString=arg0.getItemAtPosition(pos).toString();
                inflateList(pos, itemString, adaptador);
                return true;
            }
        });
    }

    public void inflateList(final int pos, String itemString, final  ArrayAdapter adaptador ){
    final String[] options = {"Eliminar Entrada", "Añadir a Lista de Transfers", "Añadir a llaves subidas", "Crear reporte de daños"};
    listViewCoches = findViewById(R.id.listView);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //String item = listViewCoches.getItemAtPosition(pos);
    matriculaItem = itemString.split("\n")[0];
    seguntoTramo = itemString.split("\n")[1];
    tercerTramo = itemString.split("\n")[2];
    modelovh = seguntoTramo.split("    ")[1];
    numero = seguntoTramo.split("    ")[2];
    combustible = seguntoTramo.split(" km  ")[1];
    hora = tercerTramo.split("    ")[1];
    fecha = tercerTramo.split("    ")[2];
    builder.setTitle("Elija una Opcion");
    builder.setItems(options, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch(which){
                case 0: delete(matriculaItem, fecha,hora);
                lista.clear();
                consultarlista();
                ArrayAdapter adaptador2 = new ArrayAdapter(getEntries.this, android.R.layout.simple_list_item_1, lista);
                listViewCoches.setAdapter(adaptador2);
                break;
                case 1:
                    getDestinyAlert();
                Toast.makeText(getEntries.this, "Coche" + matriculaItem + " agregado correctamente a transfers",Toast.LENGTH_LONG).show();
                break;
               case 2:
                   //Toast.makeText(getEntries.this,""+matriculaItem.trim() + " " + modelo.trim() + " " + numero.trim(),Toast.LENGTH_LONG).show();
                   addLlaveSubida(matriculaItem, numero, modelovh, getDay(),getHour() );
                   break;
                case 3:
                     getCarInfo(matriculaItem.trim(),fecha.trim(), hora.trim());
                    String subject = "Parte " + matriculaItem;
                    String bodyText = fecha + " " + hora + "\n" + km + " " + combustible + "\n" + coments + "\n" + "Firmado";
                    Intent i3 = new Intent(getEntries.this, DamagesActivity.class);
                    i3.putExtra("subject", subject);
                    i3.putExtra("bodyText", bodyText);
                    startActivity(i3);
                    break;
                
            }
        }
    });
    builder.show();
}
    public void addLlaveSubida(String matricula, String numero, String modelo, String fecha,String hora){
      FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
      SQLiteDatabase db = dbHelper.getWritableDatabase();
    // Create a new map of values, where column names are the keys
    ContentValues values = new ContentValues();
    values.put(FeedReaderContract.ListaSubidas.CAMPO1, matricula);
    values.put(FeedReaderContract.ListaSubidas.CAMPO2, numero);
    values.put(FeedReaderContract.ListaSubidas.CAMPO3, modelo);
    values.put(FeedReaderContract.ListaSubidas.CAMPO4, fecha);
    values.put(FeedReaderContract.ListaSubidas.CAMPO5, hora);

    // Insert the new row, returning the primary key value of the new row
    db.insert(FeedReaderContract.ListaSubidas.TABLE_S_NAME, null, values);
   db.close();

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
                TABLE2_NAME,                       // The table to query
                projection,                                                      // The array of columns to return (pass null to get all)
                selection,                                                      // The columns for the WHERE clause
                null,//selectionArgs,                              // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                FeedReaderContract.FeedEntry2.CAMPO6 + " DESC," +
                        FeedReaderContract.FeedEntry2.CAMPO7 + " DESC"          // The sort order
        );

        int cursorLength = cursor.getCount();
        cursor.moveToFirst();
            for(int i=0; i<cursorLength;i++)
            {
                lista.add(cursor.getString(0) +"\n"
                        + "     " + cursor.getString(1)  + "     " + cursor.getString(2) + "     " + cursor.getString(4)+ " km  " + cursor.getString(5)+ "/8  "+
                        "\n" + "     " + cursor.getString(3) +"     "  + cursor.getString(6));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            }

    public void delete(String id, String fecha, String hora) {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from " + "DatosChekins" + " WHERE " + "Matricula" + " ='" + id.trim() +"'"
                + " AND " + "FechaEntrada" + " ='" + fecha.trim() +"'"
                + " AND " + "HoraEntrada" + " ='" + hora.trim() +"';" );
        db.close();
        Toast.makeText(getEntries.this, "Entrada de Coche " + id + " eliminado correctamente",Toast.LENGTH_LONG).show();
    }

    public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }

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
                            "Danos" + "," +
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

    public void getCarInfo(String matricula, String fecha, String hora) {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getEntries.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                 FeedReaderContract.FeedEntry2.CAMPO1,  //matricula
                 FeedReaderContract.FeedEntry2.CAMPO5,  //numcontrato
                 FeedReaderContract.FeedEntry2.CAMPO6,  //dia
                 FeedReaderContract.FeedEntry2.CAMPO7,  //hora
                 FeedReaderContract.FeedEntry2.CAMPO10, //danos
                 FeedReaderContract.FeedEntry2.CAMPO15, //comentarios
                 FeedReaderContract.FeedEntry2.CAMPO8,  //km
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry1.CAMPO1 + "=? AND " + FeedReaderContract.FeedEntry2.CAMPO6 + "=? AND "+ FeedReaderContract.FeedEntry2.CAMPO7 + "=?";
        String[] selectionArgs = {matricula.trim(), fecha.trim(), hora.trim()};
        Cursor cursor = db.query(
                TABLE2_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        cursor.moveToFirst();
        contractNumber = cursor.getString(1);
        danos= cursor.getString(4);
        coments= cursor.getString(5);
        km = cursor.getString(6);
        cursor.close();          // Dont forget to close your cursor
        db.close();

    }

    public String getTransferData(String matricula, String numVh, String contract, String day, String hour, String km, String combustible, String comentarios){

        return matricula + "," +
                numVh + "," +
                contract + "," +
                day + " , " +
                hour + "," +
                km + "km " + "," +
                destinacion + "," +
                combustible  + "," +
                danos + "," +
                comentarios + "," + "\n";
    }

    private void backBtnListener(){
        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  getEntries.this.finish();
            }
        });
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
                        if(destino.length()>=2){
                           destinacion=input.getText().toString().trim();
                            getCarInfo(matriculaItem.trim(),fecha.trim(), hora.trim());
                            generateTransfer(getEntries.this, "transfers " + getDay() + ".csv",getTransferData(matriculaItem, numero,contractNumber,fecha, hora, km, combustible,coments));
                            dialog.dismiss();
                        }else{}//
                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public String getHour(){
        return new SimpleDateFormat("kk:mm").format(new Date(System.currentTimeMillis()));
    }
}
