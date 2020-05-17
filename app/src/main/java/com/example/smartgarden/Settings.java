package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

        cameraPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraPermission.isChecked()){
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
    }
}
