package com.example.christian.buttontest;
//Esta clase contiene la estructura de la BBDD

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry1 implements BaseColumns {
        public static final String TABLE1_NAME = "DatosVehiculos";
        public static final String CAMPO1 = "Matricula";
        public static final String CAMPO2 = "NumeroVehiculo";
        public static final String CAMPO3 = "Modelo";
        public static final String CAMPO4 = "Clase";
    }
    public static class FeedEntry2 implements BaseColumns {
        public static final String TABLE2_NAME = "DatosChekins";
        public static final String CAMPO1 = "Matricula";
        public static final String CAMPO2 = "Modelo";
        public static final String CAMPO3 = "Clase";
        public static final String CAMPO4 = "NumeroVehiculo";
        public static final String CAMPO5 = "NumeroDeContrato";
        public static final String CAMPO6 = "FechaEntrada";
        public static final String CAMPO7 = "HoraEntrada";
        public static final String CAMPO8= "KilometrosEntrada";
        public static final String CAMPO9 = "NivelDeCombustible";
        public static final String CAMPO10 = "NuevosDaños";
        public static final String CAMPO11 = "Sillita";
        public static final String CAMPO12 = "GPS";
        public static final String CAMPO13 = "Transfer";
        public static final String CAMPO14 = "Destino";
        public static final String CAMPO15 = "Comentarios";
        }
        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE if not exists " + FeedEntry1.TABLE1_NAME + "(" +
                    FeedEntry1.CAMPO1 + " TEXT PRIMARY KEY," +
                    FeedEntry1.CAMPO2 + " INTEGER," +
                    FeedEntry1.CAMPO3 + " TEXT," +
                    FeedEntry1.CAMPO4 + " TEXT);";

    public static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + FeedEntry2.TABLE2_NAME + " (" +
                    FeedEntry2.CAMPO1 + " TEXT," +
                    FeedEntry2.CAMPO2 + " TEXT," +
                    FeedEntry2.CAMPO3 + " TEXT," +
                    FeedEntry2.CAMPO4 + " INTEGER, " +
                    FeedEntry2.CAMPO5 + " INTEGER," +
                    FeedEntry2.CAMPO6 + " TEXT," +
                    FeedEntry2.CAMPO7 + " INTEGER," +
                    FeedEntry2.CAMPO8 + " TEXT," +
                    FeedEntry2.CAMPO9 + " INTEGER," +
                    FeedEntry2.CAMPO10 + " INTEGER," +
                    FeedEntry2.CAMPO11 + " TEXT," +
                    FeedEntry2.CAMPO12 + " TEXT," +
                    FeedEntry2.CAMPO13 + " TEXT," +
                    FeedEntry2.CAMPO14 + " TEXT," +
                    FeedEntry2.CAMPO15 + " TEXT, " +
                    "PRIMARY KEY(Matricula,KilometrosEntrada,FechaEntrada)"+");";



    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry1.TABLE1_NAME;
            //"DROP TABLE IF EXISTS " + FeedEntry2.TABLE2_NAME;


}
