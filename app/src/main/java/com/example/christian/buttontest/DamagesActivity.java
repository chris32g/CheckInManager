package com.example.christian.buttontest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DamagesActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imagenCamara;
    int numFoto;
    Bitmap fotoTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_damages);
        Button tomarFotos=findViewById(R.id.btnTomarFotos);
        tomarFotosListener();
        ArrayList<ImageView>imVw=null;
        enviarInformeListener();
        numFoto=0;
    }

    private void tomarFotosListener(){
        Button tomarFotos=findViewById(R.id.btnTomarFotos);
        tomarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
    }

    private void enviarInformeListener(){
        Button enviarInforme = findViewById(R.id.btnEnviarInforme);
        enviarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });
    }

    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    private void pickPhoto(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imagenCamara0 = findViewById(R.id.imageView1);
        ImageView imagenCamara1 = findViewById(R.id.imageView2);
        ImageView imagenCamara2= findViewById(R.id.imageView3);
        ImageView imagenCamara3 = findViewById(R.id.imageView4);
        ImageView imagenCamara4 = findViewById(R.id.imageView5);
        ImageView imagenCamara5 = findViewById(R.id.imageView6);
        ImageView[] imagesViews = new ImageView[]{
        imagenCamara0,
        imagenCamara1,
        imagenCamara2,
        imagenCamara3,
        imagenCamara4,
        imagenCamara5};

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
           Bundle extras = data.getExtras();
            String path = Environment.getExternalStorageDirectory().toString();
           String filename = "fotoDeTest";
            File file = new File(path,"fotoDeTest.PNG");
           Bitmap photo = (Bitmap) extras.get("data");
           imagesViews[numFoto].setImageBitmap(photo);
           fotoTest=photo;

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                fotoTest.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap    instance
// PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "fallo aqui", Toast.LENGTH_LONG).show();

            }
           //Drawable i0= imagesViews[0].getBackground();
           //BitmapDrawable bitDw0 = ((BitmapDrawable) i0);
           //Bitmap bitmap0 = bitDw0.getBitmap();

        }
        if (numFoto<5) {
            ++numFoto;
        }else{
            numFoto=0;
        }
    }

    public void sendReport(){

    }

    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        File file = new File(extStorageDirectory, "Hertz_temp" + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "Hertz_temp" + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
