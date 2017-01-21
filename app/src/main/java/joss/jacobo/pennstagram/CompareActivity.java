package joss.jacobo.pennstagram;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by simon.guo on 2017-01-21.
 */

public class CompareActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String STATE_PICTURE_TAKEN_BITMAP = "state_picture_taken_bitmap";
    private static final String STATE_PICTURE_URI = "state_picture_uri";
    private static final String IMAGE_NAME = "captured_image";

    private Uri pictureFileUri;
    private Bitmap pictureTakenBitmap;

    private TextView pictureText;
    private ImageView platyImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        pictureText = (TextView) findViewById(R.id.text);
        platyImageView = (ImageView) findViewById(R.id.platy);

    }
}
