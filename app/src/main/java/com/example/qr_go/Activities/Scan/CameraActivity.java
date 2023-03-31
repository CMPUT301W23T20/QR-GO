package com.example.qr_go.Activities.Scan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//From Youtube.com
// URL: https://www.youtube.com/watch?v=s1aOlr3vbbk&list=RDCMUCR1t5eSmLxLUdBnK2XwZOuw&index=3
//      https://www.youtube.com/watch?v=KaDwSvOpU5E&list=RDCMUCR1t5eSmLxLUdBnK2XwZOuw&index=4
//      https://www.youtube.com/watch?v=q5pqnT1n-4s&list=RDCMUCR1t5eSmLxLUdBnK2XwZOuw&index=5
//      https://www.youtube.com/watch?v=dKX2V992pWI&list=RDCMUCR1t5eSmLxLUdBnK2XwZOuw&index=1
// Author: https://www.youtube.com/@SmallAcademy

//From stackoverflow.com
// URL: https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
// Author: https://stackoverflow.com/users/5246885/alex-mamo

//From geeksforgeeks.org
// URL: https://www.geeksforgeeks.org/how-to-compress-image-in-android-before-uploading-it-to-firebase-storage/
// Author: https://auth.geeksforgeeks.org/user/prajithshetty7/articles

/**
 * This class displays the activity which gives the player the option to record a photo
 *
 */
public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 22;
    ImageView selectedImage;
    Button yesBtn;
    Button noBtn;
    String currentPhotoPath;
    Uri PhotoUri;
    StorageReference storageReference;
    Uri contentUri;
    Bitmap bmp;
    ByteArrayOutputStream baos;

    QR qr;

    /**
     * This function is called right when the activity starts
     * It ensures that when the button for the camera is clicked, it will show a camera
     *
     * @param savedInstanceState state If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        qr = getIntent().getParcelableExtra("QR");

        selectedImage = findViewById(R.id.displayImageView);
        yesBtn = findViewById(R.id.yesBtn);
        noBtn = findViewById(R.id.noBtn);

        storageReference = FirebaseStorage.getInstance().getReference();

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGetLocationActivity();
            }
        });
    }


    /**
     * This function is called after a picture is taken
     * It creates the URI for the photo which will be stored in firebase storage
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
        if(requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
//                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                // Adds photo into gallery

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                // Adds photo to firebase storage
                uploadCompressedImageToFirebase(f.getName(), contentUri, new MyCallback() {
                    @Override
                    public void onCallback(Uri uri) {
                        PhotoUri = uri;
                        System.out.println("PhotoUri is " + PhotoUri);
                        Picasso.get().load(PhotoUri).into(selectedImage);
                    }
                });


                // Go to GetLocationActivity
                goToGetLocationActivity();

            }
        }
    }

    /**
     * This function compresses the image and adds it to firebase
     *
     * @param name The name of the photo
     * @param contentUri The path of the photo
     * @param myCallback Callback interface
     */

    private void uploadCompressedImageToFirebase(String name, Uri contentUri, MyCallback myCallback) {

        // create a bitmap from the Uri
        // this compresses the high resolution from the Uri to a lower resolution bitmap
        bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = new ByteArrayOutputStream();

        // here we can choose quality factor
        // in third parameter(ex. here it is 25)
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);

        byte[] fileInBytes = baos.toByteArray();


        StorageReference image = storageReference.child("images/" + name);
        image.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into(selectedImage);
                        Log.d("tag", "onSuccess: Uploaded Image URL is " + uri.toString());
                        myCallback.onCallback(uri);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * This interface creates a callback which allows us to wait for Firebase to return the uri
     *
     */

    public interface MyCallback{
        void onCallback(Uri uri);
    }

    /**
     *  Creates an image file
     *
     * @return A file where the image should be stored
     * @throws IOException
     */

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Opens the camera and calls createImageFile to create a file where the photo is stored
     *
     */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CODE);
        }

    }

    /**
     * Starts a new activity GetLocationActivity
     *
     */

    public void goToGetLocationActivity() {
        Intent locationIntent = new Intent(CameraActivity.this, GetLocationActivity.class);
        locationIntent.putExtra("QR",qr);
        startActivity(locationIntent);
    }


}