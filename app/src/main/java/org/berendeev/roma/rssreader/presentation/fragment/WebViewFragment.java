package org.berendeev.roma.rssreader.presentation.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.presentation.App;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewFragment extends Fragment {

    private static final String LINK = "link";
    private String link;

    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.back_button) ImageButton backButton;
    private BaseRouter navigator;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof BaseRouter)){
            throw new IllegalArgumentException("activity must implement RssPreviewRouter");
        }
        navigator = (BaseRouter) getActivity();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_web, container, false);
        initData();
        initUi(view);
        return view;
    }

    private void initData() {
        link = getArguments().getString(LINK);
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl(link);
        backButton.setOnClickListener(v -> {
            navigator.moveBack();
        });
    }

    public static Fragment getInstance(String link) {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
