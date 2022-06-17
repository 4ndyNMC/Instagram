package com.example.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.PostsFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private ConstraintLayout clMain;
    private BottomNavigationView bottomNavigationView;
    BottomNavigationItemView btnHome;
    BottomNavigationItemView btnCompose;
    BottomNavigationItemView btnProfile;
    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clMain = findViewById(R.id.clMain);
        btnHome = findViewById(R.id.action_home);
        btnCompose = findViewById(R.id.action_compose);
        btnProfile = findViewById(R.id.action_profile);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragment = new ComposeFragment();

        btnCompose.performClick();
        setColors(getColor(R.color.white),
                getColor(R.color.black),
                getColor(R.color.white));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_logo);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_compose:
                        setColors(getColor(R.color.white),
                                getColor(R.color.black),
                                getColor(R.color.white));
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_home:
                        // TODO: update fragment
                        setColors(getColor(R.color.black),
                                getColor(R.color.white),
                                getColor(R.color.white));
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_profile:
                        // TODO: update fragment
                        setColors(getColor(R.color.white),
                                getColor(R.color.white),
                                getColor(R.color.black));
                        fragment = new ProfileFragment();
                        break;
                    default: break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_compose);
    }

    private void setColors(int home, int compose, int profile) {
        btnHome.setBackgroundColor(home);
        btnCompose.setBackgroundColor(compose);
        btnProfile.setBackgroundColor(profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout:
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
