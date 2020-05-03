package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
    private ArrayList<String> alerts ;
    private ArrayAdapter<String> arrayAdapter;
    private TextView lamp1;
    private TextView lamp2;
    private TextView lamp3;
    private TextView lamp4;
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
        alerts = new ArrayList<String>();
        alerts.add("This is an alert");
        final ListView listView = findViewById(R.id.alerts);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, alerts);
        listView.setAdapter(arrayAdapter);
        lamp1 = (TextView) findViewById(R.id.lamp1);
        lamp2 = (TextView) findViewById(R.id.lamp2);
        lamp3 = (TextView) findViewById(R.id.lamp3);
        lamp4 = (TextView) findViewById(R.id.lamp4);


        // call function to read txt file, the parameter is the url.



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        // Create a URL for the desired page
                        URL url = new URL("https://messir.uni.lu/bicslab/cnnResult.txt");

                        // Read all the text returned by the server
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str;


                        for(int i=1; i<5;i++){
                            str = in.readLine();
                            if(str == "ok"){
                                str = "Lamp is working";
                                lamp1.setTextColor(Color.GREEN);
                                lamp2.setTextColor(Color.GREEN);
                                lamp3.setTextColor(Color.GREEN);
                                lamp4.setTextColor(Color.GREEN);
                            }else {
                                str = "Lamp " + i +" is not working";
                                lamp1.setTextColor(Color.RED);
                                lamp2.setTextColor(Color.RED);
                                lamp3.setTextColor(Color.RED);
                                lamp4.setTextColor(Color.RED);

                            }
                            if(i==1){
                                lamp1.setText(str);
                            }else if (i==2){
                                lamp2.setText(str);
                            }else if(i==3){
                                lamp3.setText(str);
                            }else if(i==4){
                                lamp4.setText(str);
                            }
                        }
                        /*while ((str = in.readLine()) != null) {
                            i += 1;
                            if(i==1){
                                lamp1.setText(str);
                            }else if (i==2){
                                lamp2.setText(str);
                            }else if(i==3){
                                lamp3.setText(str);
                            }else if(i==4){
                                lamp4.setText(str);
                            }
                        }*/

                        in.close();
                    } catch (MalformedURLException e) {

                    } catch (IOException e) {


                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();




    }}
