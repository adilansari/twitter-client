package com.codepath.apps.twitter.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.activities.TweetDetailActivity;
import com.codepath.apps.twitter.activities.UserProfileActivity;
import com.codepath.apps.twitter.extensions.LinkifiedTextView;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.utils.TextConversionUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private  List<Tweet> tweetsList;
    private Tweet tweet;
    private TwitterClient mClient;

    public TweetsAdapter(List<Tweet> tweets){
        tweetsList = tweets;
    }

    public void addTweets(List<Tweet> tweets){
        if (tweets.size() > 0) {
            int curSize = this.getItemCount();
            tweetsList.addAll(tweets);
            notifyItemRangeInserted(curSize, this.getItemCount() - 1);
        }
    }

    public void addTweetToTop(Tweet t){
        tweetsList.add(0, t);
        notifyItemInserted(0);
    }

    public Tweet getLastItem(){
        Tweet lastTweet = null;
        if (tweetsList.size() > 0) lastTweet = tweetsList.get(tweetsList.size() - 1);
        return lastTweet;
    }

    public void clearData(){
        tweetsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View tweetView;

        tweetView = li.inflate(R.layout.activity_tweet_timeline, parent, false);
        viewHolder = new SimpleTweetViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        tweet = tweetsList.get(position);
        SimpleTweetViewHolder simpleVH = (SimpleTweetViewHolder) holder;
        Glide.with(simpleVH.ivTweeter.getContext()).load(tweet.user.profileImgUrl).into(simpleVH.ivTweeter);
        simpleVH.tvTweeterName.setText(tweet.user.name);
        simpleVH.tvScreenName.setText(TextConversionUtils.screenName(tweet.user.screenName));
        simpleVH.tvTweetTimeStamp.setText(tweet.getRelativeTimestamp());
        simpleVH.tvTweetText.setText(TextConversionUtils.linkify(tweet.text));
        simpleVH.tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        simpleVH.tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));

        if (tweet.retweeted) simpleVH.ivRetweetIcon.setImageResource(R.drawable.ic_retweet_action_active);
        if (tweet.favorited) simpleVH.ivFavoriteIcon.setImageResource(R.drawable.ic_like_action_active);
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    public abstract class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TweetViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            tweet = tweetsList.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), TweetDetailActivity.class);
            intent.putExtra("tweet", Parcels.wrap(tweet));
            v.getContext().startActivity(intent);
        }
    }

    public class SimpleTweetViewHolder extends TweetViewHolder {
        @Bind(R.id.ivTweeter) RoundedImageView ivTweeter;
        @Bind(R.id.tvTweeterName) TextView tvTweeterName;
        @Bind(R.id.tvScreenName) TextView tvScreenName;
        @Bind(R.id.tvTweetTimeStamp) TextView tvTweetTimeStamp;
        @Bind(R.id.tvTweetText) LinkifiedTextView tvTweetText;
        @Bind(R.id.tvRetweetCount) TextView tvRetweetCount;
        @Bind(R.id.tvFavoriteCount) TextView tvFavoriteCount;
        @Bind(R.id.ivRetweetIcon) ImageView ivRetweetIcon;
        @Bind(R.id.ivFavoriteIcon) ImageView ivFavoriteIcon;

        public SimpleTweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivTweeter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweet = tweetsList.get(getLayoutPosition());
                    Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                    intent.putExtra("user", Parcels.wrap(tweet.user));
                    v.getContext().startActivity(intent);
                }
            });

            ivRetweetIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweet = tweetsList.get(getLayoutPosition());
                    mClient = TwitterApplication.getTwitterClient();
                    final String tweetId = String.valueOf(tweet.tweetId);
                    Log.d("action", tweetId);
                    mClient.postRetweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivRetweetIcon.setImageResource(R.drawable.ic_retweet_action_active);
                            tweet.retweeted = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("action", errorResponse.toString());
                        }
                    });
                }
            });

            ivFavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweet = tweetsList.get(getLayoutPosition());
                    mClient = TwitterApplication.getTwitterClient();
                    String tweetId = String.valueOf(tweet.tweetId);
                    mClient.postFavorite(tweetId, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivFavoriteIcon.setImageResource(R.drawable.ic_like_action_active);
                            tweet.favorited = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("action", errorResponse.toString());
                        }
                    });
                }
            });
        }
    }
}
