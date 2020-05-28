package com.example.smartgarden;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import java.net.HttpURLConnection;





public class imageSendToServer extends AppCompatActivity {
    TextView status;
    String path = "data/data/com.example.smartgarden/app_imageDir";
    File f ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send_to_server);
        ImageView imageSentToServer = (ImageView) findViewById(R.id.TakenImage);
        loadImageFromStorage(path);
        Button backHome = (Button) findViewById(R.id.backhome);
        status = (TextView) findViewById(R.id.plantStatus);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
        f=new File(path, "profile.jpg");
        HttpURLConnection conn = null;
        URL url = null;


        // send file to server


        // wait for image to uplaod

        // execute the php script in order to execute the AI programm

        // fetch the output from the txt file.
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    uplaodImage2();
                    URL phpfile = new URL("https://messir.uni.lu/bicslab/cnn-plant-disease.php");
                    URLConnection yc = phpfile.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        System.out.println(inputLine);
                    in.close();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try  {
                    try {
                        // Create a URL for the desired page
                        URL url = new URL("https://messir.uni.lu/bicslab/cnn-plant-disease.txt");
                        Thread.sleep(4000);
                        // Read all the text returned by the server
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str = in.readLine();
                        if("blight".equals(str)){
                            status.setText("The plant is sick");
                            status.setTextColor(Color.RED);
                        } else {
                            status.setText("The plant is healthy");
                            status.setTextColor(Color.GREEN);
                        }

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
    }
    private void uplaodImage2() throws IOException {
        String url = "https://messir.uni.lu/bicslab/tmp/";

    }


    private void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    private void loadImageFromStorage(String path) {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.TakenImage);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
