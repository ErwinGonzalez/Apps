package com.galileo.cc6.helpmate.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;

import static android.graphics.Color.WHITE;

/**
 * Created by EEVGG on 15/10/2016.
 */

public class Hojas extends AppCompatActivity {
    private long course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hojas);
        course= getIntent().getExtras().getLong("Curso");
        configureImageButton();
    }

    public void configureImageButton() {
        ImageButton blanco = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton linea = (ImageButton) findViewById(R.id.imageButton7);
        ImageButton cuadro = (ImageButton) findViewById(R.id.imageButton8);

        blanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejecutar("Blanco");
            }
        });

        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejecutar("Linea");
            }
        });

        cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejecutar("Cuadro");
            }
        });
    }

    private void Ejecutar(String fondo) {
        Intent myIntent = new Intent(this, NoteActivity.class);
        myIntent.putExtra("id",fondo);
        myIntent.putExtra("Curso",course);
        startActivity(myIntent);
        finish();
    }
}