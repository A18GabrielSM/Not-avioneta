package com.example.t2_f_a18gabrielsm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPreferences extends AppCompatActivity {

    SharedPreferences prefs;
    int id;

    RadioButton rbRed;
    RadioButton rbGreen;
    RadioButton rbBlue;
    Button btnSetPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);

        id = prefs.getInt("COR", R.color.red);

        switch (id) {
            case R.color.red:
                rbRed = findViewById(R.id.rbRed);
                rbRed.setChecked(true);
                break;
            case R.color.green:
                rbGreen = findViewById(R.id.rbGreen);
                rbGreen.setChecked(true);
                break;
            case R.color.blue:
                rbBlue = findViewById(R.id.rbBlue);
                rbBlue.setChecked(true);
                break;
        }

        btnSetPreferences = findViewById(R.id.btnSetPreferences);
        btnSetPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();

                rbRed = findViewById(R.id.rbRed);
                if (rbRed.isChecked()) {
                    id = R.color.red;
                }
                rbGreen = findViewById(R.id.rbGreen);
                if (rbGreen.isChecked()) {
                    id = R.color.green;
                }
                rbBlue = findViewById(R.id.rbBlue);
                if (rbBlue.isChecked()) {
                    id = R.color.blue;
                }

                editor.putInt("COR", id);
                editor.commit();
                finish();

            }
        });

    }
}
