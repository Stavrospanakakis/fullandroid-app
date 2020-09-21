package com.example.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import static com.example.mobileui.ShowImage.SERVER_URL;

public class UploadImageToServer extends Activity {

    //Buttons
    Button clearButton, submitButton, homeButton;
    //Canvas
    LinearLayout canvas;
    //Canvas View
    View view;
    //View for drawing
    DrawingView drawView;
    //PHP Script path
    String phpScript = SERVER_URL + "saveImage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        //Canvas
        canvas = (LinearLayout) findViewById(R.id.canvasLayout);

        //Sets the view for drawing
        drawView = new DrawingView(getApplicationContext(), null);
        drawView.setBackgroundColor(Color.WHITE);

        //get screen's width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // Dynamically generating Layout through java code
        canvas.addView(drawView, width, width);
        //Button that clears the canvas
        clearButton = (Button) findViewById(R.id.clear);

        //Button that user submits his drawing
        submitButton = (Button) findViewById(R.id.submit);
        submitButton.setEnabled(false);

        //Button that returns users to home
        homeButton = (Button) findViewById(R.id.home);

        //Canvas view
        view = canvas;

        //Sets click listener to buttons
        submitButton.setOnClickListener(onButtonClick);
        clearButton.setOnClickListener(onButtonClick);
        homeButton.setOnClickListener(onButtonClick);
        submitButton.setEnabled(true);

    }
    //Function for clicking buttons
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // if the user clicks clear button
            if (v == clearButton) {
                //clear the canvas
                drawView.clear();

                //if user clicks submit button
            } else if (v == submitButton) {
                //upload image to server and start the next activity that shows the score
                UploadImage();
                Intent intent = new Intent(UploadImageToServer.this, ShowScore.class);
                startActivity(intent);
                finish();
                //if user clicks the home button
            } else if (v == homeButton) {
                // back to home page
                Intent intent = new Intent(UploadImageToServer.this, ShowImage.class);
                startActivity(intent);
            }
        }
    };

    //convert the image to bytes, creates and returns an encoded String
    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedString;
    }

    //Upload the image on server
    private void UploadImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, phpScript, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s = response.trim();

                if (s.equalsIgnoreCase("Image Upload Failed")) {
                    //error message
                    Toast.makeText(UploadImageToServer.this, "Something Went Wrong. Image Upload Failed", Toast.LENGTH_SHORT).show();
                } else {
                    //success message
                    Toast.makeText(UploadImageToServer.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadImageToServer.this, error + "", Toast.LENGTH_SHORT).show();
            }
        }) {
            //calls all the methods and finally uploads the image
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image_name = String.valueOf(ShowImage.IMAGE_NUMBER);
                drawView.save(view);

                String image = getStringImage(drawView.bitmap);
                Map<String, String> params = new HashMap<String, String>();

                params.put("IMAGE_NAME", image_name);
                params.put("IMAGE_VALUE", image);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
