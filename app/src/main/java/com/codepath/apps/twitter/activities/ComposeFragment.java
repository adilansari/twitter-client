package com.codepath.apps.twitter.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ComposeFragment extends DialogFragment {

    @Bind(R.id.btnComposeCancel)
    Button btnComposeCancel;
    @Bind(R.id.btnComposeSend)
    Button btnComposeSend;
    @Bind(R.id.etTweetText)
    EditText etTweetText;

    private TwitterClient mClient;
    private static final String TAG = ComposeFragment.class.getSimpleName();

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance() {
        return new ComposeFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mClient = TwitterApplication.getTwitterClient();

        btnComposeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        btnComposeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etTweetText.getText().toString();
                postTweet(text);
            }
        });
    }

    public void postTweet(String text) {

        if (text.length() == 0) {
            Toast.makeText(getContext(), "Cannot send empty tweet.", Toast.LENGTH_SHORT).show();
        } else if (text.length() > 140) {
            Toast.makeText(getContext(), "Tweet too long.", Toast.LENGTH_SHORT).show();
        } else {
            mClient.postTweet(text, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "Posted tweet!");
                    try {
                        Tweet t = Tweet.fromJson(response);
                        TimelineActivity.tweetsAdapter.addTweetToTop(t);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Success! Posted tweet!", Toast.LENGTH_SHORT).show();
                    onDestroyView();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    throwable.printStackTrace();
                    Log.e(TAG, "Failed to post Tweet");
                    Toast.makeText(getContext(), "Failed to post tweet", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
