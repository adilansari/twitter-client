package com.codepath.apps.twitter.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.extensions.DividerItemDecoration;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient mClient;
    private TweetsAdapter tweetsAdapter;
    private List<Tweet> listOfTweets;
    private static final String TAG = TimelineActivity.class.getSimpleName();

    @Bind(R.id.rvTimeline) RecyclerView rvTimeline;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mClient = TwitterApplication.getTwitterClient();
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tweetsAdapter = new TweetsAdapter(new ArrayList<Tweet>());
        rvTimeline.setAdapter(tweetsAdapter);
        rvTimeline.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTimeline.setLayoutManager(layoutManager);
        rvTimeline.setHasFixedSize(false);
        populateTimelineOffline();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void populateTimeline(){
        mClient.getHomeTimeline(0, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void populateTimelineOffline(){
        String url = "https://gist.githubusercontent.com/adilansari/d7b3884559ab93a97074/raw/7a1682ae7da2a4fb5e26499c578d5a5e849492cd/timeline.json";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    listOfTweets = Tweet.fromJson(response);
                    tweetsAdapter.addTweets(listOfTweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(TAG, "Network request failed");
            }
        });
    }
}
