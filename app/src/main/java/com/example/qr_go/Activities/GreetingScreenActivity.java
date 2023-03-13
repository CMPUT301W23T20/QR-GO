package com.example.qr_go.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Represents greeting screen if player on device is not already in database
 */
public class GreetingScreenActivity extends FragmentActivity {

    final String TAG = "Sample";
    private Button confirmButton;
    private EditText addUsernameText;
    private EditText addContactText;
    private String android_id;
    private String username;
    private String contact;
    private String message;
    private ArrayList<String> uniqueArrayUsername;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private DataBaseHelper dbHelper = new DataBaseHelper();


    public GreetingScreenActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_greeting_screen);

        confirmButton = findViewById(R.id.confirmButton);
        addUsernameText = findViewById(R.id.addUsernameText);
        addContactText = findViewById(R.id.addContactText);

        uniqueArrayUsername = new ArrayList<String>();

        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("Player");

        // add all usernames in db to collection
        addToArrayUsername();

        // get android id from bundle
        try {
            getIDFromBundle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = addUsernameText.getText().toString();
                contact = addContactText.getText().toString();
                if (lengthCheck() && uniqueCheck() && validateEmailAddress()) {
                    // create player and push to db

                    Player player = new Player(username, android_id);
                    player.setContact(contact);

                    dbHelper.updateDB(player);

                    finish();


                }
                else {
                    message();
                }
            }
        });
    }

    protected Boolean lengthCheck() {
        if (username.length()>0) {
            return true;
        }
        else {
            message = "Username cannot be empty.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }
    }

    protected void addToArrayUsername() {

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                uniqueArrayUsername.add(document.get("username").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    protected Boolean uniqueCheck() {
//        System.out.println(ArrayUsername);
        boolean uniqueCheckCaseInsensitive = uniqueArrayUsername.stream().anyMatch(username::equalsIgnoreCase);
        if (uniqueCheckCaseInsensitive) {
            message = "Username already taken.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }
        else {
            return true;
        }

    }

    protected Boolean validateEmailAddress() {

        if(contact.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            return true;
        } else {
            message = "Invalid Email Address.";
            addUsernameText.setText("");
            addContactText.setText("");
            return false;
        }

    }

    protected void message() {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets android ID from bundle
     * @throws Exception
     * Throws exception if no ID was passed in intent
     */
    public void getIDFromBundle() throws Exception{
        if(getIntent() == null || getIntent().getExtras() == null) {
            throw new Exception("Must have intent with \"android_id\"");
        }
        Bundle extras = getIntent().getExtras();
        android_id = extras.getString("android_id");
    }

}