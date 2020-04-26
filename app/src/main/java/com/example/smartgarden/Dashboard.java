package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;
import java.util.HashMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    // init the empty Arraylist names containing Strings
    private ArrayList<String> names ;
    private ArrayAdapter<String> arrayAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // all the code below is executed when the page is created
        super.onCreate(savedInstanceState);
        // java file corresponding to following xml file :
        setContentView(R.layout.activity_dashboard);

        // code for bottom tab navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    // change the tab in function of each possible click case
                    // when dashboard tab is clicked....
                    case R.id.dashboard :
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                , Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
                }
            });


        
        
        // define the button "add a plant" which is in the corresponding xml file
        Button button = (Button) findViewById(R.id.button);
        // when the button is clicked, open call openAdd_A_Plant() function
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd_A_Plant();
            }
        });

        // Get the sended package from AddAPlant.java
        Intent intent = getIntent();
        String plantName = intent.getStringExtra(AddAPlant.EXTRA_TEXT1);
        String plantDescription = intent.getStringExtra(AddAPlant.EXTRA_TEXT2);
        String plantCamera = intent.getStringExtra(AddAPlant.EXTRA_TEXT3);



        // code to open Json file in raw folder :
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray plantArray = obj.getJSONArray("plants");
            // define te arraylist names as empty
            names = new ArrayList<String>();
            // implement the list in the list view with the id myPlantList

            for (int i = 0; i < plantArray.length(); i++) {
                JSONObject jo_inside = plantArray.getJSONObject(i);
                String name = jo_inside.getString("name");
                //Add your values in your `ArrayList` as below:
                names.add(name);
            }
            ListView listView = findViewById(R.id.myPlantList);
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //position = position.toString();
                    //String itemPosition = position;
                    Intent intent = new Intent(view.getContext(), myPlants.class);
                    startActivity(intent);
                }
            });

            String name = intent.getStringExtra(AddAPlant.EXTRA_TEXT1);
            String description = intent.getStringExtra(AddAPlant.EXTRA_TEXT2);
            String url = intent.getStringExtra(AddAPlant.EXTRA_TEXT3);

            JSONObject jsonObj= new JSONObject();

            try {
                jsonObj.put("name", name);
                jsonObj.put("description", description);
                jsonObj.put("cameralink", url);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            plantArray = new JSONArray();
            plantArray.put(jsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // search functionality
        // get query
        // inititate a search view
        SearchView searchPlants = (SearchView) findViewById(R.id.searchPlants);
         // get the query string currently in the text field
        searchPlants.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Dashboard.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Dashboard.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }
    // function to parse json
    public String loadJSONFromAsset() {
        // init object json of type string
        String json = null;
        try {
            // open the json file from asset file
            InputStream is = getAssets().open("plants.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    // function to open add a plant page.
    public void openAdd_A_Plant() {
        Intent intent = new Intent(this, AddAPlant.class);
        startActivity(intent);
    }


}

