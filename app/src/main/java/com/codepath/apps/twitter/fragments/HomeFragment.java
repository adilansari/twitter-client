package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.twitter.R;

public class HomeFragment extends Fragment {
//    @Bind(R.id.tvFragmentTitle) TextView tvTitle;

    private int mPage;
    public static final String ARG_PAGE = "ARG_PAGE";

    public static HomeFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment frag = new HomeFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvFragmentTitle);
        tvTitle.setText("Fragment num " + mPage);
        return view;
    }
}
