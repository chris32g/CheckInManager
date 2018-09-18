package com.example.christian.buttontest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DamagesActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    int numFoto;
    int totalFotos;
    private static final int CAMERA_PHOTO = 111;
    private Uri imageToUploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_damages);
        tomarFotosListener();
        borrarBtnListener();
        enviarInformeListener();
        backBtnListener();
        numFoto=0;
    }

    private void tomarFotosListener(){
        Button tomarFotos=findViewById(R.id.btnTomarFotos);
        tomarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermission();
                captureCameraImage();
            }
        });
    }

    private void enviarInformeListener(){
        Button enviarInforme = findViewById(R.id.btnEnviarInforme);
        final String[] emails = new String[] {"chris32p@gmail.com, gescofet@hertz.com, spbcn61@hertz.com, juan.cano@grupounoctc.com, Checkinhertz.sans@grupounoctc.com"};
        enviarInforme.setOnClickListener(new View.OnClickListener()
                {
            @Override
            public void onClick(View view) {
                String subject = getIntent().getExtras().getString("subject");
                String bodyText = getIntent().getExtras().getString("bodyText");
                sendEmail(emails,subject,bodyText);
            }
        });
    }

    private void borrarBtnListener(){
        ImageButton borrarTodo = findViewById(R.id.btnDelete);
        borrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imagenCamara0 = findViewById(R.id.imageView1);
                ImageView imagenCamara1 = findViewById(R.id.imageView2);
                ImageView imagenCamara2 = findViewById(R.id.imageView3);
                ImageView imagenCamara3 = findViewById(R.id.imageView4);
                ImageView imagenCamara4 = findViewById(R.id.imageView5);
                ImageView imagenCamara5 = findViewById(R.id.imageView6);
                ImageView imagenCamara6 = findViewById(R.id.imageView7);
                ImageView imagenCamara7 = findViewById(R.id.imageView8);
                ImageView imagenCamara8 = findViewById(R.id.imageView9);
                ImageView imagenCamara9 = findViewById(R.id.imageView10);
                ImageView imagenCamara10 = findViewById(R.id.imageView11);
                ImageView imagenCamara11 = findViewById(R.id.imageView12);
                imagenCamara0.setImageResource(0);
                imagenCamara1.setImageResource(0);
                imagenCamara2.setImageResource(0);
                imagenCamara3.setImageResource(0);
                imagenCamara4.setImageResource(0);
                imagenCamara5.setImageResource(0);
                imagenCamara6.setImageResource(0);
                imagenCamara7.setImageResource(0);
                imagenCamara8.setImageResource(0);
                imagenCamara9.setImageResource(0);
                imagenCamara10.setImageResource(0);
                imagenCamara11.setImageResource(0);
                picsErraser();
                numFoto=0;
                totalFotos=0;
                getToast("Fotos Eliminadas",0);
            }
        });
    }

    private void backBtnListener(){
        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picsErraser();
                DamagesActivity.this.finish();
            }
        });
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

    private void captureCameraImage() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File root = new File(Environment.getExternalStorageDirectory(), "FotosDanos");
        if (!root.exists()) {
            root.mkdirs();}
        File f = new File(root, numFoto + ".jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, CAMERA_PHOTO);
    }

    public void sendEmail (String[] emails, String subject, String bodyText){

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
        ArrayList<Uri> uris = new ArrayList<>();
        String[] nombres = new String[]{"0.jpg", "1.jpg","2.jpg","3.jpg","4.jpg","5.jpg","6.jpg","7.jpg","8.jpg","9.jpg","10.jpg","11.jpg"};
        for (int i=0; i<totalFotos+1;i++) {
            File root = new File(Environment.getExternalStorageDirectory(), "FotosDanos");
            File file = new File(root, nombres[i]);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                uris.add(uri);}
                handlTimer();
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(emailIntent, "gmail :"));
        totalFotos=0;

        }
       public void handlTimer(){
           int TIME = 5000; //5000 ms (5 Seconds)

           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   DamagesActivity.this.finish();
               }
           }, TIME);
       }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imagenCamara0 = findViewById(R.id.imageView1);
        ImageView imagenCamara1 = findViewById(R.id.imageView2);
        ImageView imagenCamara2 = findViewById(R.id.imageView3);
        ImageView imagenCamara3 = findViewById(R.id.imageView4);
        ImageView imagenCamara4 = findViewById(R.id.imageView5);
        ImageView imagenCamara5 = findViewById(R.id.imageView6);
        ImageView imagenCamara6 = findViewById(R.id.imageView7);
        ImageView imagenCamara7 = findViewById(R.id.imageView8);
        ImageView imagenCamara8 = findViewById(R.id.imageView9);
        ImageView imagenCamara9 = findViewById(R.id.imageView10);
        ImageView imagenCamara10 = findViewById(R.id.imageView11);
        ImageView imagenCamara11 = findViewById(R.id.imageView12);
        ImageView[] imagesViews = new ImageView[]{
                imagenCamara0,
                imagenCamara1,
                imagenCamara2,
                imagenCamara3,
                imagenCamara4,
                imagenCamara5,
                imagenCamara6,
                imagenCamara7,
                imagenCamara8,
                imagenCamara9,
                imagenCamara10,
                imagenCamara11};
        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
            if(imageToUploadUri != null){
                Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                if(reducedSizeBitmap != null){
                    imagesViews[numFoto].setImageBitmap(reducedSizeBitmap);
                    if (numFoto<12) {
                        totalFotos = numFoto + 1;
                        ++numFoto;
                    }else{
                        numFoto=0;
                    }
                }else{
                    Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    private Context mContext=DamagesActivity.this;

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

    public void getToast(String texto, int num){
        if(num == 0) {
            Toast.makeText(DamagesActivity.this, texto, Toast.LENGTH_SHORT).show();
        }
        else if (num == 1){
            Toast.makeText(DamagesActivity.this, texto, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(DamagesActivity.this, texto, num).show();
        }
    }
}
