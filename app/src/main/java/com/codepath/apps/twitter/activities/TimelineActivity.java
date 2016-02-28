package com.codepath.apps.twitter.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.adapters.HomeFragmentPagerAdapter;
import com.codepath.apps.twitter.fragments.ComposeFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimelineActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabsTimeline);
        tabs.setViewPager(viewPager);

        FragmentManager fm = getSupportFragmentManager();
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
}
