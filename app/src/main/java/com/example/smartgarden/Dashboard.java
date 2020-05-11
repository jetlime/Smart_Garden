package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.net.URI;
import java.util.HashMap;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Dashboard extends AppCompatActivity implements ExampleDialog.ExampleDialogListerner {
    private static final int IMAGE_CAPTURE_CODE = 1001 ;
    // init the empty Arraylist names containing Strings
    private ArrayList<String> names ;
    private ArrayAdapter<String> arrayAdapter;
    public JSONArray jsonArray;
    public int DeleteCounter ;
    public static final String EXTRA_NUMBER = "com.example.smartgarden.EXTRA_NUMBER";
    JSONObject jsonPlants;
    JSONObject obj;
    private static final int PERMISSION_CODE = 1000;
    Uri image_uri;

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

        // create button that allows to take a picture
        Button takePicture = (Button) findViewById(R.id.TakePicure);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
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
            // if we didnt add a plant then the
            // this will avoid to create a new plant each time we go in the dashboard.
            if(plantName != null) {
                // load json from internal storage
                // fetch json from asset folder( in order to get the structure)
                // object jsonPlants is the json from the asset folder


                try{
                    jsonPlants = new JSONObject(loadJSONFromInternal());
                } catch (JSONException e) {
                    jsonPlants = new JSONObject(loadJSONFromAsset());
                }

                jsonArray = jsonPlants.getJSONArray("plants");

                // create an empty json object
                JSONObject jsonObj = new JSONObject();

                try {
                    // put the input of the user in this new json object
                    jsonObj.put("name", plantName);
                    jsonObj.put("decription", plantDescription);
                    jsonObj.put("CameraLink", plantCamera);

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
            }

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

            try{
                obj = new JSONObject(loadJSONFromInternal());
            } catch (JSONException e) {
                obj = new JSONObject(loadJSONFromAsset());
            }


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
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    DeleteCounter = position;
                    openDialog();
                    return true ;
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

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(CameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                } else {
                    openCamera();
                    Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            saveToInternalStorage(image_uri);
            Toast.makeText(this, "Picture sent to Server, please wait", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, imageSendToServer.class);
            startActivity(intent);

        }
    }

    private String saveToInternalStorage(Uri uriImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    public void openDialog(){
        ExampleDialog dialog = new ExampleDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onYesClicked() throws JSONException, IOException {
        File cacheFile = new File(this.getFilesDir(), "plants.json");

        JSONObject json = new JSONObject(loadJSONFromInternal());
        JSONArray jsonArray = json.getJSONArray("plants");
        //JSONObject plant = jsonArray.getJSONObject(DeleteCounter);
        jsonArray.remove(DeleteCounter);
        names.remove(DeleteCounter);

        // stingify json
        String jsonString = json.toString();
        // write in the created cacheFile
        FileWriter fw = new FileWriter(cacheFile);
        BufferedWriter bw = new BufferedWriter(fw);
        // write the stringified json object
        bw.write(jsonString);
        bw.close();
        // refresh page
        finish();
        startActivity(getIntent());
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
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("plants.json");
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

