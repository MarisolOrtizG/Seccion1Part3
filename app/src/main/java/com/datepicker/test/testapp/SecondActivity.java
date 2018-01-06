package com.datepicker.test.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);       //Activar flecha ir atrás.
        getSupportActionBar().setDisplayShowHomeEnabled(true);       //Vamos a forzar y cambiar icono en el Action Bar
        getSupportActionBar().setIcon(R.mipmap.ic_mariposa);         //Para que funcione, debemos decirle cuál es el icono


        textView = findViewById(R.id.textViewMain);
        btnNext =  findViewById(R.id.buttonGoSharing);

        /*Tomamos los datos del intent con Bundle.
        Imaginemos Bundle como una caja donde están todos esos datos e información que se han
        enviado del activity anterior*/

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getString("greeter") != null){
            String greeter = bundle.getString("greeter");
            Toast.makeText(SecondActivity.this, greeter, Toast.LENGTH_SHORT).show();
            textView.setText(greeter);
        }else {
            Toast.makeText(SecondActivity.this, "It is empty", Toast.LENGTH_SHORT).show();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intenSecond = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intenSecond);
            }
        });

    }
}
