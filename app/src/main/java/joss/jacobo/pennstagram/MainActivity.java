package joss.jacobo.pennstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String STATE_PICTURE_TAKEN_BITMAP = "state_picture_taken_bitmap";
    private static final String STATE_PICTURE_URI = "state_picture_uri";
    private static final String IMAGE_NAME = "captured_image";

    private Uri pictureFileUri;
    private Bitmap pictureTakenBitmap;

    private ImageView platyImageView;
    private int counter = 0;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        final ImageButton requestButton = (ImageButton) findViewById(R.id.searchButton);


        imageButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                startImageCapture();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                startImageCapture();
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                startImageCapture();
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                startImageCapture();
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                HttpPOSTRequestWithParameters();
            }
        });


        // If the savedInstanceState bundle is not null, it means we saved data in it.
        // Grab the data out and restore the state (i.e. Text, Image, and Button Icon)
        if(savedInstanceState != null)
        {
            pictureTakenBitmap = savedInstanceState.getParcelable(STATE_PICTURE_TAKEN_BITMAP);
            pictureFileUri = savedInstanceState.getParcelable(STATE_PICTURE_URI);
        }

    }

    private void different() throws IOException
    {
        HttpURLConnection httpURLConnection;
        URL url = new URL("http://45.33.95.66:8081/?image=23232.jpg");
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
    }

    /**
     * Set the screen's content depending on whether the user has already taken a picture or not.
     */
    private void setContent(Bitmap b)
    {
        counter++;
        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        if(counter == 1)
        {
            System.out.println("1");
            System.out.println(b);
            imageButton1.setImageBitmap(b);
        }
        else if(counter == 2)
        {
            System.out.println("2");
            imageButton2.setImageBitmap(b);
        }
        else if(counter == 3)
        {
            System.out.println("3");
            imageButton3.setImageBitmap(b);
        }
        else if(counter == 4)
        {
            System.out.println("4");
            imageButton4.setImageBitmap(b);
        }
    }

    private void startImageCapture()
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        pictureFileUri = getOutputMediaFileUri(this, IMAGE_NAME); // create a file to save the image
        if(pictureFileUri != null)
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri); // set the image file name
            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, "There was an error creating the file to save the image to",
                           Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                // Image captured and saved to pictureFileUri specified in the Intent
                File image = new File(pictureFileUri.getPath());
                if(image.exists())
                {
                    pictureTakenBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                    getStringImage(pictureTakenBitmap);
                    setContent(pictureTakenBitmap);
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                // User cancelled the image capture
                // Image captured and saved to pictureFileUri specified in the Intent
            }
            else
            {
                // Image capture failed, advise user
            }
        }
    }


    @Nullable private Uri getOutputMediaFileUri(Context context, String name)
    {
        // Get external storage directory
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Get or Create image folder
        File imagesDir = new File(filesDir, "images");
        if(imagesDir.mkdir() || imagesDir.isDirectory())
        {
            // If images folder exists create an image file to save our picture taken
            // and return it's Uri.
            return Uri.fromFile(new File(imagesDir, name.replace("/", "_") + ".jpeg"));
        }
        return null;
    }

    @Override protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // Save image bitmap and it's uri when activity gets destroyed
        // (i.e. on screen rotation)
        outState.putParcelable(STATE_PICTURE_TAKEN_BITMAP, pictureTakenBitmap);
        outState.putParcelable(STATE_PICTURE_URI, pictureFileUri);
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.i("myTag", encodedImage);
        return encodedImage;
    }


    public void uploadImage(String imageString)
    {
        final TextView mTextView = (TextView) findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://45.33.95.66:8081/?image=" + imageString;

        // Request a string response from the provided URL.
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error)
                    {
                        //mTextView.setText("That didn't work!");
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void HttpPOSTRequestWithParameters()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "45.33.95.66:8081";
        StringRequest postRequest =
                new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error)
                    {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                })
                {
                    // this is the relevant method
                    @Override protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("image", "hey gustav");
                        return params;
                    }
                };
        queue.add(postRequest);
    }

    public void put()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "45.33.95.66:8081";
        StringRequest putRequest =
                new StringRequest(Request.Method.PUT, url, new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error)
                    {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                })
                {
                    @Override protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("image", "Alif");
                        return params;
                    }
                };
        queue.add(putRequest);
    }
}

