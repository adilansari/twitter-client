package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewStub;
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
    @Bind(R.id.detail_ivReplyToTweetIcon) ImageView btnReply;
    @Bind(R.id.detail_tvRetweetCount) TextView tvRetweetCount;
    @Bind(R.id.detail_tvFavoriteCount) TextView tvFavoriteCount;

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
        tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));

        if (tweet.hasMedia()){
            mediaStub.setLayoutResource(R.layout.detail_tweet_image);
            mediaStub.inflate();
            ImageView ivTweetImage = (ImageView) findViewById(R.id.image_ivTweetImage);
            Glide.with(ivTweetImage.getContext()).load(tweet.media.imgUrl).into(ivTweetImage);
        }
    }
}
