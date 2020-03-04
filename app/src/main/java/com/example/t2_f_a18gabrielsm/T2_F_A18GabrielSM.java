package com.example.t2_f_a18gabrielsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class T2_F_A18GabrielSM extends AppCompatActivity {

    SharedPreferences prefs;
    private int PREFERENCES_REQUEST = 0;
    private int CALL_PHONE_PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_f_a18gabrielsm);

        loadPreferences();
        loadClicks();

    }

    private void loadClicks() {
        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(T2_F_A18GabrielSM.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST);
                    } else {
                        makeCall();
                    }
                } else {
                    makeCall();
                }
            }
        });

        Button btnSalary = findViewById(R.id.btnSalary);
        btnSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtName = findViewById(R.id.edtName);
                if (!edtName.getText().toString().equals("")){
                    Intent salary = new Intent(getApplicationContext(), ActivitySalary.class);
                    startActivity(salary);
                } else {
                    Toast.makeText(T2_F_A18GabrielSM.this, R.string.emptyNameToast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        EditText edtPhone = findViewById(R.id.edtPhone);
        String phone = String.valueOf(edtPhone.getText());
        if (!phone.equals("")) {
            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel: " + phone));
            startActivity(call);
        } else {
            Toast.makeText(this, R.string.emptyPhoneToast, Toast.LENGTH_LONG).show();
        }
    }

    private void loadPreferences() {
        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        EditText edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setTextColor(getResources().getColor(prefs.getInt("COR", R.color.red)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprefs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemCor){
            Intent preferences = new Intent(getApplicationContext(), ActivityPreferences.class);
            startActivityForResult(preferences, PREFERENCES_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PREFERENCES_REQUEST) {
            loadPreferences();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeCall();
            } else {
                Toast.makeText(this, R.string.permissionDeniedToast, Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
