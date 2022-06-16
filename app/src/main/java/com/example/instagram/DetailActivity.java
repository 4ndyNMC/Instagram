package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvTimestamp;
    TextView tvDescription;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_logo);

        tvUsername = findViewById(R.id.tvDetailUsername);
        tvTimestamp = findViewById(R.id.tvDetailTimestamp);
        tvDescription = findViewById(R.id.tvDetailDescription);
        ivImage = findViewById(R.id.ivDetailImage);

        if (getIntent().getExtras() != null) {
            Post post = (Post) getIntent().getParcelableExtra("post");
            tvUsername.setText(post.getUser().getUsername());
            tvTimestamp.setText(Post.calculateTimeAgo(post.getCreatedAt()));
            tvDescription.setText(post.getDescription());
            Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        }
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