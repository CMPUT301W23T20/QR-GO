package com.example.qr_go.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.qr_go.Activities.Profile.GreetingScreenActivity;
import com.example.qr_go.Activities.Scan.CameraActivity;
import com.example.qr_go.Activities.Scan.CaptureAct;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.Coupon;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Source: Youtube.com
//URL: https://www.youtube.com/watch?v=tkf59VvBzhc
//Authour: https://www.youtube.com/@codingbeginnerKH

/**
 * This class is the Scan fragment that displays the QR Scanner
 */
public class ScanFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String android_id;
    private Player player;
    private QR qr;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    //private View view;

    //for passing data
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onScanPressed();

    }

    /**
     * fragment constructor
     */
    public ScanFragment(String android_id) {
        this.android_id = android_id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param android_id Android ID
     * @return
     *      A new instance of fragment FragmentScan.
     */
    public static ScanFragment newInstance(String android_id) {
        ScanFragment fragment = new ScanFragment(android_id);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, android_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This create a view of the scan fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     *      Return the view of the Scan fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "onCreate", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        //((MainActivity)getActivity()).setCustomTheme();
        //((MainActivity)getActivity()).setTheme(R.style.MyAppTheme);

        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button scanButton = view.findViewById(R.id.btn_scan);
        //Button recordButton = view.findViewById(R.id.btn_record);

        scanButton.setOnClickListener(v->
        {
            scanCode();

        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)  //potential probelm
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // pass photo to be stored with QR code here
        }
    }

    /**
     * Starts CaptureActivity
     */
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Place QR code in the center");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);


    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->{

        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //generate coupon
            Coupon coupon = new Coupon();
            String couponString = coupon.lottery();
            // show score of QR code here
            qr = new QR(result.getContents());
            String name = qr.getName();
            String avatar = qr.getAvatar();
            int score = qr.getScore();
            String scorestring = Integer.toString(score);
            // show score of QR code here
            Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
            builder.setTitle("You found a "+name+"!");
            //builder.setMessage("Score: "+scorestring+" points"+"\n"+avatar);

            TextView messageText = new TextView(getActivity());
            messageText.setTypeface(typeface);
            messageText.setText( couponString + "\n" + "Score: " + scorestring + " points" + "\n\n" + avatar);
            messageText.setPadding(20, 20, 20, 20);
            messageText.setTextSize(15);
            builder.setView(messageText);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            // add QR to DB and Player
            // create new QR

            // get player from database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

            // put data into class
            db.collection(Player.class.getSimpleName()).document(android_id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String username = (String)documentSnapshot.get("username");
                            String deviceID = (String)documentSnapshot.get("deviceID");
                            String contact = (String)documentSnapshot.get("contact");
                            ArrayList<QR> qrList = dbHelper.convertQRListFromDB((List<Map<String, Object>>)documentSnapshot.get("qrList"));
                            int rank = ((Long)documentSnapshot.get("rank")).intValue();
                            int highestScore = ((Long)documentSnapshot.get("highestScore")).intValue();
                            int lowestScore = ((Long)documentSnapshot.get("lowestScore")).intValue();
                            int totalScore = ((Long)documentSnapshot.get("totalScore")).intValue();
                            int theme = ((Long)documentSnapshot.get("theme")).intValue();

                            player = new Player(username, android_id, qrList, rank, highestScore, lowestScore, totalScore, theme);
                            player.setContact(contact);

                            qr.addToPlayerList(player.getDeviceID());

                            // add QR to player's list
                            player.addQR(qr);

                            // update DB
                            dbHelper.updateDB(player);

                            db.collection(QR.class.getSimpleName()).document(qr.getQrHash()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(!task.getResult().exists()) {
                                                dbHelper.updateDB(qr);
                                            }
                                            else {
                                                db.collection(QR.class.getSimpleName()).document(qr.getQrHash()).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                List<String> playerList = (List<String>)documentSnapshot.get("playerList");

                                                                playerList.add(player.getDeviceID());

                                                                System.out.println("list after: " + playerList);

                                                                db.collection(QR.class.getSimpleName()).document(qr.getQrHash()).update("playerList", playerList);
                                                            }
                                                        });


                                            }
                                        }
                                    });
                        }
                    });

            builder.setPositiveButton("Yay!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
                    cameraIntent.putExtra("QR",qr);
                    startActivity(cameraIntent);
                }
            }).show();
            // show fragment for camera here to record object
        }
    });
}