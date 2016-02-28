package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class UserTimelineFragment extends HomeFragment{

    public static final String TAG = UserTimelineFragment.class.getSimpleName();
    private User user;

    public static UserTimelineFragment newInstance(long userId){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void initialize(){
        Bundle args = getArguments();
        long userId = args.getLong("userId");
        user = User.byId(userId);
    }

    @Override
    public void fetchRecent() {
        RequestParams params = getParams(25);
        params.put("user_id", String.valueOf(user.userId));
        fetchTweets(params, true);
    }

    public void fetchTweets(RequestParams params, final boolean updateView){
        mClient.getUserTimeline(params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "Sending nw request");
                try {
                    listOfTweets = Tweet.fromJson(response);
                    if (updateView) {
                        tweetsAdapter.clearData();
                        tweetsAdapter.addTweets(listOfTweets);
                    }
                    if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(TAG, "Network request failed");
                Toast.makeText(getContext(), "No network detected", Toast.LENGTH_SHORT).show();
                if (updateView) {
                    tweetsAdapter.clearData();
                    tweetsAdapter.addTweets(user.recentTweetsForUser());
                }
                if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void fetchOlder(Tweet lastTweet) {
        listOfTweets = user.olderTweetsForUser(lastTweet);
        tweetsAdapter.addTweets((listOfTweets));
        if (listOfTweets.size() < 25){
            RequestParams params = getParams(50);
            params.put("max_id", String.valueOf(lastTweet.tweetId));
            params.put("user_id", String.valueOf(user.userId));
            fetchTweets(params, false);
        }
    }

    @Override
    public String getOfflineUrl() {
        return null;
    }
}
