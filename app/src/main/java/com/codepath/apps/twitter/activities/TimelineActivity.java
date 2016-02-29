package com.codepath.apps.twitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.HomeFragmentPagerAdapter;
import com.codepath.apps.twitter.extensions.Cache;
import com.codepath.apps.twitter.fragments.ComposeFragment;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TwitterClient mClient;
    private Cache mCache;
    private ImageView btnUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCache = Cache.getInstance(this);
        mClient = TwitterApplication.getTwitterClient();
        cacheCurrentUser();

        btnUserProfile = (ImageView) findViewById(R.id.toolbar_btnUserProfile);
        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                User user = User.byId(mCache.getCurrentUserId());
                intent.putExtra("user", Parcels.wrap(user));
                v.getContext().startActivity(intent);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabsTimeline);
        tabs.setViewPager(viewPager);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showComposeDialog(View view){
        FragmentManager fm = this.getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fm, "tag");
    }

    private void cacheCurrentUser(){
        if (mCache.hasCurentUserId())
            return;
        mClient.getCurrentUser(null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    User user = User.fromJson(response);
                    mCache.setCurrentUserId(user.userId);
                    Log.d(TAG, "Setting current user "+ user.userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "Network error! Failed to get current user.");
                Toast.makeText(TimelineActivity.this, "No network detected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
