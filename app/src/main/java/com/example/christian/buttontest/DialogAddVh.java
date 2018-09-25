package com.example.christian.buttontest;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class DialogAddVh extends AppCompatDialogFragment {
    public Button boton1;
    public String textoMatricula;
    public String from;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.addvhtodb, null);
        from = this.getArguments().getString("from");
        final EditText edtNumVh = view.findViewById(R.id.vhNumFsDialog);
        final EditText edtModeloVh = view.findViewById(R.id.modelVhFsDialog);
        final EditText matriculaVhDiag = view.findViewById(R.id.matriculaVhFsDialog);
        builder.setView(view).setTitle("Registro de Vehiculo")
                .setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.GuardarBt, new DialogInterface.OnClickListener() {

                    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getContext());

            public void onClick(DialogInterface dialog, int id) {

                int carNum = 0;
                String carModelVh="";
                try {
                    String carNumLength = edtNumVh.getText().toString().trim();
                    if (carNumLength.length() !=7){
                        Toast.makeText(getActivity(), "Numero de Vehiculo Incorrecto", Toast.LENGTH_SHORT).show();
                    }else{
                        carNum = Integer.parseInt(edtNumVh.getText().toString().trim());
                        try {
                            carModelVh = edtModeloVh.getText().toString().trim();
                        }catch (Exception e){carModelVh= "Sin Modelo";}
                    }
                }catch (Exception e){carNum= 0000000;}
if (from.equals("Ingresos")){
                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry1.CAMPO1, textoMatricula);
                values.put(FeedReaderContract.FeedEntry1.CAMPO2, carNum);
                values.put(FeedReaderContract.FeedEntry1.CAMPO3, carModelVh);

                // Insert the new row, returning the primary key value of the new row
                db.insert(FeedReaderContract.FeedEntry1.TABLE1_NAME, null, values);
                Toast.makeText(getContext(), "el coche " + textoMatricula + " se guardo correctamente.", Toast.LENGTH_LONG).show();
                Ingresos.returnedDialog=true;
            }
            if (from.equals("editVh")){
                    updateVh(edtNumVh.getText().toString().trim(),edtModeloVh.getText().toString().trim(),matriculaVhDiag.getText().toString().trim());
                    Toast.makeText(getContext(), "Actualizado con exito", Toast.LENGTH_LONG).show();
            }
            //Ingresos.numvhET=(edtNumVh.getText().toString().trim());
            }
        });


        textoMatricula = this.getArguments().getString("textoMatricula");
        if(from.equals("editVh")){
            edtNumVh.setText(this.getArguments().getString("textonumero"));
            edtModeloVh.setText(this.getArguments().getString("textoModelo"));
        }
        matriculaVhDiag.setText(textoMatricula);

        return builder.create();
    }

    public void updateVh(String newVhNumber, String newModel, String matricula){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + "DatosVehiculos" + " SET " + "NumeroVehiculo" + " = '" + newVhNumber.trim() +"', "
                + "MODELO" + " ='" + newModel.trim() +"'" +
                " WHERE " + "Matricula" + " ='" + matricula.trim() +"';" );
    }

}




