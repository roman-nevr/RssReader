package org.berendeev.roma.rssreader.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berendeev.roma.rssreader.R;


public class SettingsFragment extends Fragment {
    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_preview, container, false);
//        initDi();
//        initData();
//        initUi(view);
        return view;
    }
}
