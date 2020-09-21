package com.example.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import java.util.Timer;
import java.util.TimerTask;


public class ShowImage extends Activity {

    public static final String SERVER_URL = "http://192.168.2.9:8000/";
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_TYPE = ".png";
    public static int IMAGE_NUMBER = 0;
    public String imageURL;
    ImageView imageView;
    Timer timer ;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        imageView = (ImageView) findViewById(R.id.imageFromServer);
        IMAGE_NUMBER++;
        imageURL = SERVER_URL + IMAGE_FOLDER + IMAGE_NUMBER + IMAGE_TYPE;

        /* Shows the image*/
        ImageRequest imageRequest = new ImageRequest(imageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowImage.this, "Something Went Wrong. Image Cannot Be Shown", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(ShowImage.this).addToRequestQue(imageRequest);

        /* Canvas is being showed after x seconds | 1000 = 1 second*/
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ShowImage.this , UploadImageToServer.class);
                startActivity(intent);
                finish();
            }
        } , 5000);
    }
}

