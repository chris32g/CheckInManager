package com.example.christian.buttontest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DatosVehiculosHertz.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES2);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
       db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
       db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES2);
       onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
    }

    public ArrayList llenar_lv(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "select Matricula, modelo, numerovehiculo, numerodecontrato, horaentrada, kilometrosentrada, niveldecombustible, nuevosdaños, comentarios\n" +
                "from DatosChekins\n" +
                "where fechaentrada = " + getDay() + ";";
        Cursor cursor = database.rawQuery(q,null);
        if(cursor.moveToFirst()){
            do{
                lista.add(cursor.getString(0));
             
             
             
             
             
             
             
             
            }while(cursor.moveToNext());
        }
        return lista;
    }
}