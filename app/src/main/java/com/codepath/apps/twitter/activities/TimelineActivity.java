package com.codepath.apps.twitter.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.extensions.DividerItemDecoration;
import com.codepath.apps.twitter.extensions.EndlessRecyclerViewScrollListener;
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
    public static TweetsAdapter tweetsAdapter;
    private List<Tweet> listOfTweets;
    private Tweet lastTweet;
    private static final String TAG = TimelineActivity.class.getSimpleName();

    @Bind(R.id.rvTimeline) RecyclerView rvTimeline;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mClient = TwitterApplication.getTwitterClient();
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tweetsAdapter = new TweetsAdapter(new ArrayList<Tweet>());
        rvTimeline.setAdapter(tweetsAdapter);
        rvTimeline.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTimeline.setLayoutManager(layoutManager);
        rvTimeline.setHasFixedSize(false);
        rvTimeline.setOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                lastTweet = tweetsAdapter.getLastItem();

                listOfTweets = Tweet.olderItems(lastTweet);
                Log.d(TAG, "items fetched: " + listOfTweets.size());
                if (listOfTweets.size() < 25) {
                    populateTimelineOffline(false);
                }
                tweetsAdapter.addTweets(listOfTweets);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweetsAdapter.clearData();
                populateTimelineOffline(true);
            }
        });

//        initial load
        populateTimelineOffline(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void populateTimeline(){
        mClient.getHomeTimeline(0, new JsonHttpResponseHandler() {
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

    private void populateTimelineOffline(final boolean recent){
        String url = "https://gist.githubusercontent.com/adilansari/d7b3884559ab93a97074/raw/6d712863c017b21528bbe7c2b72150e14f7b2c78/timeline.json";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.d("network", "Sending nw request");
                    Tweet.insertFromJson(response);
                    if (recent) tweetsAdapter.addTweets(Tweet.recentItems());
                    if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "Network request failed");
                Toast.makeText(TimelineActivity.this, "No network detected", Toast.LENGTH_SHORT).show();
                if (recent) tweetsAdapter.addTweets(Tweet.recentItems());
                if (swipeContainer.isRefreshing()) swipeContainer.setRefreshing(false);
            }
        });
    }



    public void showComposeDialog(View view){
        FragmentManager fm = this.getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fm, "tag");
    }
}
