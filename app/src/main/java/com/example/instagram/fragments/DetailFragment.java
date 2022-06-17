package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;

public class DetailFragment extends Fragment {

    TextView tvUsername;
    TextView tvTimestamp;
    TextView tvDescription;
    ImageView ivImage;

    public DetailFragment() { }

    public static DetailFragment newInstance(String username, String timestamp,
                                             String description, String imageUrl) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("timestamp", timestamp);
        args.putString("description", description);
        args.putString("imageUrl", imageUrl);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvDetailUsername);
        tvTimestamp = view.findViewById(R.id.tvDetailTimestamp);
        tvDescription = view.findViewById(R.id.tvDetailDescription);
        ivImage = view.findViewById(R.id.ivDetailImage);

        tvUsername.setText(getArguments().getString("username"));
        tvTimestamp.setText(getArguments().getString("timestamp"));
        tvDescription.setText(getArguments().getString("description"));
        Glide.with(getContext()).load(getArguments().getString("imageUrl")).into(ivImage);
    }
}