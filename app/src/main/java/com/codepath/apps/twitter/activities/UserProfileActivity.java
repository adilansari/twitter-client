package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.User;
import com.codepath.apps.twitter.utils.TextConversionUtils;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    @Bind(R.id.profile_ivUserImage) ImageView ivUserImage;
    @Bind(R.id.profile_tvUserName) TextView tvUserName;
    @Bind(R.id.profile_tvScreenName) TextView tvScreenName;
    @Bind(R.id.profile_tvUserDescription) TextView tvUserDescription;
    @Bind(R.id.profile_tvLocation) TextView tvLocation;
    @Bind(R.id.profile_tvFollowingCount) TextView tvFollowingCount;
    @Bind(R.id.profile_tvFollowersCount) TextView tvFollowersCount;
    @Bind(R.id.profile_tvTweetsCount) TextView tvTweetsCount;

    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        populateView(user);
    }

    private void populateView(User user) {
        Glide.with(ivUserImage.getContext()).load(user.profileImgUrl).into(ivUserImage);
        tvUserName.setText(user.name);
        tvScreenName.setText(TextConversionUtils.screenName(user.screenName));
        tvUserDescription.setText(user.description);
        tvLocation.setText(user.location);
        tvFollowingCount.setText(Integer.toString(user.friendsCount));
        tvFollowersCount.setText(Integer.toString(user.followersCount));
        tvTweetsCount.setText(Integer.toString(user.statusesCount));
    }
}
