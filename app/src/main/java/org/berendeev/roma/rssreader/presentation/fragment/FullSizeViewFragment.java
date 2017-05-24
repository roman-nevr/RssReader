package org.berendeev.roma.rssreader.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.App;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FullSizeViewFragment extends Fragment {

    private static final String LINK = "link";
    private String link;
    private RssFeedRepository repository;

    @BindView(R.id.web_view) WebView webView;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_web, container, false);
//        initDi();
        initData();
        initUi(view);
        return view;
    }

    private void initData() {
        link = getArguments().getString(LINK);
//        repository.getRssItem(link)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((rssItem, throwable) -> {
//                    if(rssItem != RssItem.EMPTY){
//                        showArticle(rssItem);
//                    }else {
////                        showError(throwable);
//                    }
//                });
    }

    private void showArticle(RssItem rssItem) {

    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
        //// TODO: 21.05.17 Back arrow
//        backButton.setOnClickListener(v -> {
//
//        });
        webView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        webView.loadUrl(link);
    }

    private void initDi() {
        repository = App.getInstance().getMainComponent().rssFeedRepository();
    }


    public static Fragment getInstance(String link) {
        Fragment fragment = new FullSizeViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
