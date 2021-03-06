package com.example.smartgarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;

public class myPlants extends AppCompatActivity {
    boolean isImageFitToScreen;
    public static final String EXTRA_NUMBER_EDIT = "com.example.smartgarden.EXTRA_NUMBER_EDIT";
    public static int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_plants);
        Intent intent = getIntent();
        position = intent.getIntExtra(Dashboard.EXTRA_NUMBER,0);

        final TextView itemName = (TextView) findViewById(R.id.itemName);
        final TextView itemDescription = (TextView) findViewById(R.id.itemDescription);
        final ImageView itemPicture = (ImageView) findViewById(R.id.itemPicture);
        // fetch json in order to get information about the clicked plant
        // the position of the clicked plant in the json file is stored in the variable "position"
        try {
            JSONObject json = new JSONObject(loadJSONFromInternal());
            JSONArray plantArray = json.getJSONArray("plants");
            JSONObject plant = plantArray.getJSONObject(position);
            String plantName = plant.getString("name");
            String plantDescription = plant.getString("decription");
            String plantCamera = plant.getString("CameraLink");

            itemName.setText(plantName);
            itemDescription.setText(plantDescription);
            Picasso.with(this).load(plantCamera).into(itemPicture);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add click listener to image
        // if the image is clicked, the image is zoomed in.
        itemPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    itemPicture.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                    itemPicture.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    itemPicture.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                    itemPicture.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

        });
        Button editPlant = (Button) findViewById(R.id.editplant);
        editPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditAPlant();
            }
        });
    }

    private void openEditAPlant() {
        Intent intent = new Intent(this, EditaPlant.class);
        intent.putExtra(EXTRA_NUMBER_EDIT, position);
        startActivity(intent);
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
}
