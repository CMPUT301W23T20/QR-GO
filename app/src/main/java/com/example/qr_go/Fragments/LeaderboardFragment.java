package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qr_go.Actor.Player;
import com.example.qr_go.Adapters.LeaderboardAdapter;
import com.example.qr_go.Content.LeaderboardContent;
import com.example.qr_go.DataBaseHelper;
import com.example.qr_go.MainActivity;
import com.example.qr_go.QR.QR;
import com.example.qr_go.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the fragment that holds the leaderboard
 */
public class LeaderboardFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // instance of the searchview
    SearchView searchView;
    private ListView leaderboardList;
    // will contain usernames and scores
    private ArrayList<Player> dataList;
    // will contain just the usernames
    private ArrayList<String> userList;
    private LeaderboardAdapter leaderboardAdapter;
    private DataBaseHelper dbHelper;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * This create a view of the Leaderboard fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     *      Return the view of the Leaderboard fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        dataList = new ArrayList<Player>();
        userList = new ArrayList<String>();


        // get database information
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection(Player.class.getSimpleName());

        // add db to
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                userList.clear();

                for(QueryDocumentSnapshot doc: value) {
                    // create new player
                    String username = (String)doc.get("username");
                    String deviceID = (String)doc.get("deviceID");

                    int rank = ((Long)doc.get("rank")).intValue();
                    int highestScore = ((Long)doc.get("highestScore")).intValue();
                    int lowestScore = ((Long)doc.get("lowestScore")).intValue();
                    int totalScore = ((Long)doc.get("totalScore")).intValue();

                    Player player = new Player(username, deviceID, new ArrayList<>(), rank, highestScore, lowestScore, totalScore);

                    // add player to data list
                    dataList.add(player);
                    userList.add(username);
                    // sort highest to lowest score
                    Collections.sort(dataList);
                    Collections.reverse(dataList);

                    // set adapters
                    leaderboardList = view.findViewById(R.id.leaderboard_list);
                    leaderboardAdapter = new LeaderboardAdapter(getActivity(),android.R.layout.simple_list_item_1, dataList);
                    leaderboardList.setAdapter(leaderboardAdapter);
                }
            }
        });

        Button button2= view.findViewById(R.id.score_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Collections.sort(dataList);
                Collections.reverse(dataList);

                leaderboardList = view.findViewById(R.id.leaderboard_list);
                leaderboardAdapter = new LeaderboardAdapter(getActivity(),android.R.layout.simple_list_item_1, dataList);
                leaderboardList.setAdapter(leaderboardAdapter);
            }
        });


        Button button = view.findViewById(R.id.filter_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Player>() {
                    @Override
                    public int compare(Player player, Player t1) {
                        return player.getHighestScore() - t1.getHighestScore();
                    }
                });
                Collections.reverse(dataList);
                leaderboardList = view.findViewById(R.id.leaderboard_list);
                leaderboardAdapter = new LeaderboardAdapter(getActivity(),android.R.layout.simple_list_item_1, dataList);
                leaderboardList.setAdapter(leaderboardAdapter);
            }
        });





        // runs the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            // searches the query on the submission of content over SearchView editor
            public boolean onQueryTextSubmit(String query) {
                // if username in userlist
                if(userList.contains(query)){
                    // call filter function
                    leaderboardAdapter.filter(query);
                } else{
                   // Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
           // searches the query at the time of text change over SearchView editor.
            public boolean onQueryTextChange(String newText) {
                // filter
                leaderboardAdapter.filter(newText);
                return false;
            }
        });

        return view;
    }
}
