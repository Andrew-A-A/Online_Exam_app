package com.andrew.saba.onlineexamapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.andrew.saba.onlineexamapp.databinding.ActivityMainBinding;
import com.andrew.saba.onlineexamapp.databinding.NavHeaderMainBinding;
import com.andrew.saba.onlineexamapp.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Access the NavigationView variable
        com.andrew.saba.onlineexamapp.databinding.NavHeaderMainBinding nav_binding = NavHeaderMainBinding.inflate(getLayoutInflater());
        com.andrew.saba.onlineexamapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.navView.addHeaderView(nav_binding.getRoot());
       Intent intent= getIntent();
        if (intent != null) {
            String userEmail = intent.getStringExtra("userEmail");
           nav_binding.userMail.setText(userEmail);
        }
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.logout) {
                // Handle the logout action
                Intent i = new Intent(this, LoginActivity.class); // Replace LoginActivity with the name of your LoginActivity class
                startActivity(i);
                finish(); // Finish the current activity
            }
            return true;
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}