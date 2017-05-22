package org.berendeev.roma.rssreader.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berendeev.roma.rssreader.R;


public class FullSizeViewFragment extends Fragment {

    private static final String LINK = "link";

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_list, container, false);
//        initDi();
//        initUi(view);
        return view;
    }




    public static Fragment getInstance(String link) {
        Fragment fragment = new RssPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
