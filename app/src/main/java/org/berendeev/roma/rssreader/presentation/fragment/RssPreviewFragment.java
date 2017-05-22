package org.berendeev.roma.rssreader.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.Util;
import org.berendeev.roma.rssreader.domain.RssFeedRepository;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.App;
import org.berendeev.roma.rssreader.presentation.controller.RssPreviewController;
import org.berendeev.roma.rssreader.presentation.router.Navigator;
import org.berendeev.roma.rssreader.presentation.router.Navigator.RssPreviewRouter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RssPreviewFragment extends Fragment {

    private static final String LINK = "link";
    @BindView(R.id.title) TextView title;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.description) TextView description;

    @BindView(R.id.back_button) ImageButton backButton;
    @BindView(R.id.show_article) Button showArticleButton;

    private RssFeedRepository repository;
    private String link;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof RssPreviewRouter)){
            throw new IllegalArgumentException("activity must implement RssPreviewRouter");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_preview, container, false);
        initDi();
        initData();
        initUi(view);
        return view;
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
            Util.loadImage(getContext(), rssItem.enclosure(), image);
        }
        description.setText(Util.fromHtml(rssItem.description()));
    }

    private void initUi(View view) {
        ButterKnife.bind(this, view);
        //// TODO: 21.05.17 Back arrow
        backButton.setOnClickListener(v -> {

        });
    }

    private void initDi() {
        repository = App.getInstance().getMainComponent().rssFeedRepository();
        RssPreviewController controller = new RssPreviewController((RssPreviewRouter) getActivity());
    }

    public static Fragment getInstance(String link) {
        Fragment fragment = new RssPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        fragment.setArguments(bundle);
        return fragment;
    }
}
