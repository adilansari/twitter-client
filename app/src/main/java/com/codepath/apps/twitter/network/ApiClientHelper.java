package com.codepath.apps.twitter.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiClientHelper {

    private static final String TAG = ApiClientHelper.class.getSimpleName();
    private static TwitterClient mClient = TwitterApplication.getTwitterClient();
    private static Context mContext = TwitterApplication.getContext();
    private static JsonHttpResponseHandler jsonHttpResponseHandler  = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            try {
                Log.d(TAG, "Sending nw request");
                Tweet.insertFromJson(response);
//                    if (recent) tweetsAdapter.addTweets(Tweet.recentTweets());
//                    if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG, "Network request failed");
            Toast.makeText(mContext, "No network detected", Toast.LENGTH_SHORT).show();
//                if (recent) tweetsAdapter.addTweets(Tweet.recentTweets());
//                if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
        }
    };

    private static void populateTimelineOffline(final boolean recent) {
        String url = "https://gist.githubusercontent.com/adilansari/d7b3884559ab93a97074/raw/6d712863c017b21528bbe7c2b72150e14f7b2c78/timeline.json";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, jsonHttpResponseHandler);
    }

    private void fetchTimeline(final long max_id) {
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(25));
        if (max_id > 0)
            params.put("max_id", String.valueOf(max_id));
        mClient.getHomeTimeline(params, jsonHttpResponseHandler);
    }
}
