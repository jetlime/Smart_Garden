package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;
import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> lamps ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                ,Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                ,Settings.class));
                        overridePendingTransition(0,0);
                        return true;
            }
            return false;
            }
        });
        final String alerts[] = {"This is an alert", "The light has stopped functioning in the lab !"};
        ListView listView = findViewById(R.id.alerts);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, alerts);
        listView.setAdapter(arrayAdapter);

        // call function to read txt file, the parameter is the url.
        // ArrayList<String> lampStatus = getTextFromWeb();
        // define textview in which we will show the txt file content.
        TextView lamps = (TextView) findViewById(R.id.lampstatus);
        ArrayList<String> lampStatus = new ArrayList<>();

        URL url = null;
        try {
            url = new URL("https://messir.uni.lu/bicslab");
            BufferedReader reader = new BufferedReader(new FileReader("/cnnResult.txt"));
            String line = reader.readLine();
            while(line != null){
                lampStatus.add(line);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            lamps.setText("Data not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}