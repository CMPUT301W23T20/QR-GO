package com.example.qr_go;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 22;
    Button btnpicture;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            Intent locationIntent = new Intent(CameraActivity.this, GetLocationActivity.class);
            startActivity(locationIntent);
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}