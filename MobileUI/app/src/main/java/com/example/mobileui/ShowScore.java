package com.example.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static com.example.mobileui.ShowImage.SERVER_URL;


public class ShowScore extends Activity  {
    Button next;
    TextView textView;
    String URL = SERVER_URL + "showScore.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);
        textView = (TextView) findViewById(R.id.text);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display score
                        textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ShowImage.IMAGE_NUMBER <= 3){
                    //show the next image
                    Intent intent = new Intent(ShowScore.this , ShowImage.class);
                    startActivity(intent);
                    finish();
                } else{
                    //If images are finished go to last screen
                    Intent intent = new Intent(ShowScore.this , TheEnd.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}