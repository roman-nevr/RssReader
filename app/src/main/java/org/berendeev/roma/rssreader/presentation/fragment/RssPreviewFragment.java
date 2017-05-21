package org.berendeev.roma.rssreader.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.Util;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RssPreviewFragment extends Fragment {

    public static final String LINK = "link";
    @BindView(R.id.title) TextView title;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.description) TextView description;

    private RssFeedRepository repository;
    private String link;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_preview, container, false);
        initDi();
        initData();
        initUi(view);
        return view;
        //// TODO: 21.05.17 Back arrow
    }

    private void initData() {
        link = getArguments().getString(LINK);
        repository.getRssItem(link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((rssItem, throwable) -> {
                    if(rssItem != null){
                        fillViews(rssItem);
                    }else {
                        showError(throwable);
                    }
                });
    }

    private void showError(Throwable throwable) {

    }

    private void fillViews(RssItem rssItem) {
        title.setText(rssItem.title());
        author.setText(rssItem.author());
        if (!rssItem.enclosure().isEmpty()){
            Picasso.with(getContext())
                    .load(rssItem.enclosure())
                    .into(image);
        }
        description.setText(Util.fromHtml(rssItem.description()));
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
    }

    private void initDi() {
        repository = App.getInstance().getMainComponent().rssFeedRepository();
    }

    public static Fragment getInstance(String link) {
        Fragment fragment = new RssPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
