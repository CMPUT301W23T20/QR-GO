package com.example.qr_go.Activities.Scan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qr_go.R;

//From Youtube.com
// URL: https://www.youtube.com/watch?v=59taMJThsFU
// Author: https://www.youtube.com/@allcodingtutorials1857


/**
 * This class displays the activity which gives the player the option to record a photo
 *
 */
public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 22;
    Button btnpicture;

    /**
     * This function is called right when the activity starts
     * It ensures that when the button for the camera is clicked, it will show a camera
     * @param savedInstanceS state If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btnpicture=findViewById(R.id.btncamera_id);

        btnpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,REQUEST_CODE);
            }
        });
    }

    /**
     * This function is called after a picture is taken
     * It creates a Bitmap object which is the photo, this will be stored with the QR code
     * It then starts the next activity, which is GetLocationActivity
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            //pass photo to be stored here
            Intent locationIntent = new Intent(CameraActivity.this, GetLocationActivity.class);
            startActivity(locationIntent);
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}