package com.example.christian.buttontest;

        import android.Manifest;
        import android.content.Intent;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button ButonInsertNewArrive;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onclickSearchEntry();
        onclickTransfer();
        ButonInsertNewArrive = findViewById(R.id.ButonInsertNewArrive);
        ButonInsertNewArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
    }

    public void openActivity2() {
        Intent i = new Intent(MainActivity.this, Ingresos.class);
        startActivity(i);
    }
    public void runToast(){

        Toast.makeText(MainActivity.this, "prueba de minutero test test test ", Toast.LENGTH_SHORT).show();
    }

    public void onclickSearchEntry() {
        Button ButtonSearchEntry = findViewById(R.id.ButtonSearchEntry);
        ButtonSearchEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, getEntries.class);
                startActivity(i);
            }
        });
    }
    public void onclickTransfer() {
        Button ButtonSetTransfer = findViewById(R.id.ButtonSetTransfer);
        ButtonSetTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DamagesActivity.class);
                startActivity(i);
            }
        });
    }

}
