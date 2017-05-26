package org.berendeev.roma.rssreader.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.Util;
import org.berendeev.roma.rssreader.domain.HtmlImageFiller;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.App;
import org.berendeev.roma.rssreader.presentation.controller.RssPreviewController;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter;
import org.berendeev.roma.rssreader.presentation.router.BaseRouter.RssPreviewRouter;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static org.berendeev.roma.rssreader.domain.entity.RssItem.EMPTY;


public class RssPreviewFragment extends Fragment {

    private static final String LINK = "link";
    @BindView(R.id.title) TextView title;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.show_article) Button showArticle;

    @BindView(R.id.back_button) ImageButton backButton;

    @Inject RssFeedRepository repository;
    @Inject HtmlImageFiller filler;
    private String link;
    @Inject RssPreviewController controller;
    private RssItem rssItem = EMPTY;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof RssPreviewRouter)){
            throw new IllegalArgumentException("activity must implement RssPreviewRouter");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_preview, container, false);
        initDi();
        initUi(view);
        initData();
        return view;
    }

    @Override public void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    private void initData() {
        if (rssItem == EMPTY){
            readLink();
            subscribeOnNewData();
        }else {
            fillViews(rssItem);
        }
    }

    private void readLink(){
        link = getArguments().getString(LINK);
    }

    private void subscribeOnNewData(){
        compositeDisposable.add(repository.getRssItem(link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((rssItem, throwable) -> {
                    if(rssItem != EMPTY){
                        this.rssItem = rssItem;
                        fillViews(rssItem);
                    }else {
                        showError(throwable);
                    }
                }));
    }

    private void showError(Throwable throwable) {
        //todo find out what kind of errors can be here
    }

    private void fillViews(RssItem rssItem) {
        title.setText(rssItem.title());
        author.setText(rssItem.author());
        date.setText(formatDate(rssItem.pubDate()));
        if (!rssItem.enclosure().isEmpty()){
            Util.loadImage(getContext(), rssItem.enclosure(), image);
        }
        filler.fillWithImages(description, rssItem.description());
    }

    private String formatDate(long date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext()
                .getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext()
                .getApplicationContext());
        return   timeFormat.format(date) + ", " + dateFormat.format(date);
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
        description.setMovementMethod(LinkMovementMethod.getInstance());
        showArticle.setOnClickListener(v -> {
            controller.showFullArticle(link);
        });
        backButton.setOnClickListener(v -> {
            controller.onBack();
        });
        title.setOnClickListener(v -> {
            controller.showFullArticle(link);
        });
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
        controller.setRouter((RssPreviewRouter) getActivity());
    }

    public static Fragment getInstance(String link) {
        Fragment fragment = new RssPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
