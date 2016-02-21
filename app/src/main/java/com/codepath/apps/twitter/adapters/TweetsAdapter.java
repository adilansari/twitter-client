package com.codepath.apps.twitter.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Tweet> tweetsList;
    private Tweet tweet;

    public TweetsAdapter(List<Tweet> tweets){
        this.tweetsList = tweets;
    }

    public void addTweets(List<Tweet> tweets){
        if (tweets.size() > 0) {
            int curSize = this.getItemCount();
            tweetsList.addAll(tweets);
            notifyItemRangeInserted(curSize, this.getItemCount() - 1);
        }
    }

    public void addTweetToTop(Tweet t){
        this.tweetsList.add(0, t);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        tweet = tweetsList.get(position);
        SimpleTweetViewHolder simpleVH = (SimpleTweetViewHolder) holder;
        if (tweet.inReplyToScreenName != null) {
            simpleVH.tvRetweetedByUser.setText(tweet.inReplyToScreenName);
        } else {
            simpleVH.tvRetweetedByUser.setVisibility(View.GONE);
        }
        Glide.with(simpleVH.ivTweeter.getContext()).load(tweet.user.profileImgUrl).into(simpleVH.ivTweeter);
        simpleVH.tvTweeterName.setText(tweet.user.name);
        simpleVH.tvScreenName.setText("@"+tweet.user.screenName);
        simpleVH.tvTweetTimeStamp.setText(tweet.getRelativeTimestamp());
        simpleVH.tvTweetText.setText(linkify(tweet.text));
        simpleVH.tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        simpleVH.tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
    }

    private SpannableString linkify(String text){
        SpannableString hashtagString = new SpannableString(text);
        Matcher matcher = Pattern.compile("[#@]([A-Za-z0-9_-]+)").matcher(hashtagString);
        while (matcher.find())
            hashtagString.setSpan(new ForegroundColorSpan(Color.parseColor("teal")), matcher.start(), matcher.end(), 0);

        return hashtagString;
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    public static abstract class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TweetViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public static class SimpleTweetViewHolder extends TweetViewHolder {
        @Bind(R.id.tvRetweetedByUser) TextView tvRetweetedByUser;
        @Bind(R.id.ivTweeter) ImageView ivTweeter;
        @Bind(R.id.tvTweeterName) TextView tvTweeterName;
        @Bind(R.id.tvScreenName) TextView tvScreenName;
        @Bind(R.id.tvTweetTimeStamp) TextView tvTweetTimeStamp;
        @Bind(R.id.tvTweetText) TextView tvTweetText;
        @Bind(R.id.tvRetweetCount) TextView tvRetweetCount;
        @Bind(R.id.tvFavoriteCount) TextView tvFavoriteCount;

        public SimpleTweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
