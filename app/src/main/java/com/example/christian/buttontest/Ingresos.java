package com.example.christian.buttontest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


import org.w3c.dom.Text;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ingresos extends AppCompatActivity {

    private Spinner spinnerCombustible;
    public Button btnSubmit;// = findViewById(R.id.btnSubmit);
    public Dialog dialog;
    public static String textoMatricula, modeloVh, claseVh, numVh="", destinacion, hora;
    public String emails = "chris32p@gmail.com, gescofet@hertz.com, spbcn61@hertz.com, juan.cano@grupounoctc.com, Checkinhertz.sans@grupounoctc.com";
    public String[] emailsList = emails.split(",");
    public static Boolean returnedDialog;
//    public TextView nVehiculo = findViewById(R.id.nVehiculo);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.ingresos);
        getMatricula();
        addListenerOnButton();
        listenerOnSecondButton();
        checkTransferListener();
        listenerOnBackButton();
        listenerOnClearButton();
        onUnfocusedTextMatricula();
        ListenerEditTextkm();
        blanqueator();
        hora=getHour();
        setTVHour();
        ListenerEditTextMatricula();
        addListenerOnSpinnerItemSelection();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        }

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(Ingresos.this);

    public void addListenerOnSpinnerItemSelection(){

        spinnerCombustible = findViewById(R.id.spinnerCombustible);
        spinnerCombustible.setOnItemSelectedListener(new com.mkyong.android.CustomOnItemSelectedListener());

    }

    public void addListenerOnButton(){

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkpermission();
                TextView nVehiculo = findViewById(R.id.nVehiculo);
                final String fileNom = "checkin" + getDay() + ".csv";
                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path  = baseDir + "/IngresosHertz)/" + fileNom;
                if (getMatricula().isEmpty()) {
                    getToast("Ingrese una matricula para empezar",0);
                }else if (getKm()< 10 || getKm()>200000) {
                    getToast("Ingrese kilometros del vehiculo", 0);
                }else if (!carExist()){
                    openDialog();
                }else if(carExist()) {
                    getCarInfo();
                    //writeNewArrive();
                    AlertDialog alertDialog = new AlertDialog.Builder(Ingresos.this)
                            //set icon
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            //set title
                            .setTitle("Desea Continuar?")
                            //set message
                            .setMessage("Esta por introducir el coche con Matrícula: " + "\n" + getMatricula())
                            //set positive button
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        generateNoteOnSD(Ingresos.this, fileNom, getActivity2Sdata());
                                        writeNewArrive();
                                        getToast("Añadido al archivo", 0);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                      if (getTransfer().equals("si")) {
                                        generateTransfer(Ingresos.this, "transfers " + getDay() +".csv", getTransferData());
                                    }
                                    if (getDanos().equals("si")) {
                                        picsErraser();
                                        String subject = "parte " + getMatricula();
                                        String bodyText = getDay() + " " + hora + "\n"
                                                + getKm() + " km " + getFuel() + "/8" + "\n"
                                                + getComents() + "\n" + "firmado";
                                        Intent intnt = new Intent(Ingresos.this, DamagesActivity.class);
                                        intnt.putExtra("subject",subject);
                                        intnt.putExtra("bodyText",bodyText );
                                        handlTimerBlankr();
                                        startActivity(intnt);
                                    } else { blanqueator();}
                                }
                            })
                            //set negative button
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what should happen when negative button is clicked
                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();


                }else{
                    Toast.makeText(Ingresos.this, "no se que hacer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void listenerOnBackButton(){
        ImageButton backBtn = findViewById(R.id.volverBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingresos.this.finish();
            }
        });
    }

    public void openDialog(){

            Bundle bundle1 = new Bundle();
            bundle1.putString("textoMatricula",getMatricula());
            bundle1.putString("from", "Ingresos");
            DialogAddVh dialogoDatos = new DialogAddVh();
            dialogoDatos.setArguments(bundle1);
            dialogoDatos.show(getSupportFragmentManager(), "Introduzca Nuevo Vehiculo");

        }

    public void writeNewArrive(){
        getCarInfo();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry2.CAMPO1, textoMatricula);     //matricula
        values.put(FeedReaderContract.FeedEntry2.CAMPO2, modeloVh);          //modelo
        values.put(FeedReaderContract.FeedEntry2.CAMPO3, claseVh);         //clase
        values.put(FeedReaderContract.FeedEntry2.CAMPO4, numVh);         //numVh
        values.put(FeedReaderContract.FeedEntry2.CAMPO5, getContractNumber());         //num contrato
        values.put(FeedReaderContract.FeedEntry2.CAMPO6, getDay());        //hora entrada
        values.put(FeedReaderContract.FeedEntry2.CAMPO7, hora);        //fecha de entrada
        values.put(FeedReaderContract.FeedEntry2.CAMPO8, getKm());         //km entrada
        values.put(FeedReaderContract.FeedEntry2.CAMPO9, getFuel());         //combustible
        values.put(FeedReaderContract.FeedEntry2.CAMPO10, getDanos());        //nuevos daños
        values.put(FeedReaderContract.FeedEntry2.CAMPO11, "");
        values.put(FeedReaderContract.FeedEntry2.CAMPO12, "");
        values.put(FeedReaderContract.FeedEntry2.CAMPO13, getTransfer());
        values.put(FeedReaderContract.FeedEntry2.CAMPO14, destinacion);
        values.put(FeedReaderContract.FeedEntry2.CAMPO15, getComents());        //comentarios


        // Insert the new row, returning the primary key value of the new row
        db.insert(FeedReaderContract.FeedEntry2.TABLE2_NAME, null, values);
        //long newRowId = db.insert(TABLE2_NAME, null, values);
        //Toast.makeText(Activity2.this, "el coche " + textoMatricula + " chequeado correctamente.", Toast.LENGTH_LONG).show();
    }

    public boolean carExist() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + FeedReaderContract.FeedEntry1.TABLE1_NAME + " WHERE " + FeedReaderContract.FeedEntry1.CAMPO1 + " =?";

        // Add the String you are searching by here.
        String[] selectionArgs = {getMatricula()};
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, selectionArgs);

        boolean carExist = false;
        if(cursor.moveToFirst()) {
            carExist = true;
        }

        cursor.close();
        db.close();
        return carExist;
    }

    public void checkTransferListener(){
        Switch checkTransfer = findViewById(R.id.checkTransfer);
        checkTransfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    getDestinyAlert();
                }
            }
        }
        );
    }

    public void getDestinyAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Ingresos.this);
        alertDialog.setTitle("Destino del Transfer");
        alertDialog.setMessage("Ingrese Destino del Transfer");
        final EditText input = new EditText(Ingresos.this);
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
                        }else{getToast("Destino Invalido",0);}
                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Switch checkTransfer = findViewById(R.id.checkTransfer);
                        checkTransfer.setChecked(false);
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    //area de getters

    public String getActivity2Sdata(){
        return getMatricula() + "," +
                numVh + "," +
                modeloVh + "," +
                getContractNumber() + "," +
                getDay() + "," +
                hora + "," +
                getKm() + "," +
                getFuel() + "," +
                getTransfer() + "," +
                getComents() + "," + "\n";
            }

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
    }

    public void getCarInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry1.CAMPO2,
                FeedReaderContract.FeedEntry1.CAMPO3,
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry1.CAMPO1 + " = ?";
        String[] selectionArgs = {getMatricula()};
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry1.TABLE1_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        cursor.moveToFirst();
        numVh = cursor.getString(0);
        modeloVh = cursor.getString(1);
        cursor.close();          // Dont forget to close your cursor
        db.close();
    }

    public int getContractNumber(){
        EditText nContrato = findViewById(R.id.nContrato);
        try {
            int nContratoNumeric = Integer.parseInt(nContrato.getText().toString());
            String ncontratoText = nContrato.getText().toString();
            if (ncontratoText.length() !=9 ){
                return 0;
            }else{
            return nContratoNumeric;}
        }catch (Exception e){return 0;}
    }

    public String getHour(){
        return new SimpleDateFormat("kk:mm").format(new Date(System.currentTimeMillis()));
    }

    public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }

    public int getKm(){
        EditText nkm = findViewById(R.id.nKilometros);
        try {
            return Integer.parseInt(nkm.getText().toString());
        }catch (Exception e){return 0;}
    }

    public void ListenerEditTextMatricula(){
        final EditText matricula =findViewById(R.id.nMatricula);
        matricula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

            @Override
            public void afterTextChanged(Editable editable) {

              hora=getHour();
              setTVHour();
            }
        });
    }

    public void ListenerEditTextkm(){
        final EditText km =findViewById(R.id.nKilometros);
        final TextView numvh = findViewById(R.id.nVehiculo);
        km.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(km.length()>2) {
                    getCarInfo();
                    numvh.setText(numVh);
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onUnfocusedTextMatricula(){
        EditText textMatricula = findViewById(R.id.nMatricula);
        final TextView nVehiculo = findViewById(R.id.nVehiculo);

        textMatricula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!getMatricula().equals("")) {
                    if (!b && carExist()) {
                        TextView nVehiculo = findViewById(R.id.nVehiculo);
                        getCarInfo();
                        nVehiculo.setText(numVh);
                    } else if (!carExist()) {
                        getMatricula();
                        openDialog();
                    }
                }
                }


        });
    }

    public void setTVHour(){
        final TextView horaTV =  findViewById(R.id.fechahora);
        String textHora = hora + " " + getDay();
        horaTV.setText(textHora);
        horaTV.postInvalidate();
    }

    public String getFuel(){
        String comustible = spinnerCombustible.getSelectedItem().toString();
        switch (comustible){
            case "0/8" : return "0";
            case "1/8" : return "1";
            case "2/8" : return "2";
            case "3/8" : return "3";
            case "4/8" : return "4";
            case "5/8" : return "5";
            case "6/8" : return "6";
            case "7/8" : return "7";
            case "8/8" : return "8";
            default: return "no disponible";
        }
    }

    public String getDanos(){
        boolean dañosChecked = ((Switch) findViewById(R.id.checkNuevoDaño)).isChecked();
        if (dañosChecked){
             return "si";
        }else{
            return "no";
        }
    }

    public String getTransfer(){
        boolean dañosChecked = ((Switch) findViewById(R.id.checkTransfer)).isChecked();
        if (dañosChecked){
            return "si";
        }else{
            return "no";
        }
    }

    public String getComents(){
        EditText comentariosEt =  findViewById(R.id.etComentarios);
        return comentariosEt.getText().toString();
    }

    public String getMatricula(){
        EditText nMatricula =  findViewById(R.id.nMatricula);
        textoMatricula = nMatricula.getText().toString().toUpperCase().trim();
        if (textoMatricula.equals("")) {
            return "";
        }else{
        return  textoMatricula;}
    }

    public void blanqueator(){
        EditText nMatriculaET = findViewById(R.id.nMatricula),
                nKilometrosET = findViewById(R.id.nKilometros),
                nContratoET = findViewById(R.id.nContrato),
                comentariosAdicionalesEt = findViewById(R.id.etComentarios);
        TextView nVehiculo= findViewById(R.id.nVehiculo);
        Switch transfer = findViewById(R.id.checkTransfer),
               checkDaños = findViewById(R.id.checkNuevoDaño);
        Spinner spinnerCombustible = findViewById(R.id.spinnerCombustible);
        spinnerCombustible.setSelection(0,true);
        nMatriculaET.setText("");
        nKilometrosET.setText("");
        nContratoET.setText("");
        comentariosAdicionalesEt.setText("");
        checkDaños.setChecked(false);
        transfer.setChecked(false);
        nVehiculo.setText("");
        destinacion = "";
    }  //vuelve los editText del activity 2 a blanco

    public void listenerOnSecondButton() {

        Button buttonExport = findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] emails = {"chris32p@gmail.com","spbcn61@hertz.com, Checkinhertz.sans@grupounoctc.com"};
                String subject = "Entradas " + getDay();
                String bodyText = "Entradas ocurridas el dia " + getDay() +" hasta las: " + getHour();
                Boolean adjunto = true;                                                                             //cambiar por verificacion real
                sendEmail(emails, subject,bodyText,adjunto);
            }
        });
    }

    public void listenerOnClearButton() {

        ImageButton clearBtn = findViewById(R.id.clearButton);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               blanqueator();
            }
        });
    }

    public void sendEmail (String[] emails, String subject, String bodyText, Boolean adjunto){
        String fileNom = "checkin" + getDay() + ".csv";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
        File root = new File(Environment.getExternalStorageDirectory(), "IngresosHertz");
        File file = new File(root, fileNom);
        if(adjunto) {
           if (file.exists()){
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "gmail :"));
           } else if (!adjunto) {
               startActivity(Intent.createChooser(emailIntent, "gmail :"));
           } else if (!file.exists()){
               getToast("no se encuentra el archivo", 1);
           }else{getToast("error desconocido", 1);}
        }
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "IngresosHertz");
            String basicEntry =
                    "SEP=," + "\n" +
                    "Matricula" + "," +
                    "Numero" + "," +
                    "Modelo" + "," +
                    "Contrato" + "," +
                    "Fecha" + "," +
                    "Hora" + "," +
                    "Kilometros" + "," +
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

    //Area de permisos de escritura, lectura y camara

    private Context mContext=Ingresos.this;

    private static final int REQUEST = 112;

    private void checkpermission(){        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void handlTimerBlankr(){
        int TIME = 5000; //5000 ms (5 Seconds)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blanqueator();
            }
        }, TIME);
    }

    public void picsErraser(){
        for(int j=0; j<6;j++){
            final String[] nombres = new String[]{"0.jpg", "1.jpg","2.jpg","3.jpg","4.jpg","5.jpg","6.jpg","7.jpg","8.jpg","9.jpg","10.jpg","11.jpg"};
            File root = new File(Environment.getExternalStorageDirectory(), "FotosDanos");
            File fileDelete = new File(root,nombres[j]);
            if (fileDelete.exists()){
                fileDelete.delete();
            }}
    }

    //generador de toast

    public void getToast(String texto, int num){
        if(num == 0) {
            Toast.makeText(Ingresos.this, texto, Toast.LENGTH_SHORT).show();
        }
        else if (num == 1){
            Toast.makeText(Ingresos.this, texto, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Ingresos.this, texto, num).show();
        }
    }

}
