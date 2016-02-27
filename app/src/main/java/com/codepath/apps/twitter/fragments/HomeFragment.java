package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class HomeFragment extends Fragment {

    public TwitterClient mClient;
    public TweetsAdapter tweetsAdapter;
    List<Tweet> listOfTweets;
    private Tweet lastTweet;
    RequestParams params;
    private static final String TAG = HomeFragment.class.getSimpleName();

    @Bind(R.id.rvTimeline) RecyclerView rvTimeline;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    public static HomeFragment newInstance(int position){
        switch(position){
            case 1:
                return new MentionsFragment();
            default:
                return new TimelineFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);
        ButterKnife.bind(this, view);

        mClient = TwitterApplication.getTwitterClient();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        tweetsAdapter = new TweetsAdapter(new ArrayList<Tweet>());
        rvTimeline.setAdapter(tweetsAdapter);
        rvTimeline.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rvTimeline.setLayoutManager(layoutManager);
        rvTimeline.setHasFixedSize(false);
        rvTimeline.setOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                lastTweet = tweetsAdapter.getLastItem();
                fetchOlder(lastTweet);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRecent();
            }
        });

        fetchRecent();
        return view;
    }

    public abstract void fetchRecent();
    public abstract void fetchOlder(Tweet lastTweet);
    public abstract String getOfflineUrl();

    public void fetchTweetsOffline(RequestParams params, final boolean updateView) {
        String url = getOfflineUrl();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new JsonHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
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

    public RequestParams getParams(int count){
        params = new RequestParams();
        params.put("count", String.valueOf(count));
        return params;
    }
}
