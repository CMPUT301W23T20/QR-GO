package com.example.qr_go.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.qr_go.Activities.Scan.CameraActivity;
import com.example.qr_go.Activities.Scan.CaptureAct;
import com.example.qr_go.Actor.Player;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String android_id;
    private Player player;
    private QR qr;
    private DataBaseHelper dbHelper = new DataBaseHelper();

    View view;

    //for passing data
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onScanPressed();

    }

    public ScanFragment(String android_id) {
        this.android_id = android_id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param android_id Android ID
     * @return A new instance of fragment FragmentScan.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button scanButton = view.findViewById(R.id.btn_scan);
        //Button recordButton = view.findViewById(R.id.btn_record);

        scanButton.setOnClickListener(v->
        {
            scanCode();

        });

//        recordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent,REQUEST_CODE);
//
//            }
//        });


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
            // show score of QR code here
            builder.setTitle("Result");
            builder.setMessage(result.getContents());

            // add QR to DB and Player
            // create new QR
            qr = new QR(result.getContents());

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
                            ArrayList<QR> qrList = (ArrayList<QR>) documentSnapshot.get("qrList");
                            int rank = (int) Integer.parseInt((String)documentSnapshot.get("rank"));
                            int highestScore = (int) Integer.parseInt((String)documentSnapshot.get("highestScore"));
                            int lowestScore = (int)Integer.parseInt((String)documentSnapshot.get("lowestScore"));
                            int totalScore = (int)Integer.parseInt((String)documentSnapshot.get("totalScore"));

                            player = new Player(username, android_id, qrList, rank, highestScore, lowestScore, totalScore);

                            // add player to QR's list
                            try {
                                qr.addToPlayerList(player);
                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }

                            // add QR to player's list
                            player.addQR(qr);

                            // update DB
                            dbHelper.updateDB(player);
                            dbHelper.updateDB(qr);
                        }
                    });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
                    startActivity(cameraIntent);
                }
            }).show();
            // show fragment for camera here to record object
        }
    });
}