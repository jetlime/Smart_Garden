package com.example.smartgarden;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import static com.android.volley.Request.Method.POST;

public class imageSendToServer extends AppCompatActivity {
    String upLoadServerUri;
    ProgressDialog dialog = null ;
    String path = "data/data/com.example.smartgarden/app_imageDir";
    int serverResponseCode;
    ImageView imageSentToServer;
    String base64string;

    public imageSendToServer() throws MalformedURLException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send_to_server);
        imageSentToServer = (ImageView) findViewById(R.id.TakenImage);
        loadImageFromStorage(path);
        Button backHome = (Button) findViewById(R.id.backhome);
        final TextView status = (TextView) findViewById(R.id.plantStatus);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
        File f=new File(path, "profile.jpg");

        URL url = null;
        try {
            url = new URL("http://messir.uni.lu/bicslab");
            HttpURLConnection httpCon = null;
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = null;
            out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(String.valueOf(f));
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
