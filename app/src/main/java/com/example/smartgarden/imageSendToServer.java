package com.example.smartgarden;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import static com.android.volley.Request.Method.POST;

public class imageSendToServer extends AppCompatActivity {
    String upLoadServerUri;
    ProgressDialog dialog = null ;
    String path = "data/data/com.example.smartgarden/app_imageDir";
    int serverResponseCode;

    public imageSendToServer() throws MalformedURLException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send_to_server);
        ImageView imageSentToServer = (ImageView) findViewById(R.id.TakenImage);
        loadImageFromStorage(path);
        Button backHome = (Button) findViewById(R.id.backhome);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        // send image to server

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

                try {
                    uploadFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    private void loadImageFromStorage(String path)
    {

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

    public void uploadFile() throws IOException {


        String fileName = path + "profile.jpg" ;
        upLoadServerUri = "localhost:3000/image";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile =new File(fileName);
        // open a URL connection to the Servlet
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        URL url = new URL(upLoadServerUri);



        try {

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                   dos.writeBytes(lineEnd);

                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available();

                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];

                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                   while (bytesRead > 0) {

                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   Toast.makeText(imageSendToServer.this, serverResponseCode, Toast.LENGTH_LONG).show();
                   String serverResponseMessage = conn.getResponseMessage();



                   if(serverResponseCode == 200){

                       runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(imageSendToServer.this, "File Upload Complete.",
                                             Toast.LENGTH_SHORT).show();
                            }
                        });
                   } else {
                       runOnUiThread(new Runnable() {
                           public void run() {

                               Toast.makeText(imageSendToServer.this, "File Upload Failed, server not responding.",
                                       Toast.LENGTH_SHORT).show();
                           }
                       });
                   }

                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();

              } catch (MalformedURLException ex) {


                  ex.printStackTrace();

                  runOnUiThread(new Runnable() {
                      public void run() {

                          Toast.makeText(imageSendToServer.this, "MalformedURLException",
                                                              Toast.LENGTH_SHORT).show();
                      }
                  });

              } catch (Exception e) {


                  e.printStackTrace();

                  runOnUiThread(new Runnable() {
                      public void run() {
                          Toast.makeText(imageSendToServer.this, "Got Exception",
                                  Toast.LENGTH_SHORT).show();
                      }
                  });

              }

              //return serverResponseCode;


         }
}
