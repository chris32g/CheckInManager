package com.example.christian.buttontest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.zip.Inflater;

public class contentEntriesListAdapter extends BaseAdapter{

private static LayoutInflater inflater = null;
Context contexto;
String[] coches;
ArrayList<String>lista;
Cursor cursor;
public contentEntriesListAdapter(Context context, ArrayList<String>lista){
     this.contexto = context;
     inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
     }

     @Override
    public View getView(int i, View convertView, ViewGroup parent){
    final View vista = inflater.inflate(R.layout.adapter_view_layout,null);
         TextView matricula = (TextView)vista.findViewById(R.id.tvMatricula);
         TextView hora = (TextView)vista.findViewById(R.id.tvHoraEntrada);
         TextView modelo = (TextView)vista.findViewById(R.id.tvModelo);
         TextView combustible = (TextView)vista.findViewById(R.id.tvComb);
         TextView kilometros = (TextView)vista.findViewById(R.id.tvKm);
         matricula.setText(cursor.getString(0));
         hora.setText(cursor.getString(4));
         modelo.setText(cursor.getString(1));
         combustible.setText(cursor.getString(6));
         kilometros.setText(cursor.getString(5));
         matricula.setTag(i);
    return vista;
     }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

