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
import android.util.JsonReader;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
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
    public static final String EXTRA_NUMBER = "com.example.smartgarden.EXTRA_NUMBER";

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

        // create a new cache file in the devices Internal Storage
        // the name of the file is plants.json
        // in order to see the devices file explorer : View -> Tool Windows -> Device file explorer
        // the file will be located in the following directory :
        // ./data/data/com.example.smartgarden/files/plants.json
        File cacheFile = new File(this.getFilesDir(), "plants.json");
        try {
            // Get the sended package from AddAPlant.java
            Intent intent = getIntent();
            // init the input values from the user
            String plantName = intent.getStringExtra(AddAPlant.EXTRA_TEXT1);
            String plantDescription = intent.getStringExtra(AddAPlant.EXTRA_TEXT2);
            String plantCamera = intent.getStringExtra(AddAPlant.EXTRA_TEXT3);
             if(plantName==null){
                 plantName = "";
                 plantDescription = "";
                 plantCamera = "";
             }
            // fetch json from asset folder( in order to get the structure)
            // object jsonPlants is the json from the asset folder
            JSONObject jsonPlants = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray =  jsonPlants.getJSONArray("plants");
            // create an empty json object
            JSONObject jsonObj = new JSONObject();

            try {
                // put the input of the user in this new json object
                jsonObj.put("name", plantName);
                jsonObj.put("decription", plantDescription );
                jsonObj.put("CameraLink", plantCamera );

                // put the new json object into the json array
                jsonArray = jsonArray.put(jsonObj);
                jsonPlants = jsonPlants.put("plants", jsonArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // stingify json
            String jsonString = jsonPlants.toString();
            // write in the created cacheFile
            FileWriter fw = new FileWriter(cacheFile);
            BufferedWriter bw = new BufferedWriter(fw);
            // write the stringified json object
            bw.write(jsonString);
            bw.close();
            // new json is now in internal storage

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            // on exception null will be returned
            cacheFile = null;
        }
        String fileContent = "";

        try {
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(cacheFile));
            while ((currentLine = br.readLine()) != null) {
                fileContent += currentLine + '\n';
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();

            // on exception null will be returned
            fileContent = null;
        }


        // code to open Json file in internal storage of the device and display it in the list view
        try {
            BufferedReader br = new BufferedReader(new FileReader(cacheFile));
            JSONObject obj = new JSONObject(loadJSONFromInternal());

            JSONArray plantArray = obj.getJSONArray("plants");
            // define te arraylist names as empty
            names = new ArrayList<String>();
            // implement the list in the list view with the id myPlantList
            // iterate through the objects of the json file
            for (int i = 0; i < plantArray.length(); i++) {
                // for every json object (a plant) we consider its name
                // get the i'th json object
                JSONObject jo_inside = plantArray.getJSONObject(i);
                // look for the value "name" in each json object
                String name = jo_inside.getString("name");
                // adding the name of each plant to the Array list names
                names.add(name);
            }
            ListView listView = findViewById(R.id.myPlantList);
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int number = position;
                    Intent intent = new Intent(view.getContext(), myPlants.class);
                    intent.putExtra(EXTRA_NUMBER, number);
                    startActivity(intent);
                }
            });
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // inititate a search view
        SearchView searchPlants = (SearchView) findViewById(R.id.searchPlants);
         // call search function
        searchList(searchPlants);

    }

    // function to search through the listview
    public void searchList(SearchView searchView){
        // listen to the input of the user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // adapt the list arrayAdapter in function of the search query
                Dashboard.this.arrayAdapter.getFilter().filter(query);
                return false;
            }
            // when the query changes
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
    public String loadJSONFromInternal() {
        // init object json of type string
        String json = null;
        try {
            // open the json file from asset file
            InputStream is = openFileInput("plants.json");

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
    // read from internal storage the json file
    private String readFromFile() {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = openFileInput("plants.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    // function to open add a plant page.
    public void openAdd_A_Plant() {
        Intent intent = new Intent(this, AddAPlant.class);
        startActivity(intent);
    }
}

