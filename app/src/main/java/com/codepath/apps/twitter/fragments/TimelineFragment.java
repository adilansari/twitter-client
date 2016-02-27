package com.codepath.apps.twitter.fragments;

import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimelineFragment extends HomeFragment {

    private static final String TAG = TimelineFragment.class.getSimpleName();

    @Override
    public void fetchRecent() {
        fetchTweets(getParams(25), true);
    }

    public void fetchTweets(RequestParams params, final boolean updateView) {
        mClient.getHomeTimeline(params, new JsonHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "Network request failed");
                Toast.makeText(getContext(), "No network detected", Toast.LENGTH_SHORT).show();
                if (updateView) {
                    tweetsAdapter.clearData();
                    tweetsAdapter.addTweets(Tweet.recentTweets());
                }
                if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void fetchOlder(Tweet lastTweet) {
        listOfTweets = Tweet.olderTweets(lastTweet);
        tweetsAdapter.addTweets(listOfTweets);
        if (listOfTweets.size() < 25) {
            RequestParams params = getParams(50);
            params.put("max_id", String.valueOf(lastTweet.tweetId));
            fetchTweets(params, false);
        }
    }

    @Override
    public String getOfflineUrl() {
        return "https://gist.githubusercontent.com/adilansari/d7b3884559ab93a97074/raw/6d712863c017b21528bbe7c2b72150e14f7b2c78/timeline.json";
    }
}
