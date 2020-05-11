package com.example.smartgarden;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAPlant extends AppCompatActivity {
    // create a string constants
    public static final String EXTRA_TEXT1 = "com.example.smartgarden.EXTRA_TEXT1";
    public static final String EXTRA_TEXT2 = "com.example.smartgarden.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.smartgarden.EXTRA_TEXT3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_plant);
        // Define the add a plant button
        // Once clicked it will call the function addAPlant()
        Button addplantbutton = (Button) findViewById(R.id.edittheplant);

        addplantbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAPlant();
            }
        });
    }
    // this function will add to the list of plants a new list and opening the dashboard tab.
    // the information present in  the text fields will be sent to the dashboard fragment to then be added to the list of plants.
    public void addAPlant() {
        // Put the inputed text in variables :

        EditText editText1 = (EditText) findViewById(R.id.plantName);
        EditText editText2 = (EditText) findViewById(R.id.plantDescription);
        EditText editText3 = (EditText) findViewById(R.id.cameraLink);
        // transform the inputs to strings
        String plantName = editText1.getText().toString();
        String plantDescription = editText2.getText().toString();
        String plantCamera = editText3.getText().toString();
        // check if the user inputed all the necessary information.
        if( !plantName.isEmpty() && !plantDescription.isEmpty() && !plantCamera.isEmpty() ){
            // open the dashboard page
            Intent intent = new Intent(this, Dashboard.class);
            // send the three strings to the dashboard page via the three public variables
            intent.putExtra(EXTRA_TEXT1,plantName);
            intent.putExtra(EXTRA_TEXT2,plantDescription);
            intent.putExtra(EXTRA_TEXT3, plantCamera);
            startActivity(intent);
        } else {
            // call function to alert user
            alertUser();
        }
        // Get the inputed text from the text fields

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


