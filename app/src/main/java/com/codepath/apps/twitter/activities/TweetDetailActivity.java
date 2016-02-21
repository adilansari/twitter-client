package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.utils.TextConversionUtils;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetDetailActivity  extends AppCompatActivity{

    @Bind(R.id.detail_ivUserImage) ImageView ivUserImage;
    @Bind(R.id.detail_tvUserName) TextView tvUserName;
    @Bind(R.id.detail_tvScreenName) TextView tvScrenName;
    @Bind(R.id.detail_tvTweetText) TextView tvTweetText;
    @Bind(R.id.detail_stubMedia) ViewStub mediaStub;
    @Bind(R.id.detail_tvTimestamp) TextView tvTimestamp;
    @Bind(R.id.detail_btnReply) Button btnReply;

    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        populateView(tweet);
    }

    private void populateView(Tweet tweet){
        Glide.with(ivUserImage.getContext()).load(tweet.user.profileImgUrl).into(ivUserImage);
        tvUserName.setText(tweet.user.name);
        tvScrenName.setText(TextConversionUtils.screenName(tweet.user.screenName));
        tvTweetText.setText(TextConversionUtils.linkify(tweet.text));
        tvTimestamp.setText(tweet.getReadableDate());
    }
}
