package com.example.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class EditaPlant extends AppCompatActivity {
    int positionEdit;
    String plantName;
    String plantDescription;
    String plantCamera;
    String editText1;
    String editText2;
    String editText3;
    JSONObject json;
    JSONArray plantArray;
    JSONObject plant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_plant);
        Intent intent = getIntent();
        positionEdit = intent.getIntExtra(myPlants.EXTRA_NUMBER_EDIT,0);
        Button editButton = (Button) findViewById(R.id.edittheplant);
        TextView nameofplanttoedit = findViewById(R.id.nameplant);
        final EditText editname = findViewById(R.id.plantName);
        final EditText editdescription = findViewById(R.id.plantDescription);
        final EditText editcamera = findViewById(R.id.cameraLink);

        try {
            json = new JSONObject(loadJSONFromInternal());
            plantArray = json.getJSONArray("plants");
            plant = plantArray.getJSONObject(positionEdit);
            editText1 = plant.getString("name");
            editText2 = plant.getString("decription");
            editText3 = plant.getString("CameraLink");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameofplanttoedit.setText("Edit the "  + editText1 + " plant");
        editname.setText(editText1);
        editdescription.setText(editText2);
        editcamera.setText(editText3);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String plantNameFinal = editname.getText().toString();
                String plantDescriptionFinal = editdescription.getText().toString();
                String plantCameraFinal = editcamera.getText().toString();
                try {
                    plant.put("name",plantNameFinal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( !plantNameFinal.isEmpty() && !plantDescriptionFinal.isEmpty() && !plantCameraFinal.isEmpty() ){
                    openMyPlants();
                } else {
                   alertUser();
                }

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
    private void openMyPlants(){
        Intent intent = new Intent(this, myPlants.class);
        startActivity(intent);
    }
    private void alertUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("You did not add all the necessary information");
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Please add the requested information");
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
