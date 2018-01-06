package com.datepicker.test.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private final String GREETER = "Hello from the other side!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);       //Vamos a forzar y cambiar icono en el Action Bar
        getSupportActionBar().setIcon(R.mipmap.ic_mariposa);         //Para que funcione, debemos decirle cuál es el icono


        btn = (Button) findViewById(R.id.buttonMain);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accdemos a la segunda actividad, con un intent explícito, y se manda un string
                Intent intentMain = new Intent(MainActivity.this, SecondActivity.class);
                intentMain.putExtra("greeter", GREETER);
                startActivity(intentMain);
            }
        });
    }
}
