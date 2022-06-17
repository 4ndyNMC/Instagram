package com.example.instagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.instagram.FeedActivity;
import com.example.instagram.Post;
import com.example.instagram.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private ConstraintLayout clCompose;
    private EditText etDescription;
    private ImageView ivPreview;
    private Button btnPicture;
    private Button btnSubmit;
    private Button btnFeed;
    private File photoFile;
    public String photoFileName = "photo.jpg";


    public ComposeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clCompose = view.findViewById(R.id.clCompose);
        etDescription = view.findViewById(R.id.etDescription);
        ivPreview = view.findViewById(R.id.ivPreview);
        btnPicture = view.findViewById(R.id.btnPicture);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnFeed = view.findViewById(R.id.btnFeed);

        ivPreview.setImageResource(R.drawable.ic_baseline_image_24);

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
                    Snackbar.make(v, "Please fill out a description", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (photoFile == null || ivPreview.getDrawable() == null) {
                    Snackbar.make(v, "There is no image", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(v, description, currentUser, photoFile);
            }
        });

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeedActivity.class);
                startActivity(intent);
            }
        });

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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
                Snackbar.make(clCompose, "Post saved!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPreview = (ImageView) clCompose.findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(takenImage);
            } else {
                Snackbar.make(clCompose, "Picture wasn't taken", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

}