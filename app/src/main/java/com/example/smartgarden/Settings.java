package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                , Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        final Switch cameraPermission = (Switch) findViewById(R.id.cameraPermission);
        final Switch locationPermission = (Switch) findViewById(R.id.locationPermission);
        final Button sendMailButton = (Button) findViewById(R.id.SendMail);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            cameraPermission.setChecked(false);
        } else {
            cameraPermission.setChecked(true);
        }

        cameraPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraPermission.isChecked()){
                    ActivityCompat.requestPermissions(Settings.this,
                            new String[] { Manifest.permission.CAMERA },
                            100);
                    cameraPermission.setText("Camera permission is on");
                } else {
                    cameraPermission.setText("Camera permission is off");
                }
            }
        });

        locationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationPermission.isChecked()){
                    locationPermission.setText("Location manager is on");
                } else {
                    locationPermission.setChecked(false);
                    locationPermission.setText("Location manager is off");
                }
            }
        });

        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "paul.houssel.001@student.uni.lu" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Smart Garden Application");
                intent.putExtra(Intent.EXTRA_TEXT, "Explain your problem");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}
