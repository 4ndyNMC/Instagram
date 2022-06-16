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
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private ConstraintLayout clMain;
    private EditText etDescription;
    private ImageView ivPreview;
    private Button btnPicture;
    private Button btnSubmit;
    private Button btnFeed;
    private BottomNavigationView bottomNavigationView;
    BottomNavigationItemView btnHome;
    BottomNavigationItemView btnCompose;
    BottomNavigationItemView btnProfile;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clMain = findViewById(R.id.clMain);
        etDescription = findViewById(R.id.etDescription);
        ivPreview = findViewById(R.id.ivPreview);
        btnPicture = findViewById(R.id.btnPicture);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnFeed = findViewById(R.id.btnFeed);
        btnHome = findViewById(R.id.action_home);
        btnCompose = findViewById(R.id.action_compose);
        btnProfile = findViewById(R.id.action_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        ivPreview.setImageResource(R.drawable.ic_baseline_image_24);
        btnCompose.performClick();
        setColors(getColor(R.color.white),
                getColor(R.color.black),
                getColor(R.color.white));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_logo);

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    etDescription.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    Snackbar.make(clMain, "Please fill out a description", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (photoFile == null || ivPreview.getDrawable() == null) {
                    Snackbar.make(clMain, "There is no image", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(v, description, currentUser, photoFile);
            }
        });

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_compose:
                        setColors(getColor(R.color.white),
                                getColor(R.color.black),
                                getColor(R.color.white));
                        return true;
                    case R.id.action_home:
                        setColors(getColor(R.color.black),
                                getColor(R.color.white),
                                getColor(R.color.white));
                        return true;
                    case R.id.action_profile:
                        setColors(getColor(R.color.white),
                                getColor(R.color.white),
                                getColor(R.color.black));
                        return true;
                    default: return true;
                }
            }
        });

    }

    private void setColors(int home, int compose, int profile) {
        btnHome.setBackgroundColor(home);
        btnCompose.setBackgroundColor(compose);
        btnProfile.setBackgroundColor(profile);
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(takenImage);
            } else {
                Snackbar.make(clMain, "Picture wasn't taken", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    private void savePost(View v, String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Snackbar.make(v, "Sorry, there was an error saving your post", Snackbar.LENGTH_LONG);
                    Log.e(TAG, "save post error", e);
                }
                etDescription.setText("");
                ivPreview.setImageResource(0);
                Log.i(TAG, "post saved");
                Snackbar.make(clMain, "Post saved!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "queryPosts error", e);
                    return;
                }
                for (Post post : objects) {
                    Log.i(TAG, "Post: " + post.getDescription() + " " + post.getUser().getUsername());
                }
            }
        });
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
