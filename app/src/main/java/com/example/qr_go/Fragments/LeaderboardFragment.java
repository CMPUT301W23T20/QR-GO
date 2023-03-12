package com.example.qr_go.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.qr_go.Adapters.LeaderboardAdapter;
import com.example.qr_go.Content.LeaderboardContent;
import com.example.qr_go.R;

import java.util.ArrayList;

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
    private ArrayList<LeaderboardContent> dataList;
    // will contain just the usernames
    private ArrayList<String> userList;
    private LeaderboardAdapter leaderboardAdapter;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        dataList = new ArrayList<LeaderboardContent>();
        userList = new ArrayList<String>();

        String[] users = {"Edm", "Vancouver", "To"};
        int[] scores = {124, 13, 1};

        for (int i = 0; i < users.length; i++){
            dataList.add(new LeaderboardContent(users[i], scores[i]));
            userList.add(users[i]);
        }

        leaderboardList = view.findViewById(R.id.leaderboard_list);
        leaderboardAdapter = new LeaderboardAdapter(getActivity(),android.R.layout.simple_list_item_1, dataList);
        leaderboardList.setAdapter(leaderboardAdapter);
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
                    //Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
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
