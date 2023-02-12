package com.example.qr_go;



import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.qr_go.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*
        System.out.println("hello world");

        Player player = new Player("test", "123");

        int j = 10;
        for(int i = 0; i < 10; i++) {
            player.addQR(new QR(j));
            j += 10;

            System.out.println(player.getQRList().toString());
            System.out.println("Highest: " + player.getHighestScore());
            System.out.println("Lowest: " + player.getLowestScore());
            System.out.println("Total: " + player.getTotalScore());
            System.out.println("------------------------------------------------------------");
        }

        System.out.println(player.getQRList().toString());
        System.out.println("Highest: " + player.getHighestScore());
        System.out.println("Lowest: " + player.getLowestScore());
        System.out.println("Total: " + player.getTotalScore());
        System.out.println("------------------------------------------------------------");

        player.deleteQR(9);

        System.out.println(player.getQRList().toString());
        System.out.println("Highest: " + player.getHighestScore());
        System.out.println("Lowest: " + player.getLowestScore());
        System.out.println("Total: " + player.getTotalScore());
        System.out.println("------------------------------------------------------------");

        player.addQR(new QR(10000));

        System.out.println(player.getQRList().toString());
        System.out.println("Highest: " + player.getHighestScore());
        System.out.println("Lowest: " + player.getLowestScore());
        System.out.println("Total: " + player.getTotalScore());
        System.out.println("------------------------------------------------------------");

        player.deleteQR(4);

        System.out.println(player.getQRList().toString());
        System.out.println("Highest: " + player.getHighestScore());
        System.out.println("Lowest: " + player.getLowestScore());
        System.out.println("Total: " + player.getTotalScore());
        System.out.println("------------------------------------------------------------");

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}